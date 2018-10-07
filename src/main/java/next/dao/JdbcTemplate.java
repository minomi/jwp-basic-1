package next.dao;

import core.jdbc.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * IDE : IntelliJ IDEA
 * Created by minho on 2018. 10. 7..
 */
class JdbcTemplate {

    private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);

    <T> List<T> query(String sql,
                  PreparedStatementSetter pstmtSetter,
                  RowMapper<T> rowMapper) {
        ResultSet rs = null;
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (pstmtSetter != null) { pstmtSetter.setValues(pstmt); }
            rs = pstmt.executeQuery();
            List<T> objects = new ArrayList<>();
            while (rs.next()) {
                objects.add(rowMapper.mapRow(rs));
            }
            return objects;
        } catch (SQLException e) {
            log.error(e.getMessage());
        } finally {
            close(rs);
        }
        throw new DataAccessException();
    }

    <T> T queryForObject(String sql,
                         PreparedStatementSetter pstmtSetter,
                         RowMapper<T> rowMapper) {
        List <T> objects = query(sql, pstmtSetter, rowMapper);
        if (objects.isEmpty()) {
            return null;
        }
        return objects.get(0);
    }

    void update(String query, PreparedStatementSetter pstmtSetter) {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)){
            pstmtSetter.setValues(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    void update(String query, Object... values) {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)){
            for (int i = 0 ; i < values.length ; i++) {
                pstmt.setObject(i + 1, values[i]);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    private void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        }
    }
}
