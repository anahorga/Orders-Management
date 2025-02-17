package bll;


import dao.ProdusDAO;

import model.Comanda;
import model.Produs;

import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * In acest pachet este implementata logica aplicatiei, se face legatura cu baza de date
 * prin apelul metodelor specifice, implementate in pachetul DAO
 */
public class ProdusBLL {
    private ProdusDAO produsDAO;



    public ProdusBLL() {

        produsDAO = new ProdusDAO();

    }

    public Produs findProdusById(int id) {
        Produs st = produsDAO.findById(id);
        if (st == null) {
            throw new NoSuchElementException("The client with id =" + id + " was not found!");
        }
        return st;
    }
    public List<Produs> toateProdusele()
    {
        List<Produs> clienti=produsDAO.findAll();
        return clienti;
    }
    public void inserareProdus(Produs cl)
    {

        produsDAO.insert(cl);
        //System.out.println("Inserare cu succes");
    }
    public void updateProdus(Produs schimb)
    {

        produsDAO.update(schimb);
    }
    public void deleteProdus(int id)
    {
        produsDAO.delete(id);
    }
    public DefaultTableModel tabelProduse(List<Produs> produse)
    {
        DefaultTableModel tableModel = produsDAO.buildTableModel(produse);

        // Example usage: You can use this table model to create a JTable or display data in any other way
        return tableModel;
    }
}
