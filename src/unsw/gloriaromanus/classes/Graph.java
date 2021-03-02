package unsw.gloriaromanus.classes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.json.JSONArray;

//import javax.swing.text.html.HTMLDocument.Iterator;

import org.json.JSONObject;

import unsw.gloriaromanus.ArrayUtil;

public class Graph {

    public static int V = 53;
    private HashMap<String, String> pred;
    private HashMap<String, Integer> dist;
    private HashMap<String, Boolean> visited;
    
    private JSONObject provinceAdjacencyMatrix;

    public Graph() throws IOException {
        String content = Files.readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json"));
        // generate matrix
        this.provinceAdjacencyMatrix = new JSONObject(content);
        this.pred = getPred();
        this.dist = getDist();
        this.visited = getVisitedMap();
    }
    /**
     * Set a node to be visited
     * @param province
     * @throws IOException
     */
    private void visitNode(String province) throws IOException {
        this.visited.replace(province, true);
    }

    /**
     * Set distance
     * @param province
     * @param distance
     */
    private void setDistance(String province, int distance){
        this.dist.replace(province, distance);
    }

    /**
     * Get distance from dist array given a province string
     * @param province
     * @return
     */
    private int getDistance(String province) {
        return this.dist.get(province);
    }

    /**
     * Set the parent of a node
     * @param province
     * @param parent
     */
    private void setParent(String province, String parent) {
        this.pred.replace(province, parent);
    }

    /**
     * Check if there is a path between two provinces or not
     * @param from
     * @param to
     * @return
     * @throws IOException
     */
    private boolean BFS(String from, String to, Faction currFaction) throws IOException {
        // visted array
        // create a queue
        LinkedList<String> queue = new LinkedList<>(); 
        // Length 
        // add from to queue
        queue.add(from);
        setDistance(from, 0);
        visitNode(from);
        while (!queue.isEmpty()){
            // dequeue
            // add all adjacent provinces to list
            String currentProvince = queue.remove();
            JSONObject curr = this.provinceAdjacencyMatrix.getJSONObject(currentProvince);
            // curr is a list of key value pairs
            // iterate through neighbours 
            Iterator<String> neighbours = curr.keys();
            while(neighbours.hasNext()) {
                String adj = neighbours.next();

                // if we reached to our distanistion
                if (adj.equals(to)) {
                    this.setDistance(adj, getDistance(currentProvince) + 1);
                    // set parent node
                    this.setParent(adj, currentProvince);
                    return true;
                }
                // if not visited and is connected
                if (curr.getBoolean(adj) == true && !this.visited.get(adj)) {
                    // if this node isnt belonged to the current Faction and isnt the destination (destination can belong to the enemey)
                    if (!currFaction.getProvincesString().contains(adj) && !adj.equals(to)) {
                        continue;
                    }
                    // visit the node
                    this.visitNode(adj);
                    //System.out.println("adj= " + adj);
                    // update distance array 
                    this.setDistance(adj, getDistance(currentProvince) + 1);
                    // set parent node
                    this.setParent(adj, currentProvince);
                    // add neighbour to queue
                    queue.add(adj);
                    // if we found a path
                    if (adj.equals(to)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // find province owner given province string
    /**
     * Create a hashmap to store distance between nodes
     * @return
     * @throws IOException
     */
    private HashMap<String, Integer> getDist() throws IOException {
        String provinces = Files.readString(Paths.get("src/unsw/gloriaromanus/province_list.json"));
        //System.out.println(provinces);
        JSONArray ownership = new JSONArray(provinces);
        HashMap<String, Integer> dist = new HashMap<>();
        ArrayList<String> province_list = ArrayUtil.convert(ownership);
        // set visited to false
        for (String p : province_list) {
            dist.put(p, Integer.MAX_VALUE);
        }
        return dist;
    }

    private HashMap<String, String> getPred() throws IOException {
        String provinces = Files.readString(Paths.get("src/unsw/gloriaromanus/province_list.json"));
        //System.out.println(provinces);
        JSONArray ownership = new JSONArray(provinces);
        HashMap<String, String> pred = new HashMap<>();
        ArrayList<String> province_list = ArrayUtil.convert(ownership);
        // set visited to false
        for (String p : province_list) {
            pred.put(p, "-");
        }
        return pred;
    }


    private HashMap<String, Boolean> getVisitedMap() throws IOException {
        // add the list into the visited list 
        String provinces = Files.readString(Paths.get("src/unsw/gloriaromanus/province_list.json"));
        //System.out.println(provinces);
        JSONArray ownership = new JSONArray(provinces);
        HashMap<String, Boolean> visited = new HashMap<>();
        ArrayList<String> province_list = ArrayUtil.convert(ownership);
        // set visited to false
        for (String p : province_list) {
            visited.put(p, false);
        }
        return visited;
    }

    public int getDistanceBetweenTwoProvinces(String from, String to, Faction currFaction) throws IOException {
        if (BFS(from, to, currFaction)) {
            return this.dist.get(to);
        }

        // reset arrays
        this.pred = this.getPred();
        this.dist = this.getDist();
        return -1;
    }

    public static void main(String[] args) throws IOException {
        // baetica is connected to lusitania
        Graph g = new Graph();
        String province1 = "Baetica";
        String province2 = "Lusitania";
        Player player = new Player();
        Faction faction = new Faction("Rome", player);
        
        System.out.println(g.getDistanceBetweenTwoProvinces(province1, province2, faction));
       
    }
    
}
