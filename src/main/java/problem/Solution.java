package problem;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.run(IntStream.generate(() -> new Random().nextInt(1000)).limit(1000000).toArray());
        //solution.run(new int[]{1, 2, 3, 4, 2, 7, 6, 6, 7, 8, 2, 7, 7});
    }

    public void run(int[] array) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int j : array) {
            if (map.containsKey(j)) map.put(j, map.get(j) + 1);
            else map.put(j, 1);
        }

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() >= 2) System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
