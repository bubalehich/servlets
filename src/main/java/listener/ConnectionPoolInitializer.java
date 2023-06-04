package listener;

import conectionpool.ConnectionPool;
import exception.ConnectionPoolException;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.slf4j.Slf4j;

@WebListener
@Slf4j
public class ConnectionPoolInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            log.info("Creating connection pool...");
            ConnectionPool.getInstance().init();
            log.info("Init connection pool success.");
        } catch (ConnectionPoolException e) {
            log.error("Fail to init  connection pool: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            log.info("Destroying connection pool...");
            ConnectionPool.getInstance().destroy();
            log.info("Connection pool was destroyed.");
        } catch (Exception e) {
            log.error("Fail to destroy connection pool: ", e);
        }
    }
}
