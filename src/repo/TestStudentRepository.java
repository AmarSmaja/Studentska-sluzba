package repo;

import domain.Student;
import repo.sqlite.SQLiteStudentRepository;

public class TestStudentRepository {
    public static void main(String[] args) {
        StudentRepository repo = new SQLiteStudentRepository();

        Student s = new Student("123/2022", "Amar", "Smaic", "Informatika", 2022);
        repo.save(s);
        System.out.println("Student upisan.");

        repo.findById("123/2022").ifPresentOrElse(
                found -> System.out.println("Nađen student: " + found),
                () -> System.out.println("Student nije pronađen")
        );

        System.out.println("Svi studenti u bazi:");
        repo.findAll().forEach(System.out::println);
    }
}
