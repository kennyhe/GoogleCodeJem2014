

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Solution to the Google Gem 2014 Round 1-A
 * @author Kenny He (she@scu.edu, he.scu2013@gmail.com)
 * 
 * Idea for the solution:
 * 1. Brute Force: Assign each node as root, and then try to maximize the count of nodes in a full Binary Tree
 * span from this node:
 *   1.1) If the root node has only one neighbor, then all other nodes needs to be removed;
 *   1.2) If the root node has two neighbors, then try to check the nodes count of the two sub trees;
 *   1.3) If the root node has more than two neighbors, then try to check all possible combination of two sub trees.
 *   
 * 2. Optimization:
 *   2.1) If there are at least one node which has only two neighbors, one of them must be the root of the full BT with the 
 *   max count of nodes;
 *   2.2) The nodes count of each sub tree can be cached to avoid repeated calculation.
 */

public class FullBT {
    int N = 0;
    ArrayList<Integer>[] adjList = null;  /* Adjacent list array, to represent the tree */
    ArrayList<Integer> twoNeighborNodes = null; /* store the nodes which have two neighbors */
    int[][] spanNodesCount = null;   /** Cache the nodes count of sub full BT tree. First dim: node ID; second dim: Parent ID */ 
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please add the file name as the first parameter");
        }
        
        try {
            FullBT fbt = new FullBT();
            BufferedReader in = new BufferedReader(new FileReader(args[0]));
            int cases = Integer.parseInt(in.readLine());
            
            for (int caseNo = 1; caseNo <= cases; caseNo++) {
                // get test case data
                fbt.N = Integer.parseInt(in.readLine());;
                
                fbt.readData(in);
                int result = fbt.getSolution();
                
                System.out.format("Case #%d: %d\n", caseNo, result);
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
     * Read the data, store in the adjacent list, and pre-processing them.
     * @param in The input stream
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void readData(BufferedReader in) throws IOException {
        adjList = new ArrayList[N + 1];
        for (int i = 1; i <= N; i++) {
            adjList[i] = new ArrayList<Integer>();
        }
        
        // Generate the adjacent list
        for (int i = 0; i < N - 1; i++) {
            String[] nodes = in.readLine().split("\\s");
            int node1 = Integer.parseInt(nodes[0]);
            int node2 = Integer.parseInt(nodes[1]);
            adjList[node1].add(node2);
            adjList[node2].add(node1);
        }
        
        // Find out the nodes with two neighbors
        twoNeighborNodes = new ArrayList<>();
        for (int i = 1; i <= N; i++) {
            if (adjList[i].size() == 2) {
                twoNeighborNodes.add(i);
            }
        }
        
        // Initial the cache (all elements are zero at the beginning)
        spanNodesCount = new int[N+1][N+1];
    }
    
    
    /**
     * Find a solution
     */
    public int getSolution() {
        if (twoNeighborNodes.isEmpty()) {
            // no nodes with two neighbors, need to try all nodes as root
            int maxSpanNodesCount = getNodes4FullBT(1);
            for (int i = 2; i <= N; i++) {
                int count = getNodes4FullBT(i);
                if (count > maxSpanNodesCount) {
                    maxSpanNodesCount = count;
                }
            }
            return N - maxSpanNodesCount;
        } else {
            // only try those 2 neighbor nodes as root
            int maxSpanNodesCount = 0;
            for (Integer i : twoNeighborNodes) {
                int count = getNodes4FullBT(i);
                if (count > maxSpanNodesCount) {
                    maxSpanNodesCount = count;
                }
            }
            return N - maxSpanNodesCount;
        }
    }
    

    private int getNodes4FullBT(int rootID) {
        int adjCount = adjList[rootID].size();
        if (adjCount < 2) {
            return 1; 
        }
        
        int maxSpanCount = 0;
        for (int i = 0; i < adjCount - 1; i++) {
            for (int j = i + 1; j < adjCount; j++) {
                int spanCount = getSpanNodesCount(adjList[rootID].get(i), rootID) 
                                + getSpanNodesCount(adjList[rootID].get(j), rootID);
                if (spanCount > maxSpanCount) {
                    maxSpanCount = spanCount;
                }
            }
        }
        
        return maxSpanCount + 1;
    }

    
    private int getSpanNodesCount(int nodeID, int excludeID) {
        // Cache the result
        if (spanNodesCount[nodeID][excludeID] > 0) {
            return spanNodesCount[nodeID][excludeID];
        }
        
        int adjCount = adjList[nodeID].size();
        if (adjCount <= 2) { // single connected node, reach the end
            spanNodesCount[nodeID][excludeID] = 1;
            return 1; 
        } else { // multiple connected node, get the max span nodes count
            int maxSpanCount = 0;
            for (int i = 0; i < adjCount - 1; i++) {
                int node1 = adjList[nodeID].get(i);
                if (node1 != excludeID) {
                    for (int j = i + 1; j < adjCount; j++) {
                        int node2 = adjList[nodeID].get(j);
                        if (node2 != excludeID) {
                            int spanCount = getSpanNodesCount(node1, nodeID) 
                                            + getSpanNodesCount(node2, nodeID);
                            if (spanCount > maxSpanCount) {
                                maxSpanCount = spanCount;
                            }
                        }
                    }
                }
            }
            spanNodesCount[nodeID][excludeID] = maxSpanCount + 1;
        }
        return spanNodesCount[nodeID][excludeID];
    }

    
}
