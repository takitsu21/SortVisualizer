import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Random;
import javax.swing.Timer;


class QuickSortTest {
    private final static Random r = new Random();

    public static void main(String[] args) {
        QuickSortTest myTest = new QuickSortTest();
        int t[] = ArrayUtil.getRandomArray(15);
        System.out.println(Arrays.toString(t));
        myTest.quickSort(t, 0, t.length - 1);
        System.out.println(Arrays.toString(t));
    }

    int partition(int arr[], int l, int h) {
        int x = arr[h];
        int i = (l - 1);

        for (int j = l; j <= h - 1; j++) {
            if (arr[j] <= x) {
                i++;
                ArrayUtil.swap(arr, i, j);
            }
        }
        ArrayUtil.swap(arr, i + 1, h);
        return (i + 1);
    }

    // Sorts arr[l..h] using iterative QuickSort
    void quickSort(int arr[], int l, int h) {
        // create auxiliary stack
        System.out.println(h - l + 1);
        int stack[] = new int[h - l + 1];

        // initialize top of stack
        int top = -1;

        // push initial values in the stack
        stack[++top] = l;
        stack[++top] = h;

        // keep popping elements until stack is not empty
        while (top >= 0) {
            // pop h and l
            h = stack[top--];
            l = stack[top--];

            // set pivot element at it's proper position
            int p = partition(arr, l, h);

            // If there are elements on left side of pivot,
            // then push left side to stack
            if (p - 1 > l) {
                stack[++top] = l;
                stack[++top] = p - 1;
            }

            // If there are elements on right side of pivot,
            // then push right side to stack
            if (p + 1 < h) {
                stack[++top] = p + 1;
                stack[++top] = h;
            }
        }
    }
}

public class QuickSortVisualizer {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        QuickSort panel = new QuickSort();
        frame.setBackground(Color.BLACK);
        System.out.println(Arrays.toString(panel.array));
        frame.setSize(panel.WIDTH, panel.HEIGHT);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(panel);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setTitle("Quick Sort");
        JButton startSort = new JButton("Start");
        startSort.addActionListener(panel);
        panel.add(startSort);

    }
}

class QuickSort extends JPanel implements ActionListener {
    public int[] array;
    private final int[] pos;
    private final int[] stack;
    public int WIDTH;
    public int HEIGHT;
    private int _x, v1;
    private int h;
    private final int arrayLength;

    private int l = 0, top = -1;
    private int totalArrayAccess = 0;
    private int baseHeight;
    private final int delay = 30;
    private final static Random r = new Random();
    private long start = 0L;
    private boolean isSort = false;

    private final double elapseTimeCorrection = Math.pow(10, 9);

    public QuickSort(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.arrayLength = (width / 4) - 3;
        this.pos = new int[arrayLength];
        this.array = this.generateArray();
        this.h = array.length - 1;
        this.stack = new int[h - l + 1];
        this.baseHeight = HEIGHT - 40;
        stack[++top] = l;
        stack[++top] = h;
    }

    public QuickSort() {
        this(700, 300);
    }

    public int[] generateArray() {
        int[] t = new int[arrayLength];
        int xp = 0;
        int space = 4;
        for (int i = 0; i < arrayLength; i++) {
//            t[i] = r.nextInt(HEIGHT - 100);
            t[i] = arrayLength - i;
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
        long now = System.currentTimeMillis();
        g.setColor(Color.white);
        g.drawString("Array access : " + totalArrayAccess, 10, 5 + g.getFontMetrics().getHeight());
        g.drawString("Delay : " + delay + " ms", 10, 20 + g.getFontMetrics().getHeight());
        g.drawString(
                "Elapse time : " +
                        (((now - start) > elapseTimeCorrection)
                                ? 0 : (now - start)) + " ms",
                10, 35 + g.getFontMetrics().getHeight());
        int i;
        if (isSort) {
            g.setColor(Color.green);
            for (i = 0; i < array.length - 1; i++) {
                g.fillRect(pos[i], baseHeight, rectWidth, -array[i]);
            }
            g.setColor(Color.red);
            g.fillRect(pos[i], baseHeight, rectWidth, -array[i]);
        } else {
            for (i = 0; i < array.length - 1; i++) {
                g.fillRect(pos[i], baseHeight, rectWidth, -array[i]);
            }
            g.setColor(Color.red);
            g.fillRect(_x, baseHeight, rectWidth, -v1);
        }

    }

    private int partition() {
        int x = array[h];
        totalArrayAccess++;
        int i = (l - 1);
        for (int j = l; j <= h - 1; j++) {
            if (array[j] <= x) {
                i++;
                ArrayUtil.swap(array, i, j);
                _x = pos[i];
                v1 = array[i];
                totalArrayAccess += 4;
                repaint();
            }
            totalArrayAccess++;

        }
        ArrayUtil.swap(array, i + 1, h);
        totalArrayAccess += 4;
        repaint();
        return (i + 1);
    }

    private void sortOne() {
        if (top >= 0) {
            // pop h and l
            h = stack[top--];
            l = stack[top--];

            // set pivot element at it's proper position
            int p = partition();
            // If there are elements on left side of pivot,
            // then push left side to stack
            if (p - 1 > l) {
                stack[++top] = l;
                stack[++top] = p - 1;
            }

            // If there are elements on right side of pivot,
            // then push right side to stack
            if (p + 1 < h) {
                stack[++top] = p + 1;
                stack[++top] = h;
            }
        } else {
            isSort = true;
            repaint();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        start = System.currentTimeMillis();
        Timer timer = new Timer(delay, new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (isSort) {
                    ((Timer) e.getSource()).stop();
                    System.out.println("Sort done");
                    System.out.println(Arrays.toString(array));
                } else {
                    sortOne();
                }
            }
        });
        timer.start();

    }
}