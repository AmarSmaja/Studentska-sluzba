package repo;

import domain.Predmet;

import java.util.List;
import java.util.Optional;

public interface PredmetRepository {
    void save(Predmet predmet);

    void update(Predmet predmet);

    void delete(String sifraPredmeta);

    Optional<Predmet> findById(String sifraPredmeta);

    Optional<Predmet> findBySifra(String sifra);

    List<Predmet> findAll();

    List<Predmet> findByNazivPrefix(String nazivPrefix);
}
