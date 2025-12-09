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

public class AppConfig {
    private final StudentRepository studentRepository;
    private final PredmetRepository predmetRepository;
    private final UpisRepository upisRepository;

    private final StudentService studentService;
    private final PredmetService predmetService;
    private final UpisService upisService;

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
