public class Beam {

    private int ogRow;
    private int ogCol;
    private int id;
    private char direction;
    private int length;
    private int minRow;
    private int minCol;
    private int maxRow;
    private int maxCol;

    public Beam(int id, int row, int col, int length, char direction){
        this.id = id;
        this.ogRow = row;
        this.ogCol = col;
        this.length = length;
        this.direction = direction;
    }

    public int getId(){
        return id;
    }
    public int getOgRow(){
        return ogRow;
    }
    public int getOgCol(){
        return ogCol;
    }
    public char getDirection(){
        return direction;
    }
    public int getLength(){
        return length;
    }

    public int getMinRow(){ return this.minRow;}
    public int getMaxRow(){ return this.maxRow;}
    public int getMinCol(){ return this.minCol;}
    public int getMaxCol(){ return this.maxCol;}

    public void setMinRow(int r){
        this.minRow = r;
    }
    public void setMinCol(int c){
        this.minCol = c;
    }
    public void setMaxRow(int r){
        this.maxRow = r;
    }
    public void setMaxCol(int c){
        this.maxCol = c;
    }
}
