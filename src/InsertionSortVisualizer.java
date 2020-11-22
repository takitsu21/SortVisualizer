import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Random;
import javax.swing.Timer;

public class InsertionSortVisualizer {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        InsertionSort panel = new InsertionSort();
        frame.setBackground(Color.BLACK);
        System.out.println(Arrays.toString(panel.array));
        frame.setSize(panel.WIDTH, panel.HEIGHT);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(panel);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setTitle("Insertion Sort");
        JButton startSort = new JButton("Start");
        startSort.addActionListener(panel);
        panel.add(startSort);
    }
}

class InsertionSort extends JPanel implements ActionListener {
    public int[] array;
    private final int[] pos;
    public int WIDTH;
    public int HEIGHT;
    private final int arrayLength;
    private int x, x2, v1, v2;

    private final static Random r = new Random();
    private final int delay = 30;
    private int curIdx = 1;
    private int jIdx = 0;
    private final int baseHeight;
    private boolean isSorted = false;
    private int totalArrayAccess = 0;
    private long start = 0L;
    private final double elapseTimeCorrection = Math.pow(10, 9);

    public InsertionSort(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.arrayLength = (width / 4) - 3;
        this.pos = new int[arrayLength];
        this.array = this.generateArray();
        this.baseHeight = HEIGHT - 40;
    }

    public InsertionSort() {
        this(700, 300);
    }

    public int[] generateArray() {
        int[] t = new int[arrayLength];
        int xp = 0;
        int space = 4;
        for (int i = 0; i < arrayLength; i++) {
            t[i] = r.nextInt(HEIGHT - 100);
            pos[i] = xp;
            xp += space;
        }
        return t;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        super.setBackground(Color.black);
        int rectWidth = 3;
        if (!isSorted) {
            g.setColor(Color.white);
            for (int i = 0; i < arrayLength - 1; i++) {
                if (x2 != pos[i]) {
                    g.fillRect(pos[i], baseHeight, rectWidth, -array[i]);
                }
            }
            g.setColor(Color.green);
            g.fillRect(x2, baseHeight, rectWidth, -v2);

            g.setColor(Color.red);
            g.fillRect(x, baseHeight, rectWidth, -v1);
        } else {

            g.setColor(Color.green);
            for (int i = 0; i < arrayLength - 1; i++) {
                g.fillRect(pos[i], baseHeight, rectWidth, -array[i]);
            }
            g.setColor(Color.red);
            g.fillRect(x2, baseHeight, rectWidth, -v2);
        }
        long now = System.currentTimeMillis();
        g.setColor(Color.white);
        g.drawString("Array access : " + totalArrayAccess, 10, 5 + g.getFontMetrics().getHeight());
        g.drawString("Delay : " + delay + " ms", 10, 20 + g.getFontMetrics().getHeight());
        g.drawString(
                "Elapse time : " +
                        (((now - start) > elapseTimeCorrection)
                                ? 0 : (now - start)) + " ms",
                10, 35 + g.getFontMetrics().getHeight());
    }

    private void sortOne() {
        int tmp;
        if (curIdx < arrayLength - 1) {
            tmp = array[curIdx];

            jIdx = curIdx;

            while (jIdx > 0 && array[jIdx - 1] > tmp) {
                array[jIdx] = array[jIdx - 1];
                totalArrayAccess += 2;
                jIdx--;
            }
            array[jIdx] = tmp;
            x = pos[jIdx];
            v1 = array[jIdx];
            x2 = pos[curIdx];
            v2 = array[curIdx];
            totalArrayAccess++;
            curIdx++;
        } else {
            isSorted = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        start = System.currentTimeMillis();
        Timer timer = new Timer(delay, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isSorted) {
                    ((Timer) e.getSource()).stop();
                    System.out.println("Sort done");
                    System.out.println(Arrays.toString(array));
                } else {
                    sortOne();
                    repaint();
                }

            }
        });
        timer.start();
    }
}