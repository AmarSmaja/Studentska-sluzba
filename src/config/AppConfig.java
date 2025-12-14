package config;

import repo.PredmetRepository;
import repo.StudentRepository;
import repo.UpisRepository;
import repo.sqlite.SQLitePredmetRepository;
import repo.sqlite.SQLiteStudentRepository;
import repo.sqlite.SQLiteUpisRepository;
import service.PredmetService;
import service.StudentService;
import service.UpisService;

/**
 * Centralna konfiguracijska klasa aplikacije.
 * <p>Zaduzena je za:
 * <ul>
 *     <li>Instanciranje konkretnih repozitorija (SQLite implementacije).</li>
 *     <li>Instanciranje servisnog sloja ({@link StudentService}), ({@link PredmetService}), ({@link UpisService})</li>
 *     <li>Omogucavanje pristupa istim instancama servisa kroz getter metode.</li>
 * </ul>
 * </p>
 */
public class AppConfig {
    private final StudentRepository studentRepository;
    private final PredmetRepository predmetRepository;
    private final UpisRepository upisRepository;

    private final StudentService studentService;
    private final PredmetService predmetService;
    private final UpisService upisService;

    /**
     * Podrazumijevani konstruktor koji kreire SQLite repozitorij za studente, predmete i upise.
     */
    public AppConfig() {
        this.studentRepository = new SQLiteStudentRepository();
        this.predmetRepository = new SQLitePredmetRepository();
        this.upisRepository = new SQLiteUpisRepository();

        this.studentService = new StudentService(studentRepository, upisRepository);
        this.predmetService = new PredmetService(predmetRepository, upisRepository);
        this.upisService = new UpisService(upisRepository, studentRepository, predmetRepository);
    }

    public StudentService getStudentService() {
        return studentService;
    }

    public PredmetService getPredmetService() {
        return predmetService;
    }

    public UpisService getUpisService() {
        return upisService;
    }
}
