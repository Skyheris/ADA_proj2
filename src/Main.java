import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        // Read inputs
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int testCases = Integer.parseInt(br.readLine());

        // Specifications
        String[] dimensions = br.readLine().split(" ");
        int rows = Integer.parseInt(dimensions[0]);
        int cols = Integer.parseInt(dimensions[1]);

        String[] chosenAndBeams = br.readLine().split(" ");
        int chosenColumns = Integer.parseInt(chosenAndBeams[0]);
        int startColumn = Integer.parseInt(chosenAndBeams[1]);

        // Parse Beams
        int magicBeams = Integer.parseInt(br.readLine());
        List<List<Integer>> graph = new ArrayList<>(magicBeams);
        Beam[] beams = new Beam[magicBeams];
        int[] inDegree = new int[magicBeams];
        for(int i = 0; i < magicBeams; i++){
            graph.add(new LinkedList<>());
        }
        for(int i = 0; i < magicBeams; i++){
            String[] beamInfo = br.readLine().trim().split(" ");
            int r = Integer.parseInt(beamInfo[0]);
            int c = Integer.parseInt(beamInfo[1]);
            int l = Integer.parseInt(beamInfo[2]);
            char d = beamInfo[3].charAt(0);
            Beam beam = new Beam(i,r,c,l,d);
            beams[i] = beam;
        }

        // Create graph
        fillAdjacents(graph, beams, inDegree);

        // Get the beams to be released
        List<Integer> chosenBeams = getChosenBeams(chosenColumns, startColumn, beams);

        // If no beams are required then it's a false alarm
        if (chosenBeams.size() == 0){
            System.out.println("False alarm");
            return;
        }

        // Check for cycles
        if (!isAcyclic(graph, inDegree, beams)){
            System.out.println("Disaster");
            return;
        }

        // Debug
        for(int i = 0; i < graph.size(); i++){
            System.out.println(inDegree[i]);
        }
    }

    private static boolean isAcyclic(List<List<Integer>> graph, int[] degree, Beam[] beams) {
        int numProcNodes = 0;
        Queue<Integer> ready = new ArrayDeque<>();
        int[] inDegree = new int[degree.length];

        for (Beam b: beams){
            int id = b.getId();
            inDegree[id] = degree[id];
            if (inDegree[id] == 0) ready.add(id);
        }

        while(!ready.isEmpty()){
            int id = ready.remove();
            numProcNodes++;
            for (int i: graph.get(id)){
                inDegree[i] --;
                if(inDegree[i] == 0) ready.add(i);

            }
        }
        return numProcNodes == graph.size();
    }


    private static List<Integer> getChosenBeams(int chosenColumns, int startColumn, Beam[] beams) {
        List<Integer> chosenBeams = new LinkedList<>();
        int endColumn = startColumn + chosenColumns;
        for(Beam b: beams){
            if (b.getCol() >= startColumn && b.getCol() <= endColumn){
                chosenBeams.add(b.getId());
            }
        }
        return chosenBeams;
    }

    private static void fillAdjacents(List<List<Integer>> graph, Beam[] beams, int[] inDegree) {
        for(int i = 0; i < graph.size(); i++){
            Beam b1 = beams[i];
            calcFinalCords(b1);
            for(int j = i+1; j < graph.size(); j++){
                Beam b2 = beams[j];
                calcFinalCords(b2);
                // If b1 intersects b2 - then b2 blocks b1 therefore b2 must be released before b1. b2 -> b1
                if (hasIntersection(b1, b2)){
                    graph.get(b2.getId()).add(b1.getId());
                    inDegree[b1.getId()]++;
                }
                if (hasIntersection(b2, b1)) {
                    graph.get(b1.getId()).add(b2.getId());
                    inDegree[b2.getId()]++;
                }
            }
        }
    }

    private static boolean hasIntersection(Beam b1, Beam b2){
        return switch (b1.getDirection()){
            case 'N' -> b1.getRow() > b2.getRow() && ( b1.getCol() < b2.getCol() && b1.getCol() > b2.getColF() ||
                    b1.getCol() > b2.getCol() && b1.getCol() < b2.getColF() || b1.getCol() == b2.getCol());
            case 'S' -> b1.getRow() < b2.getRow() && ( b1.getCol() < b2.getCol() && b1.getCol() > b2.getColF() ||
                    b1.getCol() > b2.getCol() && b1.getCol() < b2.getColF() || b1.getCol() == b2.getCol());
            case 'E' -> b1.getCol() < b2.getCol() && (b1.getRow() > b2.getRow() && b1.getRow() < b2.getRowF() ||
                    b1.getRow() < b2.getRow() && b1.getRow() > b2.getRowF() || b1.getRow() == b2.getRow());
            case 'W' -> b1.getCol() > b2.getCol() && (b1.getRow() > b2.getRow() && b1.getRow() < b2.getRowF() ||
                    b1.getRow() < b2.getRow() && b1.getRow() > b2.getRowF() || b1.getRow() == b2.getRow());
            default -> false;
        };
    }

    private static void calcFinalCords(Beam b){
        b.setRowF(b.getRow());
        b.setColF(b.getCol());
        int xi = b.getCol();
        int yi = b.getRow();
        int l = b.getLength();
        switch(b.getDirection()){
            case 'N' -> b.setRowF(yi - l);
            case 'S' -> b.setRowF(yi + l);
            case 'E' -> b.setColF(xi + l);
            case 'W' -> b.setColF(xi - l);
        }
    }
}