import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Random;
import javax.swing.Timer;

public class SelectionSortVisualizer {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        SelectionSort panel = new SelectionSort();
        frame.setBackground(Color.BLACK);
        System.out.println(Arrays.toString(panel.array));
        frame.setSize(panel.WIDTH, panel.HEIGHT);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(panel);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setTitle("Selection Sort");
        JButton startSort = new JButton("Start");
        startSort.addActionListener(panel);
        panel.add(startSort);
    }
}

class SelectionSort extends JPanel implements ActionListener {
    public int[] array;
    private final int[] pos;
    public int WIDTH;
    public int HEIGHT;
    private final int arrayLength;
    private int x, x2, v1, v2;

    private final static Random r = new Random();
    private final int delay = 30;
    private int curIdx = 0;
    private final int baseHeight;
    private int totalArrayAccess = 0;
    private long start = 0L;
    private final double elapseTimeCorrection = Math.pow(10, 9);

    public SelectionSort(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;

        this.arrayLength = (width / 4) - 3;
        this.pos = new int[arrayLength];
        this.array = this.generateArray();
        this.baseHeight = HEIGHT - 40;
    }

    public SelectionSort() {
        this(700, 300);
    }


    private boolean isSortingDone() {
        return curIdx == arrayLength;
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
        if (!isSortingDone()) {
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
        int indexMin = curIdx;
        int i;
        for (i = curIdx; i < arrayLength; i++) {
            if (array[i] < array[indexMin]) {
                indexMin = i;
            }
            totalArrayAccess += 2;
        }
        x = pos[curIdx];
        x2 = pos[indexMin];
        v1 = array[curIdx];
        v2 = array[indexMin];
        ArrayUtil.swap(array, curIdx, indexMin);
        totalArrayAccess += 4;
        curIdx++;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        start = System.currentTimeMillis();
        Timer timer = new Timer(delay, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isSortingDone()) {
                    ((Timer) e.getSource()).stop();
                    System.out.println("Sort done");
                    System.out.println(Arrays.toString(array));
                } else {
                    sortOne();
                }
                repaint();
            }
        });
        timer.start();
    }
}