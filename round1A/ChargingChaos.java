

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChargingChaos {
    int N = 0;
    int L = 0;
    String[] strOutputs = null;
    String[] strTargets = null;
    int[] outputs = null;
    int[] targets = null;
    int[][] dist = null;
    long[][] diff = null;
    ArrayList<Long>[] diffs = null;
    Map<Long, Integer> pairs = new HashMap<>();
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please add the file name as the first parameter");
        }
        
        try {
            ChargingChaos cc = new ChargingChaos();
            BufferedReader in = new BufferedReader(new FileReader(args[0]));
            int cases = Integer.parseInt(in.readLine());
            
            for (int caseNo = 1; caseNo <= cases; caseNo++) {
                // get test case data
                String[] vars = in.readLine().split("\\s");
                cc.N = Integer.parseInt(vars[0]);
                cc.L = Integer.parseInt(vars[1]);
                
                cc.strOutputs = in.readLine().split("\\s");
                cc.strTargets = in.readLine().split("\\s");
                
                System.out.format("Case #%d: ", caseNo);
                int result = cc.getSwitchSolution();
                if (result == -1) {
                    System.out.println("NOT POSSIBLE");
                } else {
                    System.out.println(result);
                }
            }
            in.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    
    /**
     * Find a solution and print it out. Brute Force
     */
    public int getSwitchSolution() {
        /*
         * Enumerate all arrangements, and find the possibilities
         */
        if (N == 1) {
            return getHemingDist(strOutputs[0], strTargets[0]);
        }
        
        outputs = new int[N];
        targets = new int[N];
        
        for (int i = 0; i < N; i++) {
            outputs[i] = i;
            targets[i] = i;
        }
        
        dist = new int[N][N];
        diff = new long[N][N];
        
        getAllHemingDists();
        
        for (int d = 0; d <= L; d++) {
            for (Long dfs : diffs[d]) {
                if (pairs.get(dfs) == N)
                    return d;
            }
        }
        
        return -1;
    }

    
    private int getHemingDist(String s1, String s2) {
        int hemingDist = 0;
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                hemingDist++;
            }
        }
        return hemingDist;
    }

    private void getHemingDist(int s1, int s2) {
        int hemingDist = 0;
        long aDiff = 0;
        for (int i = 0; i < L; i++) {
            if (strOutputs[s1].charAt(i) != strTargets[s2].charAt(i)) {
                hemingDist++;
                aDiff++;
            }
            aDiff <<= 1;
        }
        dist[s1][s2] = hemingDist;
        diff[s1][s2] = aDiff;
    }


    @SuppressWarnings("unchecked")
    private void getAllHemingDists() {
        diffs = new ArrayList[L + 1];
        for (int i = 0; i <= L; i++) {
            diffs[i] = new ArrayList<Long>();
        }
        pairs.clear();
        
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                getHemingDist(i, j);
                int aDist = dist[i][j];
                long aDiff = diff[i][j];
                
                Integer prs = pairs.get(aDiff);
                if (prs == null) {
                    diffs[aDist].add(aDiff);
                    pairs.put(aDiff, 1);
                } else {
                    pairs.put(aDiff, prs + 1);
                }
            }
        }    
    }
}
