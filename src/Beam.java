import beamutils.BeamBoundary;
import beamutils.BeamCoordinates;

/**
 * @Author Francisco Oliveira (67711) & Sérgio Garrido (67202) - P4
 */

/**
 * Beam Class which represents a beam object.
 */
public class Beam {
    // Beam properties
    private int id;
    private char direction;
    private int length;
    private BeamCoordinates coordinates;

    // Boundaries on the grid
    private BeamBoundary beamBoundaries;

    /**
     * Constructor
     * @param id - The id of the beam
     * @param row - The row it starts on
     * @param col - The column it starts on
     * @param length - It's length
     * @param direction - It's direction
     */
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

    /**
     * Sets the beams properties considering it's direction
     * @param minRow - The minimum row of the beam's boundaries
     * @param maxRow - The maximum row of the beam's boundaries
     * @param minCol - The minimum column of the beam's boundaries
     * @param maxCol - The maximum column of the beam's boundaries
     */
    public void setBoundaries(int minRow, int maxRow, int minCol, int maxCol){
        this.beamBoundaries = new BeamBoundary(minRow, maxRow, minCol, maxCol);
    }

    // Boundary settings
    public int getMinRow(){ return this.beamBoundaries.minRow();}
    public int getMaxRow(){ return this.beamBoundaries.maxRow();}
    public int getMinCol(){ return this.beamBoundaries.minCol();}
    public int getMaxCol(){ return this.beamBoundaries.maxCol();}

}
