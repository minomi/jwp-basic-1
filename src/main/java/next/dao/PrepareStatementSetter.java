package next.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * IDE : IntelliJ IDEA
 * Created by minho on 2018. 10. 7..
 */
public interface PrepareStatementSetter {
    void setValues(PreparedStatement pstmt) throws SQLException;
}
