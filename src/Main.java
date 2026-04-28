import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int testCases = Integer.parseInt(br.readLine());
        String[] dimensions = br.readLine().split(" ");
        int rows = Integer.parseInt(dimensions[0]);
        int cols = Integer.parseInt(dimensions[1]);
        String[] chosenAndBeams = br.readLine().split(" ");
        int chosenColumns = Integer.parseInt(chosenAndBeams[0]);
        int startColumn = Integer.parseInt(chosenAndBeams[1]);
        int magicBeams = Integer.parseInt(br.readLine());
        List<List<Integer>> graph = new ArrayList<>(magicBeams);
        Beam[] beams = new Beam[magicBeams];
        for(int i = 0; i < magicBeams; i++){
            graph.add(new LinkedList<>());
        }
        for(int i = 0; i < magicBeams; i++){
            String[] beamInfo = br.readLine().trim().split(" ");
            int r = Integer.parseInt(beamInfo[0]);
            int c = Integer.parseInt(beamInfo[1]);
            int l = Integer.parseInt(beamInfo[2]);
            char d = beamInfo[3].charAt(0);
            Beam beam = new Beam(i,r-1,c-1,l,d);
            beams[i] = beam;
        }
        fillAdjacents(graph, beams);
        for(int i = 0; i < graph.size(); i++){
            System.out.println(graph.get(i).size());
        }
    }

    private static void fillAdjacents(List<List<Integer>> graph, Beam[] beams) {
        for(int i = 0; i < graph.size(); i++){
            Beam b1 = beams[i];
            calcFinalCords(b1);
            for(int j = i+1; j < graph.size(); j++){
                Beam b2 = beams[j];
                calcFinalCords(b2);
                if (hasIntersection(b1, b2)) graph.get(b1.getId()).add(b2.getId());
                if (hasIntersection(b2, b1)) graph.get(b2.getId()).add(b1.getId());
            }
        }
    }

    private static boolean hasIntersection(Beam b1, Beam b2){
        return switch (b1.getDirection()){
            case 'N' -> b1.getRow() > b2.getRow() && ( b1.getCol() < b2.getCol() && b1.getCol() > b2.getColF() || b1.getCol() > b2.getCol() && b1.getCol() < b2.getColF());
            case 'S' -> b1.getRow() < b2.getRow() && ( b1.getCol() < b2.getCol() && b1.getCol() > b2.getColF() || b1.getCol() > b2.getCol() && b1.getCol() < b2.getColF());
            case 'E' -> b1.getCol() < b2.getCol() && (b1.getRow() > b2.getRow() && b1.getRow() < b2.getRowF() || b1.getRow() < b2.getRow() && b1.getRow() > b2.getRowF());
            case 'W' -> b1.getCol() > b2.getCol() && (b1.getRow() > b2.getRow() && b1.getRow() < b2.getRowF() || b1.getRow() < b2.getRow() && b1.getRow() > b2.getRowF());
            default -> false;
        };
    }

    private static void calcFinalCords(Beam b){
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