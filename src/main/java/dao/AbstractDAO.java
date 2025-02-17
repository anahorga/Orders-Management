package dao;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import connection.ConnectionFactory;

import javax.swing.table.DefaultTableModel;

/**
 * Acesta clasa este extinsa de toate celelalte clase din pachet deoarece foloseste
 * Reflexion si elimina scrierea aceluiasi codului de mai multe ori.
 * @param <T>
 */
public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    }

    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
    }



    private String createSelectAllQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        return sb.toString();
    }

    /**
     * Aceasta metoda returneaza o lista de obiecte din baza de date
     * @return
     */
    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectAllQuery();


        List<T> list = new ArrayList<T>();
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ctor.setAccessible(true);
                T instance = (T)ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Aceasta metoda gaseste un anumit obiect din baza de date cu id ul cerut
     * @param id dupa acesta se afce cautarea in baza de date
     * @return un obiect cu acel id
     */
    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            return createObjects(resultSet).get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            while (resultSet.next()) {
                ctor.setAccessible(true);
                T instance = (T)ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Acesta metoda genereaza folosind Reflexion query-ul specific pentru o inserare intr-un
     * anumit table din baza de date
     * @param entity numele tabelului unde se va face insearrea
     * @param fields Field urile din tabel care vor fi completate
     * @return
     */
     private String createInsertQuery(Object entity,Field[] fields) {

         Class<?> clazz = entity.getClass();

         String tableName = clazz.getSimpleName();
            StringBuilder columns = new StringBuilder();
         StringBuilder values = new StringBuilder();
         for (Field field : fields) {
             field.setAccessible(true); // Asigură accesul la câmpuri private
             String fieldName = field.getName();
             Object value;
             try {
                 value = field.get(entity);
             } catch (IllegalAccessException e) {
                 // Tratează eroarea de acces ilegal
                 continue;
             }
             if (value != null) {
                 if (columns.length() > 0) {
                     columns.append(", ");
                     values.append(", ");
                 }
                 columns.append(fieldName);
                 values.append("?");
             }
         }

         String sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + values + ")";
        return sql;
    }

    /**
     * Acesta metoda utilizeaza Reflexion. Astfel, ea va fi folosita si pentru inserarea
     * unui produs sau client sau comanda.
     * @param entity obiectul creat va fi inserat in baza de date
     */
    public void insert(Object entity) {
        // TODO:

        Class<?> clazz = entity.getClass();

        Field[] fields = clazz.getDeclaredFields();
        String sql = createInsertQuery(entity,fields);
        try {

            Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);

            int parameterIndex = 1;
            for (Field field : fields) {
                field.setAccessible(true); // Asigură accesul la câmpuri private
                Object value = field.get(entity);
                if (value != null) {
                    statement.setObject(parameterIndex++, value);
                }
            }

            statement.executeUpdate();

        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace(); // Tratează excepțiile adecvat
        }
    }

    private String createUpdateQuery(Object entity,Field[] fields)
    {
        Class<?> clazz = entity.getClass();
        String tableName = clazz.getSimpleName(); // Presupunând că numele clasei corespunde numelui tabelei

        StringBuilder setClause = new StringBuilder();

        for (Field field : fields) {
            field.setAccessible(true); // Asigură accesul la câmpuri private
            String fieldName = field.getName();
            Object value;
            try {
                value = field.get(entity);
            } catch (IllegalAccessException e) {
                // Tratează eroarea de acces ilegal
                continue;
            }
            if (value != null && !fieldName.equals("id")) { // Evităm actualizarea ID-ului
                if (setClause.length() > 0) {
                    setClause.append(", ");
                }
                setClause.append(fieldName).append(" = ?");
            }
        }

        String sql = "UPDATE " + tableName + " SET " + setClause + " WHERE id = ?";
        return sql;
    }
    public void update(Object entity) {
        // TODO:

        Class<?> clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        String sql=createUpdateQuery(entity,fields);
        try
        {
            Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);

            int parameterIndex = 1;
            for (Field field : fields) {
                field.setAccessible(true); // Asigură accesul la câmpuri private
                Object value = field.get(entity);
                if (value != null && !field.getName().equals("id")) { // Evităm actualizarea ID-ului
                    statement.setObject(parameterIndex++, value);
                }
            }
            // Setăm valoarea pentru ID
            statement.setObject(parameterIndex, fields[0].get(entity));

            statement.executeUpdate();

        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace(); // Tratează excepțiile adecvat
        }

    }

    private String createDeleteQuery(String field)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
    }
    public void delete(int id)
    {
        Connection connection = null;
        PreparedStatement statement = null;

        String query = createDeleteQuery("id");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
             statement.executeUpdate();


        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {

            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

    }

    /**
     * Aceasta functie contruieste header ul unui tabel si il populeaza cu valorile din
     * lista.
     * @param objects Aceste obiecte vor fi puse sub forma de tabel si afisate
     * @return
     * @param <T>
     */
    public static <T> DefaultTableModel buildTableModel(List<T> objects) {

        if (objects.isEmpty()) {
            return new DefaultTableModel(); // Return an empty table model if the list is empty
        }

        // Get the class of the first object in the list
        Class<?> clazz = objects.get(0).getClass();

        // Get all the fields of the class
        Field[] fields = clazz.getDeclaredFields();

        // Create a table model with column names
        DefaultTableModel model = new DefaultTableModel();

        // Add column names to the table model
        for (Field field : fields) {
            model.addColumn(field.getName());
        }

        // Add a row for the header


        // Add rows for the data
        for (T obj : objects) {
            Object[] rowData = new Object[fields.length];
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true); // Ensure we can access private fields
                try {
                    rowData[i] = fields[i].get(obj); // Get the value of the field from the object
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            model.addRow(rowData);
        }

        return model;
    }
}
