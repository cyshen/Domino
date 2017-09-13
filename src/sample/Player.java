package sample;

import java.util.ArrayList;
import java.util.List;
public class Player {
  private String name;
  private List<Domino> dominos = new ArrayList<>();
  public Player(String n){
    name = n;
  }
  public String getName(){
    return name;
  }
  public boolean isEmpty(){
    if (dominos.size() == 0) return true;
    else return false;
  }
  public void addDomino(Domino d)
  {
    dominos.add(d);
  }
  public Domino use(int index)
  {
    Domino select = dominos.remove(index);
    return select;
  }
  public int showDominos(int index, int order)
  {
    return dominos.get(index).showValue()[order];
  }
  public int dominoNum()
  {
    return dominos.size();
  }

  public int[] allValues()
  {
    int[] result = new int[dominos.size()*2];
    for (int i=0;i<dominos.size();i++)
    {
      result[i*2] = showDominos(i,0);
      result[i*2+1] = showDominos(i,1);
    }
    return result;
  }
}
