package client.GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class TestTable {
    public TestTable(Integer[][] matrix) {
        JFrame f = new JFrame("JTable Sample");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container content = f.getContentPane();
        Object rows[][] = matrix;
        Object columns[] = {"1", "2","3","4","5","6","7"};
        JTable table = new JTable(rows, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        content.add(scrollPane, BorderLayout.CENTER);
        f.setSize(800, 800);
        f.setVisible(true);
    }
}
