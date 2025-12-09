package domain;

import java.util.Objects;

public class Student {
    private String brojIndeksa;
    private String ime;
    private String prezime;
    private String studijskiProgram;
    private int godinaUpisa;

    public Student() {

    }

    public Student(String brojIndeksa, String ime, String prezime, String studijskiProgram, int godinaUpisa) {
        this.brojIndeksa = brojIndeksa;
        this.ime = ime;
        this.prezime = prezime;
        this.studijskiProgram = studijskiProgram;
        this.godinaUpisa = godinaUpisa;
    }

    public String getBrojIndeksa() {
        return brojIndeksa;
    }

    public void setBrojIndeksa(String brojIndeksa) {
        this.brojIndeksa = brojIndeksa;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getStudijskiProgram() {
        return studijskiProgram;
    }

    public void setStudijskiProgram(String studijskiProgram) {
        this.studijskiProgram = studijskiProgram;
    }

    public int getGodinaUpisa() {
        return godinaUpisa;
    }

    public void setGodinaUpisa(int godinaUpisa) {
        this.godinaUpisa = godinaUpisa;
    }

    public String getPunoIme() {
        return ime + " " + prezime;
    }

    @Override
    public String toString() {
        return "Student{" +
                "brojIndeksa='" + brojIndeksa + '\'' +
                ", ime='" + ime + '\'' +
                ", prezime='" + prezime + '\'' +
                ", studijskiProgram='" + studijskiProgram + '\'' +
                ", godinaUpisa=" + godinaUpisa +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return Objects.equals(brojIndeksa, student.brojIndeksa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brojIndeksa);
    }
}
