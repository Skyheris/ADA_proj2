public class Beam {

    private int row;
    private int col;
    private int id;
    private char direction;
    private int length;

    public Beam(int id, int row, int col, int length, char direction){
        this.id = id;
        this.row = row;
        this.col = col;
        this.length = length;
        this.direction = direction;

    }

    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }
    public char getDirection(){
        return direction;
    }
    public int getLength(){
        return length;
    }
    public int getId(){
        return id;
    }
}
