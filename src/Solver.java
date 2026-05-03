import java.util.*;

public class Solver {
    private final int rows;
    private final int cols;
    private final int chosenColumns;
    private final int startColumn;
    private final int magicBeams;
    private final Beam[] beams;

    public Solver(int rows, int cols, int chosenColumns, int startColumn, int magicBeams, Beam[] beams) {
        this.rows = rows;
        this.cols = cols;
        this.chosenColumns = chosenColumns;
        this.startColumn = startColumn;
        this.magicBeams = magicBeams;
        this.beams = beams;
    }

    public String solve() {
        List<List<Integer>> graph = new ArrayList<>(magicBeams);
        int[] inDegree = new int[magicBeams];
        
        for (int i = 0; i < magicBeams; i++) {
            graph.add(new LinkedList<>());
            calcFinalCords(beams[i]);
        }

        fillAdjacents(graph, beams, inDegree, rows, cols);

        boolean[] isNecessary = new boolean[magicBeams];
        int necessaryCount = getNecessaryBeams(chosenColumns, startColumn, beams, graph, isNecessary);

        if (necessaryCount == 0) {
            return "False alarm";
        }

        List<Integer> result = topologicalSort(graph, inDegree, isNecessary, necessaryCount);

        if (result == null) {
            return "Disaster";
        } 
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.size(); i++) {
            sb.append(result.get(i));
            if (i < result.size() - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private List<Integer> topologicalSort(List<List<Integer>> graph, int[] inDegree, boolean[] isNecessary, int totalNecessary) {
        // PriorityQueue to guarantee minimum id is processed first
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        
        // Only add necessary beams with 0 in-degree to the starting queue
        for (int i = 0; i < isNecessary.length; i++) {
            if (isNecessary[i] && inDegree[i] == 0) {
                pq.add(i);
            }
        }

        List<Integer> result = new ArrayList<>();

        while (!pq.isEmpty()) {
            int node = pq.poll();
            result.add(node + 1);

            for (int v : graph.get(node)) {
                // Only care about dependencies between necessary beams
                if (isNecessary[v]) {
                    inDegree[v]--;
                    if (inDegree[v] == 0) {
                        pq.add(v);
                    }
                }
            }
        }
        
        // If we processed fewer beams than required, a cycle exists among necessary beams
        if (result.size() < totalNecessary) {
            return null;
        }
        return result;
    }

    private int getNecessaryBeams(int chosenColumns, int startColumn, Beam[] beams, List<List<Integer>> graph, boolean[] isNecessary) {
        int endColumn = startColumn + chosenColumns - 1;

        // Check who is already on the interval
        for (Beam b : beams) {
            int id = b.getId();
            if (b.getMinCol() <= endColumn && b.getMaxCol() >= startColumn) {
                isNecessary[id] = true;
            }
        }

        // Propagate necessity using the graph relations
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int i = 0; i < beams.length; i++) {
                if (!isNecessary[i]) {
                    for (int targetId : graph.get(i)) {
                        // If beam 'i' blocks a beam that is already necessary, 'i' becomes necessary
                        if (isNecessary[targetId]) {
                            isNecessary[i] = true;
                            changed = true;
                            break;
                        }
                    }
                }
            }
        }

        // Count total beams that must be removed for result validation
        int count = 0;
        for (boolean b : isNecessary) if (b) count++;
        return count;
    }

    private void fillAdjacents(List<List<Integer>> graph, Beam[] beams, int[] inDegree, int rows, int cols) {
        // fill the grid with the beams
        int[][] grid = createOccupancyGrid(rows, cols, beams);

        // look who is blocking who
        for (Beam b : beams) {
            traceBeamPath(b, grid, graph, inDegree, rows, cols);
        }
    }

    private int[][] createOccupancyGrid(int rows, int cols, Beam[] beams) {
        int[][] grid = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(grid[i], -1);
        }

        for (Beam b : beams) {
            // put the beams in the grid to fast looking on intersection
            for (int r = b.getMinRow(); r <= b.getMaxRow(); r++) {
                for (int c = b.getMinCol(); c <= b.getMaxCol(); c++) {
                    grid[r][c] = b.getId();
                }
            }
        }
        return grid;
    }

    private void traceBeamPath(Beam b, int[][] grid, List<List<Integer>> graph, int[] inDegree, int rows, int cols) {
        int dr = 0, dc = 0;
        // see the direction of the beam and choose the direction to go look for blockers
        switch (b.getDirection()) {
            case 'N' -> dr = -1;
            case 'S' -> dr = 1;
            case 'E' -> dc = 1;
            case 'W' -> dc = -1;
        }

        // go to the edge of the beam
        int r = b.getSrcRow();
        int c = b.getSrcCol();
        while (r >= b.getMinRow() && r <= b.getMaxRow() && c >= b.getMinCol() && c <= b.getMaxCol()) {
            r += dr;
            c += dc;
        }

        Set<Integer> seenBlockers = new HashSet<>();
        // if a beam is found, put the beam on the blocker list (sucessor)
        while (r >= 0 && r < rows && c >= 0 && c < cols) {
            int blockerId = grid[r][c];
            if (blockerId != -1 && blockerId != b.getId()) {
                if (seenBlockers.add(blockerId)) {
                    graph.get(blockerId).add(b.getId());
                    inDegree[b.getId()]++;
                }
            }
            r += dr;
            c += dc;
        }
    }

    private void calcFinalCords(Beam b) {
        int xi = b.getSrcCol();
        int yi = b.getSrcRow();
        int l = b.getLength();
        switch (b.getDirection()) {
            case 'N' -> b.setBoundaries(yi - l + 1, yi, xi, xi);
            case 'S' -> b.setBoundaries(yi, yi + l - 1, xi, xi);
            case 'E' -> b.setBoundaries(yi, yi, xi, xi + l - 1);
            case 'W' -> b.setBoundaries(yi, yi, xi - l + 1, xi);
        }
    }
}