/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magicsquare;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayDeque;
import java.util.Random;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;


/**
 *
 * @author stimpy
 */
public class MagicSquarePanel extends JPanel implements MouseListener, ActionListener {

    private final int CELL_WIDTH = 60;
    
    private final ArrayDeque<Step> animator;
    private final int[][] values;
    private final JLabel[][] labels;
    private final int order;

    Timer timer;
    
    /**
     * Constructor.
     * @param order numbers per each length of the square
     */
    
    public MagicSquarePanel(int order) {
        this.order = order;
        int n = order + 2;      // top, bottom, left, right margins
        values = new int[n][n];
        labels = new JLabel[n][n];
        animator = new ArrayDeque<>();

        timer = new Timer(100, this);
        
        setPreferredSize(new Dimension(n * CELL_WIDTH, n * CELL_WIDTH));
        setLayout(new GridLayout(n, n, 1, 1));

        // create and add labels to panel and to array
        for (int j = 0; j < n; ++j) {
            for (int i = 0; i < n; ++i) {
                JLabel label = new JLabel();
                labels[i][j] = label;
                add(label);
            }
        }

        valuesClear();
        labelsClear();

        addMouseListener(this);


        squareCalculate();
        squareDisplay();
        // animate(110);
    }


    /**
     * Reset all cell values to 0.
     */
    private void valuesClear() {
        int n = order + 2;
        for (int j = 0; j < n; ++j) {
            for (int i = 0; i < n; ++i) {
                values[i][j] = 0;
            }
        }
    }
    
    /**
     * Display a String in all the sums in the square's margins.
     * @param str the String to be displayed
     */
    private void sumsClear(String str) {
        int n = order + 2;
        String strFmt = "<html><center><font size=3 color=green>%s</font></center></html>";
        
        for (int i = 0; i < n; ++i) {
            labels[i][0].setText(String.format(strFmt, str));
            labels[i][n-1].setText(String.format(strFmt, str));
        }
        for (int j = 0; j < n; ++j) {
            labels[0][j].setText(String.format(strFmt, str));
            labels[n-1][j].setText(String.format(strFmt, str));
        }
    }
    

    /**
     * Reset all the square's labels' backgrounds and fonts to some standard.
     */
    private void labelsClear() {
        for (JLabel[] row : labels) {
            for (JLabel label : row) {
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);
                label.setForeground(Color.blue);
                label.setOpaque(true);
                label.setBackground(new Color(245, 204, 176));
                label.setFont(new Font("David", Font.BOLD | Font.ITALIC, 15));
            }
        }
    }


    /**
     * Copy only the center values[][] to labels[][].
     */
    private void squareDisplay() {
        for (int j = 1; j <= order; ++j) {
            for (int i = 1; i <= order; ++i) {
                String str = String.format("<html><center><font size=1 color=black>(%d, %d)</font><br>%d</center></html>",
                        i, j, values[i][j]);
                labels[i][j].setText(str);
            }
        }
    }

    
    /**
     * Copy only the center values[][] to labels[][].
     */
    private void squareDisplayCell(int i, int j) {
        if (i < 1  ||  i > order  ||  j < 1  ||  j > order)
            throw new ArrayIndexOutOfBoundsException("Cell is not within square bounds !");

        labels[i][j].setText("" + values[i][j]);
    }

    
    /**
     * Display a String in each label of the center values.
     */
    private void squareClear(String str) {
        for (int j = 1; j <= order; ++j) {
            for (int i = 1; i <= order; ++i) {
                labels[i][j].setText(str);
            }
        }
    }

    
    /**
     * Copy only the sums in the margins values[][] to labels[][].
     */
    private void sumsDisplay() {
        int n = order + 2;
        String strFmt = "<html><center><font size=3 color=green>%d</font></center></html>";
        for (int j = 0; j < n; ++j) {       // first and last columns
            labels[0][j].setText(String.format(strFmt, values[0][j]));
            labels[order+1][j].setText(String.format(strFmt, values[order+1][j]));
        }
        for (int i = 0; i < n; ++i) {       // top and bottom rows
            labels[i][0].setText(String.format(strFmt, values[i][0]));
            labels[i][order+1].setText(String.format(strFmt, values[i][order+1]));
        }
    }
        
    
    /**
     * Display the values one by one with a pause, according to the order specified in arrays of indexes.
     * @param row ordered sequence of row indexes of the next value to be displayed
     * @param col ordered sequence of column indexes of the next value to be displayed
     */
    @SuppressWarnings("SleepWhileInLoop")
    private void animate(long l) {
        if (animator.isEmpty())
            throw new ArrayIndexOutOfBoundsException("No animation sequence !");

        sumsClear("-");
        squareClear("");
        
        timer.setDelay((int)l);
        timer.start();
    }


    /**
     * Paint and synchronize.
     * @param gr 
     */
    @Override
    protected void paintComponent(Graphics gr) {
        super.paintComponent(gr);

        Toolkit.getDefaultToolkit().sync();
    }
    
    
    /**
     * Calculate the sums of rows, columns and diagonals.
     * Demonstrating that this is a real magic square.
     */
    private void sumsCalculate() {
        int i, j;

        for (j = 1; j <= order; ++j) {      // sum rows
            int sumRow = 0;
            for (i = 1; i <= order; ++i) {
                sumRow += values[i][j];
            }
            values[0][j] = sumRow;
            values[order+1][j] = sumRow;
        }

        for (i = 1; i <= order; ++i) {      // sum columns
            int sumCol = 0;
            for (j = 1; j <= order; ++j) {
                sumCol += values[i][j];
            }
            values[i][0] = sumCol;
            values[i][order+1] = sumCol;
        }

        int sumD1 = 0, sumD2 = 0;        
        for (i = 1, j = order; i <= order  &&  j >= 1; ++i, --j) {      // sum diagonals
            sumD1 += values[i][i];
            sumD2 += values[j][j];

            values[0][0] = sumD1;
            values[order+1][order+1] = sumD1;

            values[order+1][0] = sumD2;
            values[0][order+1] = sumD2;
        }
        
    }

   
    /**
     * Fill magic square (only center, no sums).
     */
    private void squareCalculate() {
//        if (order == 3)
//            square3Calculate();
//        
        squareOddCalculate();
    }
    

    /**
     * Fill a magic square of odd order, using de la Loubère's method.
     */
    public void squareOddCalculate() {
        int i = (order / 2) + 1;
        int j = 1;
        int end = order * order;
        
        valuesClear();
        animator.clear();
        
        for (int n = 1; n <= end; ++n) {
            if (i < 1  ||  i > order  ||  j < 1  ||  j > order)
                throw new ArrayIndexOutOfBoundsException("Cell is not within square bounds !");

            if (values[i][j] > 0) {

                // take it back to previous cell
                i -= 1;
                j += 1;
                if (j > order)      j = 1;
                if (i < 1)          i = order;
                
                // go one square down
                j += 1;
                if (j > order)      j = 1;
                
                // impossible case
                if (values[i][j] > 0)
                    throw new ArrayIndexOutOfBoundsException("Cell is already full !");
            }
                
            values[i][j] = n;
            animator.push(new Step(i, j, n));

            i += 1;
            j -= 1;
            if (i > order)      i = 1;
            if (j < 1)          j = order;

        }
    }

    /**
     * Fill a magic square of 3rd order with random values, using Édouard Lucas's formula.
     */
    public void square3Calculate() {
        Random rand = new Random();
        int a, b, c, t;
        a = rand.nextInt(10) + 1;
        b = rand.nextInt(10) + a;
        if (b == (2 * a))
            b += 1;
        c = rand.nextInt(10) + b;
        
        animator.clear();
        
        animator.push(new Step(1, 1, c - b));
        animator.push(new Step(2, 1, c + (a + b)));
        animator.push(new Step(3, 1, c - a));
        
        animator.push(new Step(1, 2, c - (a - b)));
        animator.push(new Step(2, 2, c));
        animator.push(new Step(3, 2, c + (a - b)));
        
        animator.push(new Step(1, 3, c + a));
        animator.push(new Step(2, 3, c - (a + b)));
        animator.push(new Step(3, 3, c + b));
    }

    

    @Override
    public void mouseClicked(MouseEvent me) {
        squareCalculate();
        animate(110);
    }

    @Override
    public void mousePressed(MouseEvent me) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent me) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Timer fired: used to make another step in the animation.
     * When animation finishes, calculates sums, resets display and stops the timer.
     * @param ae the timer event
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        
        if (animator.peekLast() != null) {      // animation active
            labelsClear();
            Step step = animator.pollLast();
            labels[step.col][step.row].setBackground(Color.yellow);
            values[step.col][step.row] = step.value;
            squareDisplayCell(step.col, step.row);
            paintImmediately(0, 0, getWidth(), getHeight());
        }
        else {                                  // animation finisned: stop
            timer.stop();
            labelsClear();
            sumsCalculate();
            sumsDisplay();
            paintImmediately(0, 0, getWidth(), getHeight());            
        }

    }

    
}


/**
 * Represents one step in an animation sequence.
 * @author stimpy
 */
class Step {
    public int row;
    public int col;
    public int value;
    
    public Step(int col, int row, int value) {
        this.col = col;
        this.row = row;
        this.value = value;
    }
}
