package prezentare;

import bll.ClientBLL;
import bll.ComandaBLL;
import bll.ProdusBLL;
import model.Client;
import model.Comanda;
import model.Produs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class InterfataComenzi extends JFrame{
    private JPanel Panel4;
    private JTextField idComanda;
    private JComboBox idProdus;
    private JComboBox idClient;
    private JSpinner cantitate;
    private JButton adaugaComandaButton;
    private JTable table1;
    private JTable table2;
    private JTable table3;
    private ComandaBLL comandaBll;
    private ProdusBLL produsBll;
    private ClientBLL clientBll;

    public InterfataComenzi()
    {
        setContentPane(Panel4);
        comandaBll=new ComandaBLL();
        table1.setModel(comandaBll.tabelComenzi());
        setSize(new Dimension(800,400));
        setLocationRelativeTo(null);
        setVisible(true);

         produsBll=new ProdusBLL();
        List<Produs> produse=produsBll.toateProdusele();
        table2.setModel(produsBll.tabelProduse(produse));
        clientBll=new ClientBLL();
        List<Client> clienti=clientBll.totiClienti();
        table3.setModel(clientBll.tabelClienti(clienti));
        for(Produs p:produse)
        {
            idProdus.addItem(p.getId());
        }
        for(Client c:clienti)
        {
            idClient.addItem(c.getId());
        }

        adaugaComandaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try{
                    int idprodus=(int)idProdus.getSelectedItem();
                    int idclient=(int)idClient.getSelectedItem();
                    int cantitateProdus=(int)cantitate.getValue();

                    Comanda com=new Comanda(idprodus,idclient,cantitateProdus);

                    if(comandaBll.insertComanda(com)==0)
                    {
                        showError("Stocul produsului dorit nu este suficient pt a face comanda");
                    }
                    else{
                        showError("Comanda realizata cu succes");
                    }

                }catch(Exception ex)
                {
                    showError("Comanda nu poate fi inserata");
                }
                finally{
                    table1.setModel(comandaBll.tabelComenzi());

                    List<Produs> produse=produsBll.toateProdusele();
                    table2.setModel(produsBll.tabelProduse(produse));

                    List<Client> clienti=clientBll.totiClienti();
                    table3.setModel(clientBll.tabelClienti(clienti));
                }
            }
        });
    }
    public  void showError(String errMessage) {
        JOptionPane.showMessageDialog(this, errMessage);
    }
}
