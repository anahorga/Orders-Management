package dao;

import connection.ConnectionFactory;
import model.Comanda;
import model.Produs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;

public class ComandaDAO extends AbstractDAO<Comanda>{

    public ComandaDAO()
    {
        super();
    }

    /**
     * Am folosit aceasta metoda pentru afisare mai frumoasa a comenzilor
     * @return
     */
    public static DefaultTableModel buildTableModel()  {

        String query = "select comanda.id,client.nume as \"Client\",produs.denumire as \"Produs\",comanda.cantitate\n" +
                "from client , produs,comanda\n" +
                "where comanda.idClient=client.id and comanda.idProdus=produs.id ";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            resultSet = statement.executeQuery();

            ResultSetMetaData metaData = resultSet.getMetaData();
            Vector<String> user = new Vector<>();
            int columnCount = metaData.getColumnCount();

                user.add("idComanda");
                user.add("Client");
                user.add("Produs");
                user.add("Cantitate");

            Vector<Vector<Object>> data = new Vector<>();
           /* // Header ul
            Vector<Object> header = new Vector<>();
            header.add(0,"idComanda");
            header.add(1,"Client");
            header.add(2,"Produs");
            header.add(3,"Cantitate");
            data.add(header);*/

            // Datele din ResultSet
            while (resultSet.next()) {
                Vector<Object> vector = new Vector<>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    vector.add(resultSet.getObject(columnIndex));
                }
                data.add(vector);
            }

            return new DefaultTableModel(data, user);

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING,  "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }


        return  null;
    }

    /**
     * Am folosit aceasta metoda pentru a putea genera automat o factura la crearea unei noi comenzi.
     * @param comanda
     */
    public void insertComanda(Comanda comanda) {
        String insertComandaSQL = "INSERT INTO comanda (idClient, idProdus,cantitate) VALUES (?, ?,?)";
        String insertBillSQL = "INSERT INTO bill (idComanda, data, pretTotal) VALUES (?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmtComanda = null;
        PreparedStatement pstmtBill = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false); // Activăm modul de tranzacție

            // Inserăm comanda
            pstmtComanda = conn.prepareStatement(insertComandaSQL, Statement.RETURN_GENERATED_KEYS);
            pstmtComanda.setInt(1, comanda.getIdClient());
            pstmtComanda.setInt(2, comanda.getIdProdus());
            pstmtComanda.setInt(3, comanda.getCantitate());
            int affectedRows = pstmtComanda.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Inserarea comenzii a eșuat, nicio înregistrare modificată.");
            }

            // Obținem id-ul comenzii generate
            int orderId;
            try (var generatedKeys = pstmtComanda.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    orderId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Obținerea id-ului comenzii a eșuat, niciun id generat.");
                }
            }
            int total=0;
            int idComanda=comanda.getId();
            int idProdus=comanda.getIdProdus();
            ProdusDAO pro=new ProdusDAO();
            Produs p=pro.findById(idProdus);
             total=comanda.getCantitate()*p.getPretPeBucata();


            // Inserăm factura asociată comenzii
            pstmtBill = conn.prepareStatement(insertBillSQL);
            pstmtBill.setInt(1, orderId); // orderId este cheia străină în tabela Log
            pstmtBill.setInt(3, total); // Presupunând că totalul comenzii este același cu totalul facturii
            pstmtBill.setDate(2, java.sql.Date.valueOf(java.time.LocalDate.now())); // Data curentă

            pstmtBill.executeUpdate();

            conn.commit(); // Efectuăm tranzacția

        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback(); // Facem rollback la tranzacție în caz de eroare
                } catch(SQLException exRollback) {
                    exRollback.printStackTrace();
                }
            }
            ex.printStackTrace();
        } finally {
            // Închidem resursele
            if (pstmtComanda != null) {
                try {
                    pstmtComanda.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (pstmtBill != null) {
                try {
                    pstmtBill.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Revenim la modul de auto-commit
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

}
