package sample;
import com.siyeh.ig.errorhandling.ThrowsRuntimeExceptionInspection;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jetbrains.uast.values.UBooleanConstant;

import java.util.Random;

public class Main extends Application {
    private Board board = new Board();
    private Domino[] dominos = new Domino[28];
    private Boneyard boneyard = new Boneyard();
    private Player user = new Player();
    private Player computer = new Player();
    private int choose1 = Integer.MIN_VALUE;
    private int choose2 = Integer.MIN_VALUE;
    private int index;
    private int newGame = 1; //1=true, 0=false
    private int turn = 1; //1= user, 0=computer
    @Override
    public void start(Stage primaryStage) {
        int panelCol = 21;
        board.init();
        //Layout
        GridPane grid = new GridPane();
        grid.setGridLinesVisible(true);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);

        grid.setPadding(new Insets(25, 25, 25, 25));
        Scene scene = new Scene(grid, 1500, 1000);
        primaryStage.setScene(scene);

        Button start = new Button("Select Dominos");
        HBox hbStart = new HBox(start);
        hbStart.setAlignment(Pos.BOTTOM_LEFT);
        grid.add(hbStart, panelCol, 3,1,1);
        Button draw = new Button("Draw");
        HBox hbDraw = new HBox(draw);
        hbDraw.setAlignment(Pos.BOTTOM_LEFT);
        grid.add(hbDraw, panelCol, 5,1,1);
        Text message = new Text();
        grid.add(message,panelCol,7,1,1);
        Text userCards = new Text(""+user.dominoNum());
        grid.add(userCards,panelCol,8,1,1);
        Text computerCards = new Text();
        grid.add(computerCards,panelCol,9,1,1);
        final int column = 20;
        final int row = 2;
        // build the panel
        for (int i=0; i<column; i++) {
            for (int j=0; j<row; j++) {
                Button button = new Button("");
                button.setId(""+j+","+i);
                button.setPrefSize(50, 50);
                button.setOnAction(e->{
                    String[] id = button.getId().split(",");
                    int r = Integer.parseInt(id[0]);
                    int c = Integer.parseInt(id[1]);
                    Button right = (Button) scene.lookup("#"+r+","+(c+1));
                    if (choose1==Integer.MIN_VALUE || choose2==Integer.MIN_VALUE)
                    {
                        message.setText("Choose a domino first");
                    }
                    else if(board.isOccupied(r,c)||board.isOccupied(r,c+1))
                    {
                        message.setText("Is occupied");
                    }
                    else if (newGame==1||board.match(r,c,choose1)||board.match(r,c+1,choose2))
                    {
                        button.setText(""+choose1);
                        right.setText(""+choose2);
                        board.addValue(r,c,choose1);
                        board.addValue(r,c+1,choose2);
                        if (newGame==1)
                        {
                            int[] pos1 = {(r+1)%2,c};
                            int[] pos2 = {(r+1)%2,c+1};
                            board.initEndPosition(pos1,pos2);
                            board.initEndValues(choose1, choose2);
                            System.out.println("init");
                        }
                        else if(board.match(r,c,choose1))
                        {
                            int[] prevPos = {r,c};
                            int[] newPos = {(r+1)%2,c+1};
                            board.setEndValues(choose1,choose2);
                            board.setEndPositions(prevPos,newPos);
                            System.out.println("choose1");
                        }
                        else if(board.match(r,c,choose2))
                        {
                            int[] prevPos = {r,c};
                            int[] newPos = {(r+1)%2,c-1};
                            board.setEndValues(choose2,choose1);
                            board.setEndPositions(prevPos,newPos);
                            System.out.println("choose2");
                        }
                        choose1 = Integer.MIN_VALUE;
                        choose2 = Integer.MIN_VALUE;
                        message.setText("");
                        user.use(index);
                        userCards.setText(""+user.dominoNum());
                        displayDominos(user,grid);
                        newGame=0;
                        turn = 0;

                        try {
                            computerPlay(scene);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        computerCards.setText(""+computer.dominoNum());
                    }

                    else
                    {
                        message.setText("Illegal position");
                    }

                });
                grid.add(button, i, j);
            }
        }
        initializeDominos();
        start.setOnAction(e -> {
            selectDominos(user,7);
            selectDominos(computer,7);
            displayDominos(user,grid);
            userCards.setText(""+user.dominoNum());
            computerCards.setText(""+computer.dominoNum());
            primaryStage.show();
        });
        draw.setOnAction(e->{
            selectDominos(user,1);
            displayDominos(user,grid);
            userCards.setText(""+user.dominoNum());
        });

        primaryStage.setTitle("Domino");
        primaryStage.show();
    }
    private void initializeDominos(){
        int dominoNum=0;
        for(int i=0;i<7;i++){
            for(int j=0;j<=i;j++){
                dominos[dominoNum] = new Domino();
                dominos[dominoNum].setValue(i,j);
                dominos[dominoNum].setOwner(0);
                boneyard.addDomino(dominos[dominoNum]);
                dominoNum++;
            }
        }
    }
    private void selectDominos(Player player, int numSelect){
        for(int i=0; i<numSelect;i++)
        {
            player.addDomino(boneyard.draw());
        }
    }
    private void displayDominos(Player player, GridPane grid)
    {
        for (int i=0; i<player.dominoNum(); i++) {
            for (int j=0; j<2; j++) {
                Button button = new Button("("+user.showDominos(i,j)+")");
                button.setId(""+i+","+j);
                button.setPrefSize(50, 50);
                button.setOnAction(e->{
                    String[] id = button.getId().split(",");
                    choose1 = user.showDominos(Integer.parseInt(id[0]),Integer.parseInt(id[1]));
                    choose2 = user.showDominos(Integer.parseInt(id[0]),(Integer.parseInt(id[1])+1)%2);
                    index = Integer.parseInt(id[0]);
                });
                grid.add(button, i, 11+j);
            }
            Button rotate = new Button("rotate");
            rotate.setId(""+i);
            rotate.setOnAction(e->{
                user.rotateDomino(Integer.parseInt(rotate.getId()));
                displayDominos(user,grid);
            });
            grid.add(rotate, i, 13);
        }
    }
    private void computerPlay(Scene scene) throws InterruptedException {
        Thread.sleep(1000);
        boolean end = false;
        for (int i=0; i<computer.dominoNum();i++)
        {
            if (end==true) break;
            for (int j=0; j<2;j++){
                if (end==true) break;
                int toTest = computer.showDominos(i,j);
                for (int k=0; k<2;k++)
                {   int[] toPut = board.getEndPosition()[k];
                    if(board.match(toPut[0],toPut[1],toTest))
                    {
                        int another = computer.showDominos(i,(j+1)%2);
                        int offset = k*2-1;
                        board.addValue(toPut[0],toPut[1],toTest);
                        board.setEndValues(toTest,another);
                        Button button1 = (Button) scene.lookup("#"+toPut[0]+","+toPut[1]);
                        button1.setText(""+toTest);
                        int[] newPos = {(toPut[0]+1)%2, toPut[1]+offset};
                        board.addValue(toPut[0],toPut[1]+offset,another);
                        board.setEndPositions(toPut,newPos);
                        Button button2 = (Button) scene.lookup("#"+toPut[0]+","+(toPut[1]+offset));
                        button2.setText(""+another);
                        end=true;
                        computer.use(i);
                    }
                }
                /*
                int[] result = board.search(toTest);
                if(result[0]!=Integer.MIN_VALUE)
                {
                    int[][] toPut = board.posiblePosition(result[0],result[1]);
                    if (toPut[0][0]!=Integer.MIN_VALUE)
                    {System.out.println("4,"+ toPut[0][0]+toPut[0][1]+toPut[1][0]+toPut[1][1]);
                        Button button1 = (Button) scene.lookup("#"+toPut[0][1]+toPut[0][0]);
                        Button button2 = (Button) scene.lookup("#"+toPut[1][1]+toPut[1][0]);
                        button1.setText(""+toTest);
                        button2.setText(""+computer.showDominos(i,(j+1)%2));
                        board.addValue(toPut[0][0],toPut[0][1],toTest);
                        board.addValue(toPut[1][0],toPut[1][1],computer.showDominos(i,(j+1)%2));
                        computer.use(i);
                        end = true;

                    }

                }
                */


            }
        }
        if (end==false) selectDominos(computer,1);
        turn=1;
    }
}