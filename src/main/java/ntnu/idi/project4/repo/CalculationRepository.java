package ntnu.idi.project4.repo;

import ntnu.idi.project4.model.Calculation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CalculationRepository {
  private JdbcTemplate jdbcTemplate;
  private String TABLE_NAME = "calculations";

  private final RowMapper<Calculation> calculationRowMapper = (rs, rowNum) -> {
    Calculation calculation = new Calculation();
    calculation.setId(rs.getInt("id"));
    calculation.setUserId(rs.getInt("user_id"));
    calculation.setExpression(rs.getString("expression"));
    calculation.setResult(rs.getDouble("result"));
    calculation.setTimestamp(rs.getTimestamp("created_at"));
    return calculation;
  };

  public CalculationRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public void save(Calculation calculation) {
    String sql = "INSERT INTO "+ TABLE_NAME +" (user_id, expression, result) VALUES (?, ?, ?)";
    jdbcTemplate.update(sql, calculation.getUserId(), calculation.getExpression(), calculation.getResult());
  }

  public Calculation findById(int id) {
    String sql = "SELECT * FROM "+ TABLE_NAME +" WHERE id = ?";
    return jdbcTemplate.queryForObject(sql, calculationRowMapper, id);
  }

  public void deleteById(int id) {
    String sql = "DELETE FROM "+ TABLE_NAME +" WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }


  public Calculation findByUserId(int userId) {
    String sql = "SELECT * FROM " + TABLE_NAME + " WHERE user_id = ?";
    return jdbcTemplate.queryForObject(sql, calculationRowMapper, userId);
  }

  public void deleteByUserId(int userId) {
    String sql = "DELETE FROM " + TABLE_NAME + " WHERE user_id = ?";
    jdbcTemplate.update(sql, userId);
  }

  public void updateExpression(int id, String expression) {
    String sql = "UPDATE " + TABLE_NAME + " SET expression = ? WHERE id = ?";
    jdbcTemplate.update(sql, expression, id);
  }

  public void updateResult(int id, double result) {
    String sql = "UPDATE " + TABLE_NAME + " SET result = ? WHERE id = ?";
    jdbcTemplate.update(sql, result, id);
  }

  public Calculation getLatestCalculation(int userId) {
    String sql = "SELECT * FROM " + TABLE_NAME + " WHERE user_id = ? ORDER BY created_at DESC LIMIT 1";
    return jdbcTemplate.queryForObject(sql, calculationRowMapper, userId);
  }

  public List<Calculation> findAll() {
    return jdbcTemplate.query("SELECT * from " + TABLE_NAME, calculationRowMapper);
  }

  public List<Calculation> findAllByUserId(int userId) {
    String sql = "SELECT * from " + TABLE_NAME + " WHERE user_id = ?";
    return jdbcTemplate.query(sql, calculationRowMapper, userId);
  }

  public List<Calculation> findTenMostRecentCalculations(int userId) {
    String sql = "SELECT * from " + TABLE_NAME + " WHERE user_id = ? ORDER BY created_at DESC LIMIT 10";
    return jdbcTemplate.query(sql, calculationRowMapper, userId);
  }

  public void deleteAll() {
    String sql = "DELETE from " + TABLE_NAME;
    jdbcTemplate.update(sql);
  }

  public List<Calculation> findRecentCalculations(int userId, int limit) {
    String sql = "SELECT * from " + TABLE_NAME + " WHERE user_id = ? ORDER BY created_at DESC LIMIT ?";
    return jdbcTemplate.query(sql, calculationRowMapper, userId, limit);
  }
}
