package interfaces;

import domain.Adres;
import domain.Reiziger;

import java.util.ArrayList;
import java.util.List;

public interface AdresDAOInterface {
    boolean save(Adres adres);
    boolean update(Adres adres);
    boolean delete(Adres adres);

    Adres findById(int id);
    List<Adres> findAll();
}
