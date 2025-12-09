package repo.sqlite;

import domain.Student;
import persistance.DBConnectionFactory;
import repo.StudentRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SQLiteStudentRepository implements StudentRepository {
    @Override
    public void save(Student student) {
        String sql = """
                INSERT INTO student (broj_indeksa, ime, prezime, studijski_program, godina_upisa)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getBrojIndeksa());
            ps.setString(2, student.getIme());
            ps.setString(3, student.getPrezime());
            ps.setString(4, student.getStudijskiProgram());
            ps.setInt(5, student.getGodinaUpisa());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri spremanju studenta", e);
        }
    }

    @Override
    public void update(Student student) {
        String sql = """
                UPDATE student
                SET ime = ?, prezime = ?, studijski_program = ?, godina_upisa = ?
                WHERE broj_indeksa = ?
                """;

        try (Connection conn = DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getIme());
            ps.setString(2, student.getPrezime());
            ps.setString(3, student.getStudijskiProgram());
            ps.setInt(4, student.getGodinaUpisa());
            ps.setString(5, student.getBrojIndeksa());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri ažuriranju studenta", e);
        }
    }

    @Override
    public void delete(String brojIndeksa) {
        String sql = "DELETE FROM student WHERE broj_indeksa = ?";

        try (Connection conn = DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, brojIndeksa);
            ps.executeUpdate();
        } catch (SQLException e) {
            // Ako student ima upise, ovdje ćeš dobiti SQL grešku zbog FK (RESTRICT) – to možeš uhvatiti u service sloju
            throw new RuntimeException("Greška pri brisanju studenta", e);
        }
    }

    @Override
    public Optional<Student> findById(String brojIndeksa) {
        String sql = "SELECT * FROM student WHERE broj_indeksa = ?";

        try (Connection conn = DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, brojIndeksa);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri findById za studenta", e);
        }
    }

    @Override
    public List<Student> findAll() {
        String sql = "SELECT * FROM student ORDER BY prezime, ime";
        List<Student> result = new ArrayList<>();

        try (Connection conn = DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri findAll za studenta", e);
        }

        return result;
    }

    @Override
    public List<Student> findByPrezimePrefix(String prefix) {
        String sql = "SELECT broj_indeksa, ime, prezime, studijski_program, godina_upisa " +
                "FROM student WHERE prezime LIKE ? ORDER BY prezime, ime";

        List<Student> rezultati = new ArrayList<>();

        try (Connection conn = DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, prefix + "%");   // prefix search

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Student s = new Student(
                            rs.getString("broj_indeksa"),
                            rs.getString("ime"),
                            rs.getString("prezime"),
                            rs.getString("studijski_program"),
                            rs.getInt("godina_upisa")
                    );
                    rezultati.add(s);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri findByPrezimePrefix", e);
        }

        return rezultati;
    }

    private Student mapRow(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setBrojIndeksa(rs.getString("broj_indeksa"));
        s.setIme(rs.getString("ime"));
        s.setPrezime(rs.getString("prezime"));
        s.setStudijskiProgram(rs.getString("studijski_program"));
        s.setGodinaUpisa(rs.getInt("godina_upisa"));
        return s;
    }
}
