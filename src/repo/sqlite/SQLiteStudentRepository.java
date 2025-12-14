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

/**
 * Implementacija {@link StudentRepository} interfejsa
 * <p>Koristi JDBC i {@link persistance.DBConnectionFactory} za vrsenje CRUD operacija prema bazi.</p>
 */
public class SQLiteStudentRepository implements StudentRepository {
    /**
     * Upisuje novog studenta u bazu
     * @param student Student koji se dodaje u tabelu
     */
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

    /**
     * Azurira podatke o postojecem studentu u bazi.
     * @param student Student sa novim podacima
     */
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

    /**
     * Brise studenta iz tabele na osnovu broja indeksa.
     * @param brojIndeksa Broj indeksa studenta koji se brise, npr. 100/IT-20
     */
    @Override
    public void delete(String brojIndeksa) {
        String sql = "DELETE FROM student WHERE broj_indeksa = ?";

        try (Connection conn = DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, brojIndeksa);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri brisanju studenta", e);
        }
    }

    /**
     * Pronalazi studenta po broju indeksa
     * @param brojIndeksa Broj indeksa studenta, npr. 100/IT-20
     * @return Student, ako postoji
     */
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

    /**
     * Vraca listu svih studenata iz tabele, sortiranu po prezimenu i imenu
     * @return Lista svih studenata
     */
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

    /**
     * Vrsi pretragu studenata po prefiksu
     * @param prefix Prefisk prezimena, npr. S
     * @return Student, ako postoji
     */
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

    /**
     * Mapira red iz ResultSet-a u objekat {@link Student}.
     * Ocekuje rezultat:
     * <ul>
     *     <li>{@code broj_indeksa}</li>
     *     <li>{@code ime}</li>
     *     <li>{@code prezime}</li>
     *     <li>{@code studijski_program}</li>
     *     <li>{@code godina_upisa}</li>
     * </ul>
     * @param rs ResultSet SQL upita
     * @return Novi {@link Student} popunjen podacima
     * @throws SQLException Ako dodje do problema pri citanju podataka
     */
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
