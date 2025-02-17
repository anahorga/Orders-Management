package prezentare;

import bll.BillBLL;

import javax.swing.*;
import java.awt.*;

public class InterfataFacturi extends JFrame{
    private JPanel Panel5;
    private JTable table1;
    private BillBLL billBll;

    public InterfataFacturi()
    {
        setContentPane(Panel5);
        billBll=new BillBLL();
        table1.setModel(billBll.tabelFacturi());

        setSize(new Dimension(600,400));
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
