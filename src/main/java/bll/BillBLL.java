package bll;


import dao.BillDAO;


import dao.ComandaDAO;
import dao.ProdusDAO;
import model.Bill;
import model.Comanda;
import model.Produs;


import javax.swing.table.DefaultTableModel;
import java.time.LocalDateTime;
import java.util.List;

public class BillBLL {
    private BillDAO billDAO;



    public BillBLL() {

        billDAO = new BillDAO();

    }


    public List<Bill> toateFacturile()
    {
        return billDAO.findAll();

    }

    public DefaultTableModel tabelFacturi()
    {
        DefaultTableModel tableModel = billDAO.buildTableModel();

        // Example usage: You can use this table model to create a JTable or display data in any other way
        return tableModel;
    }
}
