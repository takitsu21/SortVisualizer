import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Random;
import javax.swing.Timer;


class RadixSortLSDTest {
    private final static Random r = new Random();

    public static void main(String[] args) {
        RadixSortLSDTest myTest = new RadixSortLSDTest();
        int[] t = ArrayUtil.getRandomArray(497);
        System.out.println(Arrays.toString(t));
        myTest.sort(t);
        System.out.println(Arrays.toString(t));
    }

    public static void sort(int[] array) {
        sort(array, 10);
    }

    public static void sort(int[] array, int radix) {
        if (array.length == 0) {
            return;
        }

//         Determine minimum and maximum values
        int minValue = array[0];
        int maxValue = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < minValue) {
                minValue = array[i];
            } else if (array[i] > maxValue) {
                maxValue = array[i];
            }
        }

        // Perform counting sort on each exponent/digit, starting at the least
        // significant digit
        int exponent = 1;
        while ((maxValue - minValue) / exponent >= 1) {
            countingSortByDigit(array, radix, exponent, minValue);
            exponent *= radix;
        }
    }

    private static void countingSortByDigit(
            int[] array, int radix, int exponent, int minValue) {
        int bucketIndex;
        int[] buckets = new int[radix];
        int[] output = new int[array.length];

        // Initialize bucket
        for (int i = 0; i < radix; i++) {
            buckets[i] = 0;
        }

        // Count frequencies
        for (int i = 0; i < array.length; i++) {
            bucketIndex = (((array[i] - minValue) / exponent) % radix);
            buckets[bucketIndex]++;
        }

        // Compute cumulates
        for (int i = 1; i < radix; i++) {
            buckets[i] += buckets[i - 1];
        }

        // Move records
        for (int i = array.length - 1; i >= 0; i--) {
            bucketIndex = (int) (((array[i] - minValue) / exponent) % radix);
            output[--buckets[bucketIndex]] = array[i];
        }
        // Copy back
        for (int i = 0; i < array.length; i++) {
            array[i] = output[i];
        }
    }
}


public class RadixSortLSDVisualizer {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        RadixSortLSD panel = new RadixSortLSD();
        frame.setBackground(Color.BLACK);
        System.out.println(Arrays.toString(panel.array));
        frame.setSize(panel.WIDTH, panel.HEIGHT);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(panel);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setTitle("Radix Sort LSD");
        JButton startSort = new JButton("Start");
        startSort.addActionListener(panel);
        panel.add(startSort);
    }
}

class RadixSortLSD extends JPanel implements ActionListener {
    public int[] array;
    private int[] pos;
    public int WIDTH;
    public int HEIGHT;
    private int _x, v1;
    private final int arrayLength;
    private int bucketIndex;
    private int[] buckets;
    private int[] output;
    private int baseHeight;

    private int minValue = 0;
    private int maxValue = 0;
    private int radix;
    private int exponent = 1;
    private int totalArrayAccess = 0;
    private final int delay = 10;
    private final static Random r = new Random();
    private long start = 0L;
    private boolean isSort = false;
    private int copyIdx = 0;
    private boolean isWaiting = false;
    private final double elapseTimeCorrection = Math.pow(10, 9);

    public RadixSortLSD(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.arrayLength = (width / 4) - 3;
        this.pos = new int[arrayLength];
        this.array = this.generateArray();
        this.output = new int[arrayLength];
        this.baseHeight = HEIGHT - 40;
        this.radix = 10;
        this.buckets = new int[radix];

        for (int i = 0; i < arrayLength; i++) {
            if (array[i] < minValue) {
                this.minValue = array[i];
            } else if (array[i] > maxValue) {
                this.maxValue = array[i];
            }
        }
    }

    public RadixSortLSD() {
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
//        g.setColor(Color.white);
//        for (i = 0; i < array.length - 1; i++) {
//            g.fillRect(pos[i], HEIGHT - 40, rectWidth, -array[i]);
//        }
//        g.setColor(Color.red);
//        g.fillRect(pos[i], baseHeight, rectWidth, -array[i]);
        if (isSort) {
            g.setColor(Color.green);
            for (i = 0; i < array.length - 1; i++) {
                g.fillRect(pos[i], baseHeight, rectWidth, -array[i]);
            }
            g.setColor(Color.red);
            g.fillRect(pos[i], baseHeight, rectWidth, -array[i]);
        } else {
            g.setColor(Color.white);
            for (i = 0; i < array.length - 1; i++) {
                g.fillRect(pos[i], baseHeight, rectWidth, -array[i]);
            }
            g.setColor(Color.red);
            g.fillRect(_x, baseHeight, rectWidth, -v1);
        }

    }

    private void update() {
        for (int i = 0; i < radix; i++) {
            buckets[i] = 0;
        }

        // Count frequencies
        for (int i = 0; i < array.length; i++) {
            bucketIndex = (array[i] - minValue) / exponent % radix;
            buckets[bucketIndex]++;
            totalArrayAccess++;
        }

        // Compute cumulates
        for (int i = 1; i < radix; i++) {
            buckets[i] += buckets[i - 1];
            totalArrayAccess++;
        }

        // Move records
        for (int i = array.length - 1; i >= 0; i--) {
            bucketIndex = (array[i] - minValue) / exponent % radix;
            output[--buckets[bucketIndex]] = array[i];
            totalArrayAccess += 2;
        }
    }

    private void sortOne() {
        if ((maxValue - minValue) / exponent >= 1) {
            if (!isWaiting) {
                update();
                isWaiting = true;
            }
            if (copyIdx < arrayLength) {
                array[copyIdx] = output[copyIdx];
                _x = pos[copyIdx];
                v1 = array[copyIdx];
                totalArrayAccess++;
                copyIdx++;
            } else {
                exponent *= radix;
                copyIdx = 0;
                isWaiting = false;
            }
        }
        if ((maxValue - minValue) / exponent < 1) {
            isSort = true;
        }
        repaint();


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