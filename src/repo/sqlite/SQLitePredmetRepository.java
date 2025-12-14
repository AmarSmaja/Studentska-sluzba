package repo.sqlite;

import domain.Predmet;
import persistance.DBConnectionFactory;
import repo.PredmetRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementacija {@link PredmetRepository} interfejsa koja koristi SQLite bazu podataka.
 * <p>Koristi JDBC i {@link persistance.DBConnectionFactory} za vrsenje CRUD operacija prema bazi.</p>
 */
public class SQLitePredmetRepository implements PredmetRepository {
    @Override
    public void save(Predmet predmet) {
        String sql = """
                INSERT INTO predmet (sifra_predmeta, naziv, ects, semestar)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, predmet.getSifraPredmeta());
            ps.setString(2, predmet.getNaziv());
            ps.setInt(3, predmet.getEcts());
            ps.setInt(4, predmet.getSemestar());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Greška pri spremanju predmeta", e);
        }
    }

    /**
     * Vrsi azuriranje nad Predmetom
     * @param predmet ID Predmeta
     */
    @Override
    public void update(Predmet predmet) {
        String sql = """
                UPDATE predmet
                SET naziv = ?, ects = ?, semestar = ?
                WHERE sifra_predmeta = ?
                """;

        try (Connection conn = DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, predmet.getNaziv());
            ps.setInt(2, predmet.getEcts());
            ps.setInt(3, predmet.getSemestar());
            ps.setString(4, predmet.getSifraPredmeta());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Greška pri ažuriranju predmeta", e);
        }
    }

    /**
     * Vrsi brisanje nad Predmetom
     * @param sifraPredmeta Sifra predmeta koji se brise, npr. MAT1
     */
    @Override
    public void delete(String sifraPredmeta) {
        String sql = "DELETE FROM predmet WHERE sifra_predmeta = ?";

        try (Connection conn = DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sifraPredmeta);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Greška pri brisanju predmeta", e);
        }
    }

    /**
     * Vrsi pretragu predmeta na osnovu sifre predmeta
     * @param sifraPredmeta Sifra predmeta koji trazimo, npr. MAT1
     * @return Predmet, ako postoji
     */
    @Override
    public Optional<Predmet> findById(String sifraPredmeta) {
        String sql = "SELECT * FROM predmet WHERE sifra_predmeta = ?";

        try (Connection conn = DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sifraPredmeta);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Greška pri findById za predmet", e);
        }
    }

    /**
     * Vraca listu svih predmeta.
     * @return Lista predmeta
     */
    @Override
    public List<Predmet> findAll() {
        String sql = "SELECT * FROM predmet ORDER BY sifra_predmeta";
        List<Predmet> result = new ArrayList<>();

        try (Connection conn = DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Greška pri findAll za predmet", e);
        }

        return result;
    }

    /**
     * Pretraga predmeta po prefiksu
     * @param nazivPrefix Prefiks predmeta kojeg trazimo, npr. M
     * @return Predmet, ako postoji
     */
    @Override
    public List<Predmet> findByNazivPrefix(String nazivPrefix) {
        String sql = "SELECT * FROM predmet WHERE naziv LIKE ? ORDER BY naziv";
        List<Predmet> result = new ArrayList<>();

        try (Connection conn = DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nazivPrefix + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Greška pri findByNazivPrefix za predmet", e);
        }

        return result;
    }

    /**
     * Pretraga predmeta po sifri predmeta
     * @param sifra Sifra predmeta, npr. MAT1
     * @return Predmet, ako postoji
     */
    @Override
    public Optional<Predmet> findBySifra(String sifra) {
        String sql = "SELECT sifra_predmeta, naziv, ects, semestar FROM predmet WHERE sifra_predmeta = ?";

        try (Connection conn = DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sifra);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Predmet p = new Predmet(
                            rs.getString("sifra_predmeta"),
                            rs.getString("naziv"),
                            rs.getInt("ects"),
                            rs.getInt("semestar")
                    );
                    return Optional.of(p);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri findBySifra", e);
        }
    }

    /**
     * Mapira red iz ResultSet-a u objekat Predmet.
     * Ocekuje rezultat:
     * <ul>
     *     <li>{@code sifra_predmeta}</li>
     *     <li>{@code naziv}</li>
     *     <li>{@code ects}</li>
     *     <li>{@code semestar}</li>
     * </ul>
     * @param rs Rezultat SQL upita
     * @return Novi predmet popunjen podacima
     * @throws SQLException Ako dodje do problema pri citanju podataka
     */
    private Predmet mapRow(ResultSet rs) throws SQLException {
        Predmet p = new Predmet();
        p.setSifraPredmeta(rs.getString("sifra_predmeta"));
        p.setNaziv(rs.getString("naziv"));
        p.setEcts(rs.getInt("ects"));
        p.setSemestar(rs.getInt("semestar"));
        return p;
    }
}
