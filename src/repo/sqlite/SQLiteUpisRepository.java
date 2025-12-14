package repo.sqlite;

import domain.Student;
import domain.Upis;
import persistance.DBConnectionFactory;
import repo.UpisRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementacija {@link UpisRepository} interfejsa koja koristi SQLite bazu podataka.
 * <p>Koristi JDBC i {@link persistance.DBConnectionFactory} za vrsenje CRUD operacija prema bazi.</p>
 */
public class SQLiteUpisRepository implements UpisRepository {
    /**
     * Upisuje novi zapis u tabelu
     * @param upis Upis koji se dodaje u bazu.
     */
    @Override
    public void save(Upis upis) {
        String sql = """
                INSERT INTO upis (broj_indeksa, sifra_predmeta, akademska_godina, ocjena, razlog_izmjene)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, upis.getBrojIndeksa());
            ps.setString(2, upis.getSifraPredmeta());
            ps.setString(3, upis.getAkademskaGodina());

            if (upis.getOcjena() == null) {
                ps.setNull(4, Types.INTEGER);
            } else {
                ps.setInt(4, upis.getOcjena());
            }

            if (upis.getRazlogIzmjene() == null) {
                ps.setNull(5, Types.VARCHAR);
            } else {
                ps.setString(5, upis.getRazlogIzmjene());
            }

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    upis.setId(keys.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Greška pri spremanju upisa", e);
        }
    }

    /**
     * Azurira postojeci zapis u tabeli
     * @param upis Upis sa novim podacima u tabelu.
     */
    @Override
    public void update(Upis upis) {
        String sql = """
                UPDATE upis
                SET broj_indeksa = ?, sifra_predmeta = ?, akademska_godina = ?, ocjena = ?, razlog_izmjene = ?
                WHERE id = ?
                """;

        try (Connection conn = DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, upis.getBrojIndeksa());
            ps.setString(2, upis.getSifraPredmeta());
            ps.setString(3, upis.getAkademskaGodina());

            if (upis.getOcjena() == null) {
                ps.setNull(4, Types.INTEGER);
            } else {
                ps.setInt(4, upis.getOcjena());
            }

            if (upis.getRazlogIzmjene() == null) {
                ps.setNull(5, Types.VARCHAR);
            } else {
                ps.setString(5, upis.getRazlogIzmjene());
            }

            ps.setLong(6, upis.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Greška pri ažuriranju upisa", e);
        }
    }

    /**
     * Brise upis iz baze na osnovu ID-a.
     * @param id ID upisa koji se brise, npr. 5
     */
    @Override
    public void delete(long id) {
        String sql = "DELETE FROM upis WHERE id = ?";

        try (Connection conn = DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Greška pri brisanju upisa", e);
        }
    }

    /**
     * Vrsi pretragu po ID-u upisa
     * @param id ID upisa koji se pretrazuje, npr. 5
     * @return Upis, ako postoji
     */
    @Override
    public Optional<Upis> findById(long id) {
        String sql = "SELECT * FROM upis WHERE id = ?";

        try (Connection conn = DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Greška pri findById za upis", e);
        }
    }

    /**
     * Vraca sve upise za nekog studenta.
     * @param brojIndeksa Broj indeksa studenta, npr. 100/IT-20
     * @return Lista upisa studenta, ako postoji
     */
    @Override
    public List<Upis> findByStudent(String brojIndeksa) {
        String sql = "SELECT * FROM upis WHERE broj_indeksa = ? ORDER BY akademska_godina, sifra_predmeta";
        List<Upis> result = new ArrayList<>();

        try (Connection conn = DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, brojIndeksa);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Greška pri findByStudent", e);
        }

        return result;
    }

    /**
     * Vraca sve upise za odredjenog studenta.
     * @param brojIndeksa Broj indeksa studenta, npr. 100/IT-20
     * @param akademskaGodina Akademska godina, npr. 2020./21.
     * @return Lista upisa po indeksu i godini, ako postoji
     */
    @Override
    public List<Upis> findByStudentAndGodina(String brojIndeksa, String akademskaGodina) {
        String sql = "SELECT * FROM upis WHERE broj_indeksa = ? AND akademska_godina = ? ORDER BY sifra_predmeta";
        List<Upis> result = new ArrayList<>();

        try (Connection conn = DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, brojIndeksa);
            ps.setString(2, akademskaGodina);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Greška pri findByStudentAndGodina", e);
        }

        return result;
    }

    /**
     * Vraca sve upise po sifri predmeta.
     * @param sifraPredmeta Sifra predmeta, npr. MAT1
     * @return Lista predmeta, ako postoji
     */
    @Override
    public List<Upis> findByPredmet(String sifraPredmeta) {
        String sql = "SELECT * FROM upis WHERE sifra_predmeta = ? ORDER BY akademska_godina, broj_indeksa";
        List<Upis> result = new ArrayList<>();

        try (Connection conn = DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sifraPredmeta);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Greška pri findByPredmet", e);
        }

        return result;
    }

    /**
     * Provjerava da li vec postoji upis istog studenta na isti predmet u istoj akademskoj godini.
     * @param brojIndeksa Broj indeksa studenta, npr. 100/IT-20
     * @param sifraPredmeta Sifra predmeta, npr. MAT1
     * @param akademskaGodina Akademska godina, npr. 2020./21.
     * @return {@code true} ako postoji barem jedan upis, inace {@code false}.
     */
    @Override
    public boolean exists(String brojIndeksa, String sifraPredmeta, String akademskaGodina) {
        String sql = """
                SELECT 1 FROM upis
                WHERE broj_indeksa = ? AND sifra_predmeta = ? AND akademska_godina = ?
                """;

        try (Connection conn = DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, brojIndeksa);
            ps.setString(2, sifraPredmeta);
            ps.setString(3, akademskaGodina);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Greška pri exists za upis", e);
        }
    }

    /**
     * Provjerava da li student ima barem jedan upis u tabeli.
     * @param brojIndeksa Broj indeksa studenta, npr. 100/IT-20
     * @return {@code true} ako postoji barem jedan upis za studenta, inace {@code false}.
     */
    @Override
    public boolean existsForStudent(String brojIndeksa) {
        String sql = "SELECT 1 FROM upis WHERE broj_indeksa = ? LIMIT 1";

        try (Connection conn = DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, brojIndeksa);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri existsForStudent", e);
        }
    }

    /**
     * Provjerava da li predmet ima barem jedan upis u tabeli.
     * @param sifraPredmeta Sifra predmeta, npr. MAT1
     * @return {@code true} ako postoji barem jedan upis za predmet, inace {@code false}.
     */
    @Override
    public boolean existsForPredmet(String sifraPredmeta) {
        String sql = "SELECT 1 FROM upis WHERE sifra_predmeta = ? LIMIT 1";

        try (Connection conn = DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sifraPredmeta);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri existsForPredmet", e);
        }
    }

    /**
     * Mapira red iz ResultSet-a u objekat {@link Upis}.
     * Ocekuje rezultat:
     * <ul>
     *     <li>{@code id}</li>
     *     <li>{@code broj_indeksa}</li>
     *     <li>{@code sifra_predmeta}</li>
     *     <li>{@code akademska_godina}</li>
     *     <li>{@code ocjena}</li>
     *     <li>{@code razlog_izmjene}</li>
     * </ul>
     * @param rs Rezultat SQL upita
     * @return Novi {@link Upis} popunjen podacima.
     * @throws SQLException Ako dodje do problema pri citanju podataka.
     */
    private Upis mapRow(ResultSet rs) throws SQLException {
        Upis u = new Upis();
        u.setId(rs.getLong("id"));
        u.setBrojIndeksa(rs.getString("broj_indeksa"));
        u.setSifraPredmeta(rs.getString("sifra_predmeta"));
        u.setAkademskaGodina(rs.getString("akademska_godina"));

        int ocjena = rs.getInt("ocjena");
        if (rs.wasNull()) {
            u.setOcjena(null);
        } else {
            u.setOcjena(ocjena);
        }

        String razlog = rs.getString("razlog_izmjene");
        if (rs.wasNull()) {
            u.setRazlogIzmjene(null);
        } else {
            u.setRazlogIzmjene(razlog);
        }

        return u;
    }
}
