package sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Player {
    private List<Domino> dominos = new ArrayList<>();
    public void addDomino(Domino d)
    {
        dominos.add(d);
    }
    public Domino use(int index)
    {
        Domino select = dominos.remove(index);
        System.out.println(select.showValue());
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
    public void rotateDomino(int index)
    {
        int[] temp = dominos.get(index).showValue();
        dominos.get(index).setValue(temp[1],temp[0]);
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
