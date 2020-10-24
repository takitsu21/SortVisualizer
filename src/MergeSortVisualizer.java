import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;
import javax.swing.Timer;

class MergeSortTest {
    public static void main(String[] args) {
        MergeSortTest myTest = new MergeSortTest();
        int t[] = ArrayUtil.getRandomArray(15);
        System.out.println(Arrays.toString(t));
        myTest.mergeSort(t, t.length);
        System.out.println(Arrays.toString(t));
    }

    static void mergeSort(int arr[], int n) {
        int curr_size;
        int left_start;
        for (curr_size = 1; curr_size <= n - 1;
             curr_size = 2 * curr_size) {

            for (left_start = 0; left_start < n - 1;
                 left_start += 2 * curr_size) {
                int mid = Math.min(left_start + curr_size - 1, n - 1);

                int right_end = Math.min(left_start
                        + 2 * curr_size - 1, n - 1);

                merge(arr, left_start, mid, right_end);
            }
        }
    }

    static void merge(int arr[], int l, int m, int r) {
        int i, j, k;
        int n1 = m - l + 1;
        int n2 = r - m;
        int L[] = new int[n1];
        int R[] = new int[n2];

        for (i = 0; i < n1; i++)
            L[i] = arr[l + i];
        for (j = 0; j < n2; j++)
            R[j] = arr[m + 1 + j];
        i = 0;
        j = 0;
        k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }
}

public class MergeSortVisualizer {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        MergeSort panel = new MergeSort();
        frame.setBackground(Color.BLACK);
        System.out.println(Arrays.toString(panel.array));
        frame.setSize(panel.WIDTH, panel.HEIGHT);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(panel);
        frame.setVisible(true);
        frame.setTitle("Merge Sort");
        JButton startSort = new JButton("Start");
        startSort.addActionListener(panel);
        panel.add(startSort);
    }
}

class MergeSort extends JPanel implements ActionListener {
    public int[] array;
    public int[] pos;
    public int WIDTH;
    public int HEIGHT;
    private int arrayLength;
    public Timer timer;
    //    public int x, x2, v1, v2;
    private boolean isSort = false;
    private final static Random r = new Random();
    private int delay = 30;
    private int curr_size = 1, left_start = 0;
    private int x, v;
    private long start = 0L, now = 0L;
    private int totalArrayAccess = 0;


    public MergeSort(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;

        this.arrayLength = (width / 4) - 3;
        this.pos = new int[arrayLength];
        this.array = this.generateArray();


    }

    public MergeSort() {
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


        int i;
        if (!isSort) {
            g.setColor(Color.white);
            for (i = 0; i < arrayLength - 1; i++) {
                g.fillRect(pos[i], HEIGHT - 40, 3, -array[i]);
            }
            g.setColor(Color.red);
            g.fillRect(x, HEIGHT - 40, 3, -v);
        } else {
            g.setColor(Color.green);
            for (i = 0; i < arrayLength - 1; i++) {
                g.fillRect(pos[i], HEIGHT - 40, 3, -array[i]);
            }
            g.setColor(Color.red);
            g.fillRect(x, HEIGHT - 40, 3, -v);
        }
        now = System.currentTimeMillis();
        g.setColor(Color.white);
        g.drawString("Array access : " + totalArrayAccess, 10, 5 + g.getFontMetrics().getHeight());
        g.drawString("Delay : " + delay + " ms", 10, 20 + g.getFontMetrics().getHeight());
        g.drawString(
                "Elapse time : " +
                        (((now - start) > Math.pow(10, 9))
                                ? 0 : (now - start)) + " ms",
                10, 35 + g.getFontMetrics().getHeight());

    }

    private void sortOne() {
        if (curr_size <= arrayLength - 1) {
            if (left_start < arrayLength - 1) {
                int mid = Math.min(left_start + curr_size - 1, arrayLength - 1);

                int right_end = Math.min(left_start
                        + 2 * curr_size - 1, arrayLength - 1);

                merge(left_start, mid, right_end);
            }
            left_start += 2 * curr_size;
            if (left_start >= arrayLength - 1) {
                left_start = 0;
                curr_size = 2 * curr_size;
            }
        } else {
            isSort = true;
        }
    }

    private void merge(int l, int m, int r) {
        int i, j, k;
        int n1 = m - l + 1;
        int n2 = r - m;
        int L[] = new int[n1];
        int R[] = new int[n2];

        for (i = 0; i < n1; i++)
            L[i] = array[l + i];
        totalArrayAccess++;
        for (j = 0; j < n2; j++)
            R[j] = array[m + 1 + j];
        totalArrayAccess++;
        i = 0;
        j = 0;
        k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                array[k] = L[i];
                i++;
            } else {
                array[k] = R[j];
                j++;
            }
            x = pos[k];
            v = array[k];
            k++;
            totalArrayAccess += 2;
        }
        while (i < n1) {
            array[k] = L[i];
            x = pos[k];
            v = array[k];
            i++;
            k++;
            totalArrayAccess++;

        }
        while (j < n2) {
            array[k] = R[j];
            x = pos[k];
            v = array[k];
            j++;
            k++;
            totalArrayAccess++;
        }

        repaint();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        start = System.currentTimeMillis();
        timer = new Timer(delay, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isSort) {
                    ((Timer) e.getSource()).stop();
                    System.out.println("Sort done");
                    System.out.println(Arrays.toString(array));
                    repaint();
                }
                sortOne();
            }
        });
        timer.start();
    }
}