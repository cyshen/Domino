package sample;

public class Domino {
    private int num1, num2;
    private int owner;
    public Domino() {
    }
    public void setValue(int n1, int n2){
        num1 = n1;
        num2 = n2;
    }
    public void setOwner(int player){ // 0 = boneyard, 1 = player1, 2 = player2
        owner = player;
    }
    public int[] showValue(){
        int[] value = {num1, num2};
        return value;
    }
    public int showOwner(){
        return owner;
    }
}
