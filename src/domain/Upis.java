package domain;

import java.util.Objects;

/**
 * Predstavlja upis ocjene odredjenom studentu.
 * Sadrzi ID unosa, broj indeksa studenta kojem unosimo,
 * akademsku godinu, ocjenu i razlog izmjene ukoliko
 * mijenjamo ocjenu.
 */
public class Upis {
    private long id;
    private String brojIndeksa;
    private String sifraPredmeta;
    private String akademskaGodina;   // npr. "2024/25"
    private Integer ocjena;           // može biti null ako još nema ocjene
    private String razlogIzmjene;     // null ako nije bilo izmjene

    public Upis() {
    }

    /**
     *
     * @param id ID Upisa, svaki upis ima drugi ID, npr. 10
     * @param brojIndeksa Broj indeksa studenta kojem unosimo ocjenu, npr. 100/IT-20
     * @param sifraPredmeta Sifra predmeta za koji unosimo ocjenu, npr. MAT1
     * @param akademskaGodina Akademska godina u kojem je student upisao ocjenu, npr. 2020./21.
     * @param ocjena Ocjena koju je upisao iz predmeta, npr. 7
     * @param razlogIzmjene Razlog izmjene ocjene, ukoliko postoji razlog izmjene
     */
    public Upis(long id, String brojIndeksa, String sifraPredmeta,
                String akademskaGodina, Integer ocjena, String razlogIzmjene) {
        this.id = id;
        this.brojIndeksa = brojIndeksa;
        this.sifraPredmeta = sifraPredmeta;
        this.akademskaGodina = akademskaGodina;
        this.ocjena = ocjena;
        this.razlogIzmjene = razlogIzmjene;
    }

    /**
     *
     * @param brojIndeksa Broj indeksa studenta kojem unosimo ocjenu, npr. 100/IT-20
     * @param sifraPredmeta Sifra predmeta za koji unosimo ocjenu, npr. MAT1
     * @param akademskaGodina Akademska godina u kojem je student upisao ocjenu, npr. 2020./21.
     */
    public Upis(String brojIndeksa, String sifraPredmeta, String akademskaGodina) {
        this(0, brojIndeksa, sifraPredmeta, akademskaGodina, null, null);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBrojIndeksa() {
        return brojIndeksa;
    }

    public void setBrojIndeksa(String brojIndeksa) {
        this.brojIndeksa = brojIndeksa;
    }

    public String getSifraPredmeta() {
        return sifraPredmeta;
    }

    public void setSifraPredmeta(String sifraPredmeta) {
        this.sifraPredmeta = sifraPredmeta;
    }

    public String getAkademskaGodina() {
        return akademskaGodina;
    }

    public void setAkademskaGodina(String akademskaGodina) {
        this.akademskaGodina = akademskaGodina;
    }

    public Integer getOcjena() {
        return ocjena;
    }

    public void setOcjena(Integer ocjena) {
        this.ocjena = ocjena;
    }

    public String getRazlogIzmjene() {
        return razlogIzmjene;
    }

    public void setRazlogIzmjene(String razlogIzmjene) {
        this.razlogIzmjene = razlogIzmjene;
    }

    @Override
    public String toString() {
        return "Upis{" +
                "id=" + id +
                ", brojIndeksa='" + brojIndeksa + '\'' +
                ", sifraPredmeta='" + sifraPredmeta + '\'' +
                ", akademskaGodina='" + akademskaGodina + '\'' +
                ", ocjena=" + ocjena +
                ", razlogIzmjene='" + razlogIzmjene + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Upis)) return false;
        Upis upis = (Upis) o;
        return id == upis.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
