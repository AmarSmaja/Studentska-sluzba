package domain;

import java.util.Objects;

public class Predmet {
    private String sifraPredmeta;
    private String naziv;
    private int ects;
    private int semestar;

    public Predmet() {
    }

    public Predmet(String sifraPredmeta, String naziv, int ects, int semestar) {
        this.sifraPredmeta = sifraPredmeta;
        this.naziv = naziv;
        this.ects = ects;
        this.semestar = semestar;
    }

    public String getSifraPredmeta() {
        return sifraPredmeta;
    }

    public void setSifraPredmeta(String sifraPredmeta) {
        this.sifraPredmeta = sifraPredmeta;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getEcts() {
        return ects;
    }

    public void setEcts(int ects) {
        this.ects = ects;
    }

    public int getSemestar() {
        return semestar;
    }

    public void setSemestar(int semestar) {
        this.semestar = semestar;
    }

    @Override
    public String toString() {
        return "Predmet{" +
                "sifraPredmeta='" + sifraPredmeta + '\'' +
                ", naziv='" + naziv + '\'' +
                ", ects=" + ects +
                ", semestar=" + semestar +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Predmet)) return false;
        Predmet predmet = (Predmet) o;
        return Objects.equals(sifraPredmeta, predmet.sifraPredmeta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sifraPredmeta);
    }
}
