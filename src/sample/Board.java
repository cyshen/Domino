package sample;

import java.util.Arrays;

public class Board {
  private int COL;
  private int[][] values;
  private boolean[][] occupied;
  private int[] endValues;
  private int[][] endPositions;

  public Board(int columns)
  {
    COL = columns;
    values = new int[2][COL];
    occupied = new boolean[2][COL];
    endValues = new int[2];
    endPositions = new int[2][2];
    for (int i=0; i<2;i++){
      for(int j=0;j<COL;j++){
        values[i][j]=Integer.MIN_VALUE;
        occupied[i][j]=false;
      }
    }
  }
  public void addValue(int row, int col, int value)
  {
      values[row][col]= value;
      occupied[row][col]=true;
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
      else if(value == 0 && values[(row+1)%2][col]!=Integer.MIN_VALUE)
      {
          return true;
      }
      else if (values[(row+1)%2][col]==0)
      {
          return true;
      }
      else return false;
  }
  public void setEndPositions(int[] previous, int[] current) {
    for(int i=0; i<2; i++)
    {
      if(endPositions[i][0]==previous[0] && endPositions[i][1]==previous[1])
      {
        endPositions[i]=current;
      }
    }
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
  public int findAdjacent(int[] pos)
  {
    for(int i=0;i<2;i++)
    {
      if (endPositions[i][0] == pos[0] && endPositions[i][1]==pos[1]) return i; //0 : left, 1: right
    }
        return 1;
  }

  public void setEndValues(int previous, int current) {
    for(int i=0; i<2; i++)
    {
      if(endValues[i]==previous)
      {
        endValues[i]=current;
        break;
      }

    }
  }
  public void initEndValues(int value1, int value2)
  {
    endValues[0]=value1;
    endValues[1]=value2;
  }
  public boolean canMove(int[] values)
  {
    for (int i : values)
    {
      if (endValues[0]==i || endValues[1]==i) return true;
    }
    return false;
  }
}
