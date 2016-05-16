/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magicsquare;


import java.awt.GridLayout;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


/**
 *
 * @author stimpy
 */
public class MagicSquare extends JFrame {

    public MagicSquare(int order) {
        JFrame frame = new JFrame("Magic Square");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(1, 1, 0, 0));
        frame.add(new MagicSquarePanel(order));
        frame.pack();
        frame.setVisible(true);
    }

    
    /**
     * @param args the command line arguments
     */
    @SuppressWarnings("Convert2Lambda")
    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MagicSquare magicSquare = new MagicSquare(15);
                
                System.out.println("Done swing");
            }
        });
        System.out.println("Done main");
    }
    
}
