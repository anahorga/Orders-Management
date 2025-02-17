package prezentare;

import bll.ClientBLL;
import bll.ProdusBLL;
import model.Client;
import model.Produs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class InterfataProduse extends JFrame{
    private JPanel Panel3;
    private JButton insertButton;
    private JComboBox idDeGasit;
    private JButton findButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JTextField idProdus;
    private JTextField denumireProdus;
    private JTextField stocProdus;
    private JTextField pretProdus;
    private JTable table1;
    private ProdusBLL produsBll;

    public InterfataProduse()
    {
        setContentPane(Panel3);

        produsBll=new ProdusBLL();
        List<Produs> produse=produsBll.toateProdusele();
        table1.setModel(produsBll.tabelProduse(produse));
        setSize(new Dimension(800,400));
        setLocationRelativeTo(null);
        setVisible(true);

        for(Produs p:produse)
        {
            idDeGasit.addItem(p.getId());
        }

        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String userInput6="";
                String userInput7="";
                String userInput8="";


                userInput6=denumireProdus.getText();
                userInput7=stocProdus.getText();
                userInput8=pretProdus.getText();

                try {
                    Produs pr=new Produs(Integer.parseInt(userInput7),Integer.parseInt(userInput8),userInput6);
                    produsBll.inserareProdus(pr);
                    showError("Produs inserat cu succes");
                }catch(Exception ex)
                {
                    showError("Produsul nu poate fii inserat");
                }
                finally {
                    List<Produs> produse=produsBll.toateProdusele();
                    table1.setModel(produsBll.tabelProduse(produse));
                    denumireProdus.setText("");
                    stocProdus.setText("");
                    pretProdus.setText("");
                    idDeGasit.addItem(produse.getLast().getId());

                }
            }
        });

        findButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int idprodus=(int)idDeGasit.getSelectedItem();
                Produs p=produsBll.findProdusById(idprodus);

                idProdus.setText(idDeGasit.getSelectedItem().toString());
                denumireProdus.setText(p.getDenumire());
                stocProdus.setText(String.valueOf(p.getStoc()));
                pretProdus.setText(String.valueOf(p.getPretPeBucata()));
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String idprodus=idProdus.getText();
                try {

                    produsBll.deleteProdus(Integer.parseInt(idprodus));
                    showError("Produs sters cu succes");
                }catch(Exception ex)
                {
                    showError("Produsul nu poate fii sters");
                }
                finally {
                    List<Produs> produse=produsBll.toateProdusele();
                    table1.setModel(produsBll.tabelProduse(produse));
                    denumireProdus.setText("");
                    stocProdus.setText("");
                    pretProdus.setText("");
                    idProdus.setText("");
                    idDeGasit.removeItem(Integer.parseInt(idprodus));
                }


            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{

                    int idprodus=Integer.parseInt(idProdus.getText());
                    String userInput1="";
                    String userInput2="";
                    String userInput3="";

                    userInput1=denumireProdus.getText();
                    userInput2=stocProdus.getText();
                    userInput3=pretProdus.getText();
                    Produs p=new Produs(idprodus,Integer.parseInt(userInput2),Integer.parseInt(userInput3),userInput1);

                    produsBll.updateProdus(p);
                    showError("Produs updatat cu succes");

                } catch(Exception ex)
                {
                    showError("Produsul nu poate fii updatat");
                }
                finally {
                    List<Produs> produse=produsBll.toateProdusele();
                    table1.setModel(produsBll.tabelProduse(produse));
                    denumireProdus.setText("");
                    stocProdus.setText("");
                    pretProdus.setText("");
                    idProdus.setText("");
                }

            }
        });

    }
    public  void showError(String errMessage) {
        JOptionPane.showMessageDialog(this, errMessage);
    }
}
