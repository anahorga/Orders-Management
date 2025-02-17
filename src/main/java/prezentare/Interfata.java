package prezentare;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Acetast pachet reprezinta interfata grafica care va comunica cu utilizatorul. Astfel acesta nu va fi nevoit sa aiba cunostiine in domeniul IT pentru a utiliza aplicatia.
 */
public class Interfata extends JFrame{
    private JButton clientiButton;
    private JButton produseButton;
    private JButton comenziButton;
    private JButton facturiButton;
    private JPanel Panel1;

    public Interfata()
    {
        setContentPane(Panel1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(new Dimension(300,300));
        setLocationRelativeTo(null);
        setVisible(true);

        clientiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InterfataClienti view=new InterfataClienti();
            }
        });

        produseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InterfataProduse view=new InterfataProduse();
            }
        });

        comenziButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InterfataComenzi view=new InterfataComenzi();
            }
        });

        facturiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InterfataFacturi view=new InterfataFacturi();
            }
        });
    }
}
