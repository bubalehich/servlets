package dao;

import dao.dto.DaoPositionDto;
import entity.Position;

import java.sql.SQLException;
import java.util.List;

public interface PositionDaoInterface {

    Position save(Position position) throws SQLException;

    boolean delete(Long id) throws SQLException;

    List<DaoPositionDto> findByCashReceiptId(Long id) throws SQLException;
}
