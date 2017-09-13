package sample;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Boneyard {

  private List<Domino> dominos = new ArrayList<>();
  public boolean isEmpty()
  {
      if(dominos.size()==0) return true;
      else return false;
  }
  public void addDomino(Domino d)
  {
      dominos.add(d);
  }
  public Domino draw()
  {
    Random random = new Random();
    Domino select = dominos.remove(random.nextInt(dominos.size()));
    return select;
  }

}
