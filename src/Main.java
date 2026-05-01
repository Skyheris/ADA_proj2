import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        // Read inputs
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int testCases = Integer.parseInt(br.readLine());

        for(int t = 0; t < testCases; t++){
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
                calcFinalCords(beam);
            }

            // Create graph
            fillAdjacents(graph, beams, inDegree);

            // Get the beams to be released
            boolean[] isNecessary = new boolean[magicBeams];
            List<Integer> necessaryList = getNecessaryBeams(chosenColumns, startColumn, beams, graph, isNecessary);

            // If no beams are required then it's a false alarm
            if (necessaryList.isEmpty()){
                System.out.println("False alarm");
                continue;
            }

            // Resolve the problem
            List<Integer> result = topologicalSort(graph, inDegree, isNecessary, magicBeams);

            // If result is null then we found a loop
            if (result == null){
                System.out.println("Disaster");
            // Otherwise output the permutation that solves the problem
            } else {
                for (int i = 0; i < result.size(); i++) {
                    System.out.print(result.get(i) + (i == result.size() - 1 ? "" : " "));
                }
                System.out.println();
            }
        }
    }

    private static List<Integer> topologicalSort(List<List<Integer>> graph, int[] inDegree, boolean[] isNecessary, int magicBeams) {
        // PriorityQueue to guarantee minimum id is processed first
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        int processedCount = 0;

        // Start the queue with every beam that has degree 0, meaning does not block anyone
        for (int i = 0; i < magicBeams; i++) {
            if (inDegree[i] == 0) {
                pq.add(i);
            }
        }

        List<Integer> result = new ArrayList<>();

        // Process graph
        while (!pq.isEmpty()) {
            int node = pq.poll();
            processedCount++;
            // If the beam is necessary then we add it to the result
            if (isNecessary[node]) {
                // Use id+1 to prepare output
                result.add(node + 1);
            }
            for (int v : graph.get(node)) {
                inDegree[v]--;
                if (inDegree[v] == 0) {
                    pq.add(v);
                }
            }
        }
        if (processedCount < magicBeams){
            return null;
        }
        return result;
    }

    private static List<Integer> getNecessaryBeams(int chosenColumns, int startColumn, Beam[] beams, List<List<Integer>> graph, boolean[] isNecessary) {
        List<Integer> necessary = new LinkedList<>();

        // Defining boundaries of the chosen interval
        int endColumn = startColumn + chosenColumns - 1;

        // Check who is already on the interval
        for (Beam b : beams) {
            int id = b.getId();
            // A beam touches the interval if it's minCol <= endInterval and if their maxCol >= startInterval
            if (b.getMinCol() <= endColumn && b.getMaxCol() >= startColumn) {
                if (!isNecessary[id]){
                    necessary.add(id);
                    isNecessary[id] = true;
                }
            }
        }

        // If A is necessary and B blocks A. B is also necessary
        // We will use BFS to check
        boolean changed = true;
        while (changed) {
            changed = false;
            for (Beam b : beams) {
                int potentialObstacle = b.getId();
                // If this beam is not yet necessary we check if it blocks a necessary beam
                if (!isNecessary[potentialObstacle]) {
                    for (int targetId : graph.get(potentialObstacle)) {
                        // If targetId is already necessary then potentialObstacle is necessary aswell
                        if (isNecessary[targetId]) {
                            necessary.add(potentialObstacle);
                            isNecessary[potentialObstacle] = true;
                            changed = true;
                            // Early prune since we already have the info we need
                            break;
                        }
                    }
                }
            }
        }
        return necessary;
    }

    private static void fillAdjacents(List<List<Integer>> graph, Beam[] beams, int[] inDegree) {
        for(int i = 0; i < beams.length; i++){
            Beam b1 = beams[i];
            for(int j = i+1; j < beams.length; j++){
                Beam b2 = beams[j];
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

    private static boolean hasIntersection(Beam a, Beam b){
        return switch (a.getDirection()){
            case 'N' -> a.getOgCol() >= b.getMinCol() && a.getOgCol() <= b.getMaxCol() && a.getOgRow() > b.getMaxRow();
            case 'S' -> a.getOgCol() >= b.getMinCol() && a.getOgCol() <= b.getMaxCol() && a.getOgRow() < b.getMinRow();
            case 'E' -> a.getOgRow() >= b.getMinRow() && a.getOgRow() <= b.getMaxRow() && a.getOgCol() < b.getMinCol();
            case 'W' -> a.getOgRow() >= b.getMinRow() && a.getOgRow() <= b.getMaxRow() && a.getOgCol() > b.getMaxCol();
            default -> false;
        };
    }

    private static void calcFinalCords(Beam b){
        int xi = b.getOgCol();
        int yi = b.getOgRow();
        int l = b.getLength();
        switch(b.getDirection()){
            case 'N' ->{
                b.setMaxRow(yi);
                b.setMinRow(yi - l + 1);
                b.setMinCol(xi);
                b.setMaxCol(xi);
            }
            case 'S' -> {
                b.setMinRow(yi);
                b.setMaxRow(yi + l - 1);
                b.setMinCol(xi);
                b.setMaxCol(xi);
            }
            case 'E' -> {
                b.setMinCol(xi);
                b.setMaxCol(xi + l - 1);
                b.setMinRow(yi);
                b.setMaxRow(yi);
            }
            case 'W' -> {
                b.setMinCol(xi - l + 1);
                b.setMaxCol(xi);
                b.setMinRow(yi);
                b.setMaxRow(yi);
            }
        }
    }
}