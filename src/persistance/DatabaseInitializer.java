package persistance;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void main(String[] args) {
        try (Connection conn = DBConnectionFactory.getConnection();
             Statement st = conn.createStatement()) {

            st.execute("""
                    CREATE TABLE IF NOT EXISTS student (
                        broj_indeksa      TEXT PRIMARY KEY,
                        ime               TEXT NOT NULL,
                        prezime           TEXT NOT NULL,
                        studijski_program TEXT NOT NULL,
                        godina_upisa      INTEGER NOT NULL
                            CHECK (godina_upisa BETWEEN 2020 AND 2050)
                    )
                    """);

            st.execute("""
                    CREATE TABLE IF NOT EXISTS predmet (
                        sifra_predmeta   TEXT PRIMARY KEY,
                        naziv            TEXT NOT NULL,
                        ects             INTEGER NOT NULL
                            CHECK (ects BETWEEN 1 AND 15),
                        semestar         INTEGER NOT NULL
                            CHECK (semestar BETWEEN 1 AND 10)
                    )
                    """);

            st.execute("""
                    CREATE TABLE IF NOT EXISTS upis (
                        id               INTEGER PRIMARY KEY AUTOINCREMENT,
                        broj_indeksa     TEXT NOT NULL,
                        sifra_predmeta   TEXT NOT NULL,
                        akademska_godina TEXT NOT NULL,   -- npr. '2024/25'
                        ocjena           INTEGER
                            CHECK (ocjena IS NULL OR (ocjena BETWEEN 5 AND 10)),
                        razlog_izmjene   TEXT,

                        FOREIGN KEY (broj_indeksa)
                            REFERENCES student (broj_indeksa)
                            ON DELETE RESTRICT,

                        FOREIGN KEY (sifra_predmeta)
                            REFERENCES predmet (sifra_predmeta)
                            ON DELETE RESTRICT,

                        UNIQUE (broj_indeksa, sifra_predmeta, akademska_godina)
                    )
                    """);

            System.out.println("Sve tabele su spremne.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
