public class Beam {

    private int row;
    private int rowF;
    private int col;
    private int colF;
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

    public void setRowF(int rowF){
        this.rowF = rowF;
    }
    public void setColF(int colF){
        this.colF = colF;
    }

    public int getRowF(){
        return rowF;
    }
    public int getColF(){
        return colF;
    }
}
