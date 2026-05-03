import beamutils.BeamBoundary;
import beamutils.BeamCoordinates;

/**
 * @Author Francisco Oliveira (67711) & Sérgio Garrido (67202) - P4
 */

public class Beam {
    private int id;
    private BeamCoordinates coordinates;
    private char direction;
    private int length;

    // Boundaries
    private BeamBoundary beamBoundaries;

    public Beam(int id, int row, int col, int length, char direction){
        this.id = id;
        this.coordinates = new BeamCoordinates(col, row);
        this.length = length;
        this.direction = direction;
    }

    // Default settings
    public int getId(){
        return id;
    }
    public int getSrcRow(){
        return coordinates.row();
    }
    public int getSrcCol(){
        return coordinates.col();
    }
    public char getDirection(){
        return direction;
    }
    public int getLength(){
        return length;
    }

    public void setBoundaries(int minRow, int maxRow, int minCol, int maxCol){
        this.beamBoundaries = new BeamBoundary(minRow, maxRow, minCol, maxCol);
    }
    // Boundary settings
    public int getMinRow(){ return this.beamBoundaries.minRow();}
    public int getMaxRow(){ return this.beamBoundaries.maxRow();}
    public int getMinCol(){ return this.beamBoundaries.minCol();}
    public int getMaxCol(){ return this.beamBoundaries.maxCol();}

}
