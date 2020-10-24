import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Random;
import javax.swing.Timer;

class BubbleSortTest {
    public static void main(String[] args) {
        BubbleSortTest myTest = new BubbleSortTest();
        int t[] = ArrayUtil.getRandomArray(15);
        System.out.println(Arrays.toString(t));
        myTest.bubbleSortOptimized(t);
        System.out.println(Arrays.toString(t));
    }

    public void bubbleSortOptimized(int[] t) {
        boolean isSort;
        for (int i = t.length - 1; i > 0; i--) {
            isSort = true;
            for (int j = 0; j < i; j++) {
                if (t[j + 1] < t[j]) {
                    ArrayUtil.swap(t, j + 1, j);
                    isSort = false;
                }
            }
            if (isSort) {
                break;
            }
        }
    }
}

public class BubbleSortVisualizer {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        BubbleSort panel = new BubbleSort();
        frame.setBackground(Color.BLACK);
        System.out.println(Arrays.toString(panel.array));
        frame.setSize(panel.WIDTH, panel.HEIGHT);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(panel);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setTitle("Bubble Sort");
        JButton startSort = new JButton("Start");
        startSort.addActionListener(panel);
        panel.add(startSort);
    }
}

// using optimized bubble sort
class BubbleSort extends JPanel implements ActionListener {
    public int[] array;
    private final int[] pos;
    public int WIDTH;
    public int HEIGHT;
    private final int arrayLength;
    public int x, x2, v1, v2;
    private long start;
    private int curIdx;

    private boolean isSort = true; // for bubble sort optimized
    private boolean lastCheck = false;
    private final int delay = 30;
    private final int baseHeight;
    private int totalArrayAccess = 0;

    private final static Random r = new Random();
    private final double elapseTimeCorrection = Math.pow(10, 9);


    public BubbleSort(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.arrayLength = (width / 4) - 3;
        this.pos = new int[arrayLength];
        this.array = this.generateArray();
        this.curIdx = arrayLength - 1;
        this.baseHeight = HEIGHT - 40;
    }

    public BubbleSort() {
        this(700, 300);
    }

    private boolean isSortingDone() {
        return curIdx == 0;
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
        long now = System.currentTimeMillis();
        int rectWidth = 3;
        g.setColor(Color.white);
        if (!lastCheck) {
            for (int i = 0; i < arrayLength - 1; i++) {
                g.fillRect(pos[i], baseHeight, rectWidth, -array[i]);
            }
            g.setColor(Color.red);
            g.fillRect(x2, baseHeight, rectWidth, -v2);

            g.setColor(Color.green);
            g.fillRect(x, baseHeight, rectWidth, -v1);
        } else {
            g.setColor(Color.green);
            for (int i = 0; i < arrayLength - 1; i++) {

                if (i == arrayLength - 2) {
                    g.setColor(Color.red);
                }
                g.fillRect(pos[i], baseHeight, rectWidth, -array[i]);
            }
        }
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
        int i;
        for (i = 0; i < curIdx; i++) {
            if (array[i + 1] < array[i]) {
                ArrayUtil.swap(array, i + 1, i);
                isSort = false;
                repaint();
            }
            totalArrayAccess += 6;
        }
        x = pos[curIdx];
        x2 = pos[i];
        v1 = array[curIdx];
        v2 = array[i];
        curIdx--;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        start = System.currentTimeMillis();
        Timer timer = new Timer(delay, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sortOne();
                if (isSort) {
                    lastCheck = true;
                    ((Timer) e.getSource()).stop();
                    System.out.println("Sort done");
                    System.out.println(Arrays.toString(array));
                    repaint();
                }
                isSort = true;
            }
        });
        timer.start();
    }
}