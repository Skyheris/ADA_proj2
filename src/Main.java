import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        String firstLine = br.readLine();
        
        int testCases = Integer.parseInt(firstLine.trim());
        StringBuilder out = new StringBuilder();

        for (int t = 0; t < testCases; t++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int rows = Integer.parseInt(st.nextToken());
            int cols = Integer.parseInt(st.nextToken());

            st = new StringTokenizer(br.readLine());
            int chosenColumns = Integer.parseInt(st.nextToken());
            int startColumn = Integer.parseInt(st.nextToken());

            int magicBeams = Integer.parseInt(br.readLine().trim());
            Beam[] beams = new Beam[magicBeams];

            for (int i = 0; i < magicBeams; i++) {
                st = new StringTokenizer(br.readLine());
                int r = Integer.parseInt(st.nextToken());
                int c = Integer.parseInt(st.nextToken());
                int l = Integer.parseInt(st.nextToken());
                char d = st.nextToken().charAt(0);
                
                beams[i] = new Beam(i, r, c, l, d);
            }

            Solver solver = new Solver(rows, cols, chosenColumns, startColumn, magicBeams, beams);
            out.append(solver.solve()).append("\n");
        }

        System.out.print(out.toString());
    }
}