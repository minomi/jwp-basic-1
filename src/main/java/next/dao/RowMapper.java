package next.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * IDE : IntelliJ IDEA
 * Created by minho on 2018. 10. 7..
 */
public interface RowMapper<T> {
    T mapRow(ResultSet rs) throws SQLException;
}
