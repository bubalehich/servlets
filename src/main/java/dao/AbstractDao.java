package dao;

import conectionpool.ConnectionPool;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AbstractDao {

    protected final ConnectionPool pool;

    protected AbstractDao() {
        pool = ConnectionPool.getInstance();
    }
}
