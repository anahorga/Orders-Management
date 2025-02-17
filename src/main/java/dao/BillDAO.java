package dao;

import connection.ConnectionFactory;
import model.Bill;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;

public class BillDAO extends AbstractDAO<Bill>{

    public BillDAO()
    {
        super();
    }

    /**
     * Am folosit acesta metodda pentru afisarea mai frumoasa a facturilor.
     * @return
     */
    public static DefaultTableModel buildTableModel()  {

        String query = "select bill.id as \"idFactura\",comanda.id as \"idComanda\",client.nume as \"Client\",produs.denumire as \"Produs\",PretTotal,data\n" +
                "from client , produs,comanda,bill\n" +
                "where comanda.idClient=client.id and comanda.idProdus=produs.id and comanda.id=bill.idComanda ";
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

                user.add("idFactura");
                user.add("idComanda");
                user.add("Client");
                user.add("Produs");
                user.add("PretTotal");
                user.add("Data");





            Vector<Vector<Object>> data = new Vector<>();
           /* // Header ul
            Vector<Object> header = new Vector<>();
            header.add(0,"idFactura");
            header.add(1,"idComanda");
            header.add(2,"Client");
            header.add(3,"Produs");
            header.add(4,"PretTotal");
            header.add(5,"data");
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

}
