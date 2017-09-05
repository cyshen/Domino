package sample;

import java.util.Arrays;

public class Board {
    private int COL = 20;
    private int[][] values = new int[2][COL];
    private boolean[][] occupied = new boolean[2][COL];
    private int[] endValues = new int[2];
    private int[][] endPositions = new int[2][2];
    private int[][] sides = new int[2][COL]; // [left-top,right-top,left,right,left-bottom,right-bottom]

    public void init()
    {
        for (int i=0; i<2;i++){
            for(int j=0;j<COL;j++){
                values[i][j]=Integer.MIN_VALUE;
                occupied[i][j]=false;
                /*
                sides[i][j]=0;
                if(i==0) sides[i][j]|=42; //101010
                else if(i==9) sides[i][j]|=21; //010101
                if(j==0) sides[i][j]|=48; //110000
                else if(j==9) sides[i][j] = 3;//000011
                */
            }
        }
    }
    public void addValue(int row, int col, int value)
    {
        values[row][col]= value;
        occupied[row][col]=true;
        /*
        sides[row-1][col-1]|=1; //000001
        sides[row+1][col-1]|=2; //000010
        sides[row-1][col]|=4;   //000100
        sides[row+1][col]|=8;   //001000
        sides[row-1][col+1]|=16;//010000
        sides[row+1][col+1]|=32;//100000
        */
    }
    public int getValue(int row, int col)
    {
        return values[row][col];
    }
    public boolean isOccupied(int row, int col)
    {
        return occupied[row][col];
    }
    public boolean match(int row, int col, int value)
    {
        if (value == values[(row+1)%2][col])
        {
            return true;
        }
        else return false;
    }
    public int[] search(int value)
    {
        int[] result = {Integer.MIN_VALUE,Integer.MIN_VALUE};
        for (int i=0;i<10;i++)
        {
            for (int j=0;j<10;j++)
            {
                if (values[i][j]==value)
                {
                    result[0] = i;
                    result[1] = j;
                    return result;
                }
            }
        }
        return result;
    }
    public int[][] posiblePosition(int r, int c)
    { System.out.println("p1,"+sides[r][c]);
        int result[][] = {{Integer.MIN_VALUE, Integer.MIN_VALUE},{Integer.MIN_VALUE, Integer.MIN_VALUE}};
        if((~sides[r][c]&5)==5) //000101
        {
            result = new int[][] {{r+1,c},{r+1,c+1}};
        }
        else if((~sides[r][c]&20)==20) //010100
        {
            result = new int[][] {{r+1,c},{r+1,c-1}};
        }
        else if((~sides[r][c]&10)==10) //001010
        {

            result = new int[][] {{r-1,c},{r-1,c+1}};
        }
        else if((~sides[r][c]&40)==40) //101000
        {
            result = new int[][] {{r-1,c},{r-1,c-1}};
        }
        System.out.println("p2,");
        return result;
    }

    public void setEndPositions(int[] previous, int[] current) {
        for(int i=0; i<2; i++)
        {
            if(endPositions[i]==previous) endPositions[i]=current;

        }
        System.out.println("pos: ["+endPositions[0][0]+","+endPositions[0][1]+"],["+endPositions[1][0]+","+endPositions[1][1]+"]");
    }
    public void initEndPosition(int[] pos1, int[] pos2)
    {
        endPositions[0] = pos1;
        endPositions[1] = pos2;
    }
    public int[][] getEndPosition()
    {
        return endPositions;
    }
    public void setEndValues(int previous, int current) {
        for(int i=0; i<2; i++)
        {
            if(endValues[i]==previous)
            {
                endValues[i]=current;
                System.out.println("values: "+endValues[0]+","+endValues[1]);
                break;
            }

        }
    }
    public void initEndValues(int value1, int value2)
    {
        endValues[0]=value1;
        endValues[1]=value2;
    }
}
