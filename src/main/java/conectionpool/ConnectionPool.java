package conectionpool;

import exception.ConnectionPoolException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ConnectionPool {
    private static final String DB_CONNECTION_POOL_PROPERTIES = "connectionPool.properties";
    private static final String URL_PROPERTY_NAME = "url";
    private static final String USER_PROPERTY_NAME = "user";
    private static final String PASSWORD_PROPERTY_NAME = "password";
    private static final int DEFAULT_POOL_SIZE = 10;
    private static final int DEFAULT_CONNECTION_AWAITING_TIMEOUT = 30;

    private final LinkedBlockingQueue<Connection> availableConnections;
    private final List<Connection> usedConnections;
    private final AtomicBoolean isInitialized;
    private final AtomicBoolean isPoolClosing;
    private final Lock initLock;
    private final Properties properties;

    private String url;

    private ConnectionPool() {
        availableConnections = new LinkedBlockingQueue<>();
        usedConnections = new LinkedList<>();
        isInitialized = new AtomicBoolean(false);
        isPoolClosing = new AtomicBoolean(false);
        initLock = new ReentrantLock();
        properties = new Properties();
    }

    private static class ConnectionPoolSingletonHolder {
        static final ConnectionPool INSTANCE = new ConnectionPool();
    }

    public static ConnectionPool getInstance() {
        return ConnectionPoolSingletonHolder.INSTANCE;
    }

    public void init() throws ConnectionPoolException {
        if (!isInitialized.get()) {
            try {
                initLock.lock();
                Class.forName("org.postgresql.Driver");
                initProperties(DB_CONNECTION_POOL_PROPERTIES);
                createDatasource();
                createConnections(DEFAULT_POOL_SIZE);
                isInitialized.set(true);
            } catch (ClassNotFoundException e) {
                log.error("Connection pool is not initialized ", e);
                throw new ConnectionPoolException("Connection pool is not initialized ", e);
            } finally {
                initLock.unlock();
            }
        }
    }

    private void createDatasource() throws ConnectionPoolException {
        try (Connection conn = DriverManager.getConnection(
                properties.getProperty(URL_PROPERTY_NAME),
                properties.getProperty(USER_PROPERTY_NAME),
                properties.getProperty(PASSWORD_PROPERTY_NAME)
        );
             Statement statement = conn.createStatement();
        ) {
            String sql = String.format("create database %s", properties.getProperty("database"));
            statement.executeUpdate(sql);
            log.info("Database created successfully.");
        } catch (SQLException e) {
            log.error("Can't create database", e);
            throw new ConnectionPoolException("Database was not created.");
        }
    }

    public Connection getConnection() throws ConnectionPoolException {
        if (!isPoolClosing.get()) {
            Connection connection = null;
            if (!availableConnections.isEmpty()) {
                try {
                    connection = availableConnections.poll(DEFAULT_CONNECTION_AWAITING_TIMEOUT, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    log.error("Can't get connection", e);
                    Thread.currentThread().interrupt();
                }
                usedConnections.add(connection);
            }
            return connection;
        }
        throw new ConnectionPoolException("Connections are closing now");
    }

    public void destroy() {
        isPoolClosing.set(true);
        initLock.lock();
        availableConnections.stream()
                .map(ProxyConnection.class::cast)
                .forEach(this::closeConnection);
        usedConnections.stream()
                .map(ProxyConnection.class::cast)
                .forEach(this::closeConnection);

        availableConnections.clear();
        usedConnections.clear();
        isInitialized.set(false);
        isPoolClosing.set(false);
        initLock.unlock();
    }

    private void closeConnection(ProxyConnection connection) {
        try {
            connection.forceClose();
        } catch (SQLException e) {
            log.warn("Can't close connection", e);
        }
    }

    void releaseConnection(Connection connection) {
        if (connection != null) {
            usedConnections.remove(connection);
            availableConnections.add(connection);
        } else {
            log.warn("Attempted to release null connection");
        }
    }

    private void createConnections(int connectionsCount) {
        for (int i = 0; i < connectionsCount; i++) {
            try {
                ProxyConnection proxyConnection = new ProxyConnection(DriverManager.getConnection(url, properties));
                proxyConnection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
                availableConnections.add(proxyConnection);
            } catch (SQLException e) {
                log.warn("Connection not created", e);
            }
        }
    }

    private void initProperties(String propertiesFileName) throws ConnectionPoolException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFileName)) {
            properties.load(inputStream);
            if (properties.isEmpty()) {
                log.error("DB properties has not been loaded.");
                throw new ConnectionPoolException("DB properties has not been loaded.");
            }
            url = properties.getProperty(URL_PROPERTY_NAME);
        } catch (IOException e) {
            log.error("DB properties has not been loaded ", e);
            throw new ConnectionPoolException("DB properties has not been loaded ", e);
        }
    }
}
