# Studentska služba - Java desktop i konzolna aplikacija

---

## 1. Pokretanje aplikacije

### 1.0 Preduvjeti

- Instaliran **JDK 17 +**
- JDBC driver za SQLite:
  - File -> Project Structure -> Modules -> Dependencies -> + -> JAR or Directories... -> lib/sqlite-jdbc jar i potvrdi.
- Inicijalizovanje baze podataka:
  - Unutar src/persistance pronaci i pokrenuti `DatabaseInitializer` klasu kako bi se napravila baza:
    - Ukoliko se u konzoli ispise "Sve tabele su spremne.", onda je aplikacija spremna za upotrebu.

### 1.1 Pokretanje GUI aplikacije

Glavna ulazna tačka aplikacije za GUI je klasa **Main**

    public class Main {
    public static void main(String[] args) {
        AppConfig config = new AppConfig();

        SwingUtilities.invokeLater(() -> {
            LoginFrame frame = new LoginFrame(config);
            frame.setVisible(true);
        });
        }
    }

* U IDE-u desni klik na **Main** -> **Run 'Main.main()'.**
* Otvori se **Login** prozor.

### 1.2 Pokretanje konzolne aplikacije

Konzolni interfejs radi preko klase **ConsoleApp.**

    public class ConsoleMain {
    public static void main(String[] args) {
        AppConfig config = new AppConfig();
        ConsoleApp app = new ConsoleApp(config);
        app.start();
        }
    }

* Desni klik na **ConsoleMain** -> **Run 'ConsoleMain.main()'.**
* U terminalu se otvara glavni meni.

---

## 2. Kontrole

### 2.1 Kontrole za Swing GUI

- Dugmad:
    - `Referent` – loguje se referent.
    - `Student` – loguje se student, mora postojati u bazi.
- Polje `Broj indeksa (za studenta)`:
    - Validno samo ako je izabrana uloga ***Student***.
    - Ako indeks ne postoji, javlja se greška.
- Dugmad:
    - `Login` – potvrđuje login.
    - `Izlaz` – zatvara aplikaciju.

### 2.1.1 Prozor referenta (`ReferentMainFrame`):

Unutar `ReferentMainFrame` se nalaze tabovi:

- Studenti
  - Lista svih studenata,
  - Forma:
    - Broj indeksa,
    - Ime,
    - Prezime,
    - Studijski program, i
    - Godina upisa.
  - Dugmad:
    - `Osvjezi listu` - ponovo ucitava sve studente,
    - `Dodaj studenta` - kreira novog studenta,
    - `Obrisi studenta` - brise studenta po broju indeksa (ako nema upisa), i
    - `Pretrazi po prezimenu` - pretraga po prefixu prezimena.
- Predmeti
  - Lista svih predmeta,
  - Forma:
    - Sifra predmeta,
    - Naziv
    - ECTS, i
    - Semestar.
  - Dugmad:
    - `Osvjezi listu` - osvjezava listu predmeta,
    - `Dodaj predmet` - kreira novi predmet.
- Upisi i ocjene
  - Lista svih upisa za studenta,
  - Forma:
    - Broj indeksa,
    - Sifra predmeta,
    - Akadamska godina,
    - ID Upisa, 
    - Ocjene, i
    - Razlog izmjene.
  - Dugmad:
    - `Upisi predmet` - kreira novi upis,
    - `Prikazi upise studenata` - prikazuje sve upise za indeks,
    - `Unesi ocjenu` - postavlja ocjenu za zadani ID upisa (ako jos nema ocjene),
    - `Promijeni ocjenu` - mijenja postojecu ocjenu uz obavezan razlog, i 
    - `Ponisti upis` - brise upis.
- Ocjene
  - Lista svih upisa studenata,
  - Forma:
    - Broj indeksa, 
    - ID upisa, 
    - Ocjena, i
    - Razlog promjene.
  - Dugmad:
    - `Prikazi upise studenata`,
    - `Unesi ocjenu`, i
    - `Promijeni ocjenu`.
- Karton
  - Tekstualni prikaz kartona,
  - Broj indeksa, 
  - Dugme `Prikazi karton`,
  - Labela `Ukupno polozenih ECTS`.

### 2.1.2 Prozor studenta (`StudentMainFrame`):

- Otvara se nakon ispravnog logina studenta.
- Prikazuje listu svih upisa i ocjena tog studenta.
- Samo ***read-only*** pregled, bez mogucnosti izmjena.

### 2.2 Kontrole za konzolnu aplikaciju

### 2.2.1 Glavni meni

Glavni meni se sastoji od tri opcije:

- Unosom `1`, otvara se `referentMenu()`.
- Unosom `2`, otvara se `studentMenu()`.
- Unosom `0`, konzolni program se zatvara i ispiuje poruku.

### 2.2.2 Referent meni

Referent meni se sastoji od cetiri opcije:

- Unosom `1`, otvara se `studentSubMenu()`.
- Unosom `2`, otvara se `predmetSubMenu()`.
- Unosom `3`, otvara se `upisSubMenu()`.
- Unosom `0`, program se zatvara.

### 2.2.2.1 `studentSubMenu()` prozor

Studentski podmeni ima opcije:

- Unosom `1`, prikazuju se svi studenti preko `prikaziSveStudente()` metode.
- Unosom `2`, dodaje se novi student preko `dodajStudenta()` metode.
- Unosom `3`, azurira se student preko `azurirajStudenta()` metode.
- Unosom `4`, student se brise preko `obirisStudneta()` metode.
- Unosom `5`, vrsi se pretraga studenta preko `pretragaStudenataPoPrezimenu()` metode.
- Unosom `0`, program se zatvara.

### 2.2.2.2 `predmetSubMenu()` prozor

Predmetni podmeni ima opcije:

- Unosom `1`, prikazuju se svi predmeti preko `prikaziSvePredmete()` metode.
- Unosom `2`, dodaje se novi predmetp preko `dodajPredmet()` metode.
- Unosom `3`, azurira se predmet preko `azurirajPredmet()` metode.
- Unosom `4`, brise se predmet preko `obirisPredmet()` metode.
- Unosom `0`, program se zatvara.

### 2.2.2.3 `upisSubMenu()` prozor

Upisni podmeni ima opcije:

- Unosom `1`, student se upisuje na predmet preko `upisiPredmetStudentu()` metode.
- Unosom `2`, ponistava se upis preko `ponisiUpis()` metode.
- Unosom `3`, unosi se ocjena preko `unesiOcjenu` metode.
- Unosom `4`, mijenja se ocjena preko `promijeniOcjenu()` metode.
- Unosom `5`, prikazuje sve upise studenta preko `prikaziUpiseStudenta()` metode.
- Unosom `0`, program se zatvara.

### 2.2.3 Student meni

Nakon unosa indeksa i validacije, moze pregledati svoje ocjene.

- Unosom `1`, pravi se lista upisa, koji koriste `upisService` servis da prikazu upise.
- Unosom `0`, program se zatvara.

## 3. Opcije

### 3.1 Studenti opcije

- Broj indeksa je obavezan i jedinstven.
- Godina upisa validna samo u rasponu od 2020.-2050.
- Brisanje studenta nije dozvoljeno ako student ima evidentirane upise/ocjene.

### 3.2 Predmeti opcije

- Sifra predmeta i naziv su obavezni.
- ECTS u rasponu od 1 - 15.
- Semestar u rasponu od 1 - 10.
- Nije moguce kreirati predmet sa vec postojecom sifrom.
- Brisanje predmeta nije dozvoljeno ako postoje upisi za taj predmet.

### 3.3 Upisi i ocjene opcije

- Prije upisa se provjerava:
  - da student postoji,
  - da predmet postoji, i
  - da vec ne postoji upis istog studenta na isti predmet u istoj godini.
- Ocjene:
  - raspon od 5 - 10