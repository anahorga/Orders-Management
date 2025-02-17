package bll;


import dao.ComandaDAO;

import model.Comanda;
import model.Produs;

import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.NoSuchElementException;

public class ComandaBLL {
    private ComandaDAO comandaDAO;



    public ComandaBLL() {

        comandaDAO = new ComandaDAO();

    }

    public Comanda findComandaById(int id) {
        Comanda st = comandaDAO.findById(id);
        if (st == null) {
            throw new NoSuchElementException("The client with id =" + id + " was not found!");
        }
        return st;
    }
    public List<Comanda> toateComenzile()
    {
        List<Comanda> comenzi=comandaDAO.findAll();
        return comenzi;
    }
    public int inserareComanda(Comanda cl)//insearareComandaSimpla
    {
        ProdusBLL produs=new ProdusBLL();

        Produs produsulMeu=null;
       produsulMeu= produs.findProdusById(cl.getIdProdus());

         if(produsulMeu.getStoc()<cl.getCantitate())
             return 0;
             //System.out.println("Stocul Produsului nu este suficient pt a face comanda");
             else {
             comandaDAO.insert(cl);
              return 1;
         }
        //System.out.println("Inserare cu succes");
    }
    public DefaultTableModel tabelComenzi()
    {
        DefaultTableModel tableModel = comandaDAO.buildTableModel();

        // Example usage: You can use this table model to create a JTable or display data in any other way
        return tableModel;
    }

    /**
     * Acesta metoda foloseste un obiect de tipul DAO pentru a inseara in baza de date
     * atat o comanda cat si o factura(obiect imutabil generat automat) daca este posibil.
     * (stocul produsului trebuie sa fie mai mare sau egal decat cantitatea dorita in comanda)
     * @param cl comanda de inseart
     * @return
     */
    public int insertComanda(Comanda cl)//inserareComandaCuFacutra
    {
        ProdusBLL produs=new ProdusBLL();

        Produs produsulMeu=null;
        produsulMeu= produs.findProdusById(cl.getIdProdus());

        if(produsulMeu.getStoc()<cl.getCantitate())
            return 0;
            //System.out.println("Stocul Produsului nu este suficient pt a face comanda");
        else {
            comandaDAO.insertComanda(cl);
            return 1;
        }
        //System.out.println("Inserare cu succes");
    }
}
