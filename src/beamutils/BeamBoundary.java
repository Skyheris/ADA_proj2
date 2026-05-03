package beamutils;

/**
 * @Author Francisco Oliveira (67711) & Sérgio Garrido (67202) - P4
 */


/**
 * Record defining the beam's boundary
 * @param minRow - The minimum row of the beam's boundaries
 * @param maxRow - The maximum row of the beam's boundaries
 * @param minCol - The minimum column of the beam's boundaries
 * @param maxCol - The maximum column of the beam's boundaries
 */
public record BeamBoundary(int minRow, int maxRow, int minCol, int maxCol) {
    
}
