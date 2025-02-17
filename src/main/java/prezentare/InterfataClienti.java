package prezentare;

import bll.ClientBLL;
import model.Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class InterfataClienti extends JFrame {

    private JPanel Panel2;
    private JButton FINDButton;
    private JComboBox idDeGasit;
    private JTextField idClient;
    private JTextField numeTextField;
    private JTextField adresaTextField;
    private JButton insertClientButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JTable table1;

    private ClientBLL clientBll;

    public InterfataClienti()
    {
        clientBll=new ClientBLL();
        List<Client> clienti=clientBll.totiClienti();
        table1.setModel(clientBll.tabelClienti(clienti));
        setContentPane(Panel2);
        setSize(new Dimension(800,400));
        setLocationRelativeTo(null);
        setVisible(true);

        for(Client c:clienti)
        {
            idDeGasit.addItem(c.getId());
        }
        insertClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String userInput6="";
                String userInput7="";


                userInput6=numeTextField.getText();
                userInput7=adresaTextField.getText();

                try {
                    Client client = new Client(userInput6, userInput7);
                    clientBll.inserareClient(client);
                    showError("Client inserat cu succes");
                }catch(Exception ex)
                {
                    showError("Clientul nu poate fii inserat");
                }
                finally {
                    List<Client> clienti=clientBll.totiClienti();
                    table1.setModel(clientBll.tabelClienti(clienti));
                    numeTextField.setText("");
                    adresaTextField.setText("");
                    idDeGasit.addItem(clienti.getLast().getId());
                }
            }
        });

        FINDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int idclient=(int)idDeGasit.getSelectedItem();
                Client cl=clientBll.findClientById(idclient);

                idClient.setText(idDeGasit.getSelectedItem().toString());
                numeTextField.setText(cl.getNume());
                adresaTextField.setText(cl.getAdresa());
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String idclient=idClient.getText();
                try {

                    clientBll.deleteClient(Integer.parseInt(idclient));
                    showError("Client sters cu succes");
                }catch(Exception ex)
                {
                    showError("Clientul nu poate fii sters");
                }
                finally {
                    List<Client> clienti=clientBll.totiClienti();
                    table1.setModel(clientBll.tabelClienti(clienti));
                    numeTextField.setText("");
                    adresaTextField.setText("");
                    idClient.setText("");
                    idDeGasit.removeItem(Integer.parseInt(idclient));
                }


            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 try{

                     int idclient=Integer.parseInt(idClient.getText());
                     String userInput1="";
                     String userInput2="";

                     userInput1=numeTextField.getText();
                     userInput2=adresaTextField.getText();
                     Client cl=new Client(idclient,userInput1,userInput2);

                     clientBll.updateClient(cl);
                     showError("Clientul updatat cu succes");

                 } catch(Exception ex)
                 {
                     showError("Clientul nu poate fii updatat");
                 }
                 finally {
                     List<Client> clienti=clientBll.totiClienti();
                     table1.setModel(clientBll.tabelClienti(clienti));
                     numeTextField.setText("");
                     adresaTextField.setText("");
                     idClient.setText("");
                 }

            }
        });

    }
    public  void showError(String errMessage) {
        JOptionPane.showMessageDialog(this, errMessage);
    }
}
