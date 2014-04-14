import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;


public class ScoreCredit {

    
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please add the file name as the first parameter");
        }
        
        ScoreCredit sc = new ScoreCredit();
        
        try {
            BufferedReader in = new BufferedReader(new FileReader(args[0]));
            int cases = Integer.parseInt(in.readLine());
            
            for (int i = 1; i <= cases; i++) {
                // get test case data
                int credit = Integer.parseInt(in.readLine());
                int count = Integer.parseInt(in.readLine());
                int[] prices = new int[count];
                String[] cprices = in.readLine().split("\\s");
                for (int j = 0; j < count; j++) {
                    prices[j] = Integer.parseInt(cprices[j]);
                }
                
                Solution solution = sc.getSolution(credit, prices);
                System.out.println("Case #" + i + ": " + solution.v1 + " " + solution.v2);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private Solution getSolution(int credit, int[] values) {
        int[] prices = Arrays.copyOf(values, values.length);
        Arrays.sort(prices);
        int max = prices.length - 1;
        int p1, p2 = 0, v1 = 0, v2 = 0, v2t = 0;
        for (p1 = 0; p1 < max; p1++) {
            v1 = prices[p1];
            v2 = credit - v1;
            p2 = max - p1;
            if (p2 == p1) {
                p2++;
            }
            
            v2t = prices[p2];
            if (v2 == v2t) {
                break;
            } else if (v2 < v2t) {
                p2 = binarySearch(credit - prices[p1], prices, p2 + 1, max);
            } else {
                p2 = binarySearch(credit - prices[p1], prices, p1 + 1, p2 - 1);
            }
            if (p2 > 0) {
                break;
            }
        }
        
        return new Solution(index(v1, values), index(v2, values));
    }
    
    private static int index(int value, int[] array) {
        for (int p1 = 0; p1 < array.length; p1++) {
            if (array[p1] == value)
                return p1;
        }
        return -1;
    }
    
    // return the smallest index at which the array item is equal or bigger than the value
    private static int binarySearch(int value, int[] array, int start, int end) {
        if (value < array[start])
            return start;
        
        if (value > array[end]) {
            return end + 1;
        }
        
        if (array[start] == value)
            return start;
        else if (array[end] == value)
            return end;
        else if (start + 1 == end) {
            return end;
        } else {
            int mid = (start + end) / 2;
            if (array[mid] == value)
                return mid;
            else if (array[mid] > value) {
                return binarySearch(value, array, start + 1, mid -1);
            } else {
                return binarySearch(value, array, mid + 1, end -1);
            }
        }
    }

    class Solution {
        int v1, v2;
        Solution (int p1, int p2) {
            v1 = p1 + 1; v2 = p2 + 1;
        }
    }
}
