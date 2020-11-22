import java.util.Arrays;
import java.util.Random;

public final class ArrayUtil {
    private ArrayUtil() {
    }

    private static final Random r = new Random();

    public static void swap(int[] t, int i, int j) {
        int tmp = t[i];
        t[i] = t[j];
        t[j] = tmp;
    }

    public static void main(String[] args) {
        int[] t = {1, 2, 3, 4};
        System.out.println(Arrays.toString(t));
        shuffle(t);
        System.out.println(Arrays.toString(t));
        int[] rArray = getRandomArray(15);
        System.out.println(Arrays.toString(rArray));
    }

    public static void shuffle(int[] t) {
        for (int i = t.length - 1; 0 < i; i--) {
            int j = r.nextInt(i);
            swap(t, i, j);
        }
    }

    public static int[] getRandomArray(int n) {
        int[] arrayRet = new int[n];
        for (int i = 0; i < n; i++) {
            arrayRet[i] = i;
        }
        shuffle(arrayRet);
        return arrayRet;
    }
}
