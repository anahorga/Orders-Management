package start;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import bll.BillBLL;
import bll.ClientBLL;
import bll.ComandaBLL;
import bll.ProdusBLL;
import model.Client;
import model.Comanda;
import model.Produs;
import prezentare.Interfata;


import static start.Reflection.retrieveProperties;


/**
 * @author Horga Ana, studenta la Facultatea de Automatica si Calculatoare
 * @version 1.0
 * @since 2024-05-11
 *
 * Aceasta aplicatie doreste comunicarea cu o baza de date a unui depozit in care sunt produse.
 * Clientii pot face comenzi, iar factura va fi generata automat. Sunt implementate metode
 * CRUD pentru a accesa baza de date.
 *
 * Layered Architecture: sunt implementate 6 pachete: connection, dao, bll, prezentare, model si
 * start(main-ul). Fiecare comunica cu pachetul model, dar de exemplu pachetul prezentare
 * comunica cu pachetul dao prin intermediul pachetului bll. Astfel, rolul fiecarui pachet
 * este stabilit clar, iar codul este simplu de urmarit, de exemplu in casa dao se realizeaza
 * doar metode specifice accesului la baza de date.
 */


public class Start {


    protected static final Logger LOGGER = Logger.getLogger(Start.class.getName());

    public static void main(String[] args) throws SQLException {


         Interfata view=new Interfata();
    }

}
