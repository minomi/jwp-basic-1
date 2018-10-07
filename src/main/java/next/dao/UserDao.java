package next.dao;

import java.util.List;

import next.model.User;
import org.springframework.jdbc.core.PreparedStatementSetter;

public class UserDao {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    public void insert(User user) {
        PreparedStatementSetter pstmtSetter = pstmt -> {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
        };
        jdbcTemplate.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", pstmtSetter);
    }

    public void update(User user) {
        jdbcTemplate.update("UPDATE USERS SET password=?, name=?, email=? WHERE userId=?",
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getUserId());
    }

    public List<User> findAll() {
        RowMapper<User> rowMapper = rs -> {
            String id = rs.getString("userId");
            String pw = rs.getString("password");
            String name = rs.getString("name");
            String email = rs.getString("email");
            return new User(id, pw, name, email);
        };
        return jdbcTemplate.query("SELECT * FROM USERS",
                null, rowMapper);
    }

    public User findByUserId(String userId) {
        return  jdbcTemplate.queryForObject("SELECT userId, password, name, email FROM USERS WHERE userid=?",
                pstmt -> pstmt.setString(1, userId),
                rs -> User.from(rs));
    }
}
