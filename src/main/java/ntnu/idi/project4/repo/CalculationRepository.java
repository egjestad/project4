package ntnu.idi.project4.repo;

import ntnu.idi.project4.model.Calculation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public class CalculationRepository {
  private JdbcTemplate jdbcTemplate;

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
    String sql = "INSERT INTO Calculation (user_id, expression, result) VALUES (?, ?, ?)";
    jdbcTemplate.update(sql, calculation.getUserId(), calculation.getExpression(), calculation.getResult());
  }

  public Calculation findById(int id) {
    String sql = "SELECT * FROM Calculation WHERE id = ?";
    return jdbcTemplate.queryForObject(sql, calculationRowMapper, id);
  }

  public void deleteById(int id) {
    String sql = "DELETE FROM Calculation WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  public Calculation findByUserId(int userId) {
    String sql = "SELECT * FROM Calculation WHERE user_id = ?";
    return jdbcTemplate.queryForObject(sql, calculationRowMapper, userId);
  }

  public void deleteByUserId(int userId) {
    String sql = "DELETE FROM Calculation WHERE user_id = ?";
    jdbcTemplate.update(sql, userId);
  }

  public void updateExpression(int id, String expression) {
    String sql = "UPDATE Calculation SET expression = ? WHERE id = ?";
    jdbcTemplate.update(sql, expression, id);
  }

  public void updateResult(int id, double result) {
    String sql = "UPDATE Calculation SET result = ? WHERE id = ?";
    jdbcTemplate.update(sql, result, id);
  }

  public Calculation getLatestCalculation(int userId) {
    String sql = "SELECT * FROM Calculation WHERE user_id = ? ORDER BY created_at DESC LIMIT 1";
    return jdbcTemplate.queryForObject(sql, calculationRowMapper, userId);
  }

  public List<Calculation> findAll() {
    return jdbcTemplate.query("SELECT * from Calculation", calculationRowMapper);
  }

  public List<Calculation> findAllByUserId(int userId) {
    String sql = "SELECT * from Calculation WHERE user_id = ?";
    return jdbcTemplate.query(sql, calculationRowMapper, userId);
  }

  public List<Calculation> findTenMostRecentCalculations(int userId) {
    String sql = "SELECT * from Calculation WHERE user_id = ? ORDER BY created_at DESC LIMIT 10";
    return jdbcTemplate.query(sql, calculationRowMapper, userId);
  }

  public void deleteAll() {
    String sql = "DELETE from Calculation";
    jdbcTemplate.update(sql);
  }
}
