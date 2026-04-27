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
        int[][] beamGrid = new int[rows][cols];
        List<List<Beam>> beams = new ArrayList<>(magicBeams);
        for(int i = 0; i < magicBeams; i++){
            beams.add(new LinkedList<>());
        }
        for(int i = 0; i < magicBeams; i++){
            String[] beamInfo = br.readLine().trim().split(" ");
            int r = Integer.parseInt(beamInfo[0]);
            int c = Integer.parseInt(beamInfo[1]);
            int l = Integer.parseInt(beamInfo[2]);
            char d = beamInfo[3].charAt(0);
            Beam beam = new Beam(i+1,r-1,c-1,l,d);
            beams.get(i).add(beam);
            fillGrid(beamGrid,beam, true);
        }
        for (int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                System.out.print(beamGrid[i][j]);
            }
            System.out.println();
        }
    }

    private static void fillGrid(int[][] beamGrid, Beam beam, boolean remove) {
        int dI = 0;
        int dJ = 0;
        switch(beam.getDirection()){
            case 'N' -> dI = -1;
            case 'S' -> dI = 1;
            case 'E' -> dJ = 1;
            case 'W' -> dJ = -1;
        }
        int i = beam.getRow();
        int j = beam.getCol();
        int counter = beam.getLength();
        while(counter > 0){
            if(remove)
                beamGrid[i][j] = 0;
            else
                beamGrid[i][j] = beam.getId();
            i += dI;
            j += dJ;
            counter--;
        }
    }
}