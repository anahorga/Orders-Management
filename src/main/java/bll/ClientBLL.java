package bll;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


import dao.ClientDAO;
import model.Client;
import model.Produs;

import javax.swing.table.DefaultTableModel;


public class ClientBLL {


    private ClientDAO clientDAO;



    public ClientBLL() {

        clientDAO = new ClientDAO();

    }

    public Client findClientById(int id) {
        Client st = clientDAO.findById(id);
        if (st == null) {
            throw new NoSuchElementException("The client with id =" + id + " was not found!");
        }
        return st;
    }
    public List<Client> totiClienti()
    {
        List<Client> clienti=clientDAO.findAll();
        return clienti;
    }
    public void inserareClient(Client cl)
    {

        clientDAO.insert(cl);
        //System.out.println("Inserare cu succes");
    }
   public void updateClient(Client schimb)
   {

      clientDAO.update(schimb);
   }
   public void deleteClient(int id)
   {
       clientDAO.delete(id);
   }
    public DefaultTableModel tabelClienti(List<Client> clienti)
    {
        DefaultTableModel tableModel = clientDAO.buildTableModel(clienti);

        // Example usage: You can use this table model to create a JTable or display data in any other way
        return tableModel;
    }
}
