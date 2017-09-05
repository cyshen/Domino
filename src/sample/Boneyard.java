package sample;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Boneyard {

    private List<Domino> dominos = new ArrayList<>();
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
    public void showDominos()
    {
        for(int i=0;i<dominos.size();i++)
        {
            System.out.println(dominos.get(i).showValue()[0]);
            System.out.print(dominos.get(i).showValue()[1]);
        }
    }
}
