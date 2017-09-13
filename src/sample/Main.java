package sample;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application
{
  public static final boolean DEBUG_MODE = false;
  private int COLUMNS=30;
  private Board board = new Board(COLUMNS);
  private Domino[] dominos = new Domino[28];
  private Boneyard boneyard = new Boneyard();
  private Player user = new Player("user");
  private Player computer = new Player("computer");
  private GridPane grid = new GridPane();
  private Button start, draw;
  private List<Button> hand = new ArrayList<>();;
  private Text title, message, computerCards, userCards;
  private Scene scene;
  private int choose1 = Integer.MIN_VALUE;
  private int choose2 = Integer.MIN_VALUE;
  private int index;
  private int newGame = 1; //1=true, 0=false
  private String  turn = "USER";
  @Override
  public void start(Stage primaryStage)
  {
    //Layout
    //grid.setGridLinesVisible(true);
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(5);
    grid.setVgap(5);
    grid.setPadding(new Insets(25, 25, 25, 25));
    scene = new Scene(grid, 1800, 800);
    scene.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
    primaryStage.setScene(scene);
    start = new Button("Start");
    HBox hbStart = new HBox(start);
    start.getStyleClass().add("options");
    grid.add(hbStart, 8, 3,7,2);
    grid.setHalignment(hbStart, HPos.CENTER);
    draw = new Button("Draw");
    HBox hbDraw = new HBox(draw);
    draw.getStyleClass().add("options");
    grid.add(hbDraw, 15, 3,7,2);
    grid.setHalignment(hbDraw, HPos.CENTER);
    title = new Text("DOMINO");

    HBox hbTitle = new HBox(title);
    hbTitle.setId("title");
    grid.add(hbTitle,8,0,14,3);
    grid.setHalignment(hbTitle, HPos.CENTER);
    message = new Text();
    HBox hbMessage = new HBox(message);
    hbMessage.setId("message");
    grid.add(hbMessage,8,6,14,2);
    userCards = new Text();
    HBox hbUserCards = new HBox(userCards);
    hbUserCards.setId("userCards");
    grid.add(hbUserCards,25,3,5,5);
    computerCards = new Text();
    HBox hbComputerCards = new HBox(computerCards);
    hbComputerCards.setId("computerCards");
    grid.add(hbComputerCards,0,3,5,5);
    setCardsLeft();
    HBox gap1 = new HBox();
    gap1.getStyleClass().add("gap");
    HBox gap2 = new HBox();
    gap2.getStyleClass().add("gap");
    HBox bottom = new HBox();
    grid.add(gap1,0,8,1,1);
    grid.add(gap2,0,12,1,1);
    grid.add(bottom,0,16,1,1);
    final int column = COLUMNS;
    final int row = 2;
    // build the panel
    for (int i=0; i<column; i++) {
      for (int j=0; j<row; j++) {
        Button button = new Button("");
        button.getStyleClass().add("board");
        button.setId(""+j+","+i);
        button.setPrefSize(50, 50);

          button.setOnAction(e->{
            String[] id = button.getId().split(",");
            int[] pos= {Integer.parseInt(id[0]),Integer.parseInt(id[1])};
            if (DEBUG_MODE) System.out.println("position:"+pos[0]+","+pos[1]);
            Button other;
            if (choose1==Integer.MIN_VALUE || choose2==Integer.MIN_VALUE)
            {
              message.setText("Choose a domino first");
            }
            else if(board.isOccupied(pos[0],pos[1]))
            {
              message.setText("Is occupied");
            }
            else if (newGame==1||board.match(pos[0],pos[1],choose1))
            {
              int offset = board.findAdjacent(pos)*2-1; // -1, 1
              if (DEBUG_MODE) System.out.println("the other side: "+pos[0]+","+(pos[1]+offset));
              other = (Button) scene.lookup("#"+pos[0]+","+(pos[1]+offset));
              putDotImage(button,choose1,true);
              putDotImage(other,choose2,true);
              board.addValue(pos[0],pos[1],choose1);
              board.addValue(pos[0],pos[1]+offset,choose2);
              if (newGame==1)
              {
                int[] pos1 = {(pos[0]+1)%2,pos[1]};
                int[] pos2 = {(pos[0]+1)%2,pos[1]+offset};
                board.initEndPosition(pos1,pos2);
                board.initEndValues(choose1, choose2);
              }
              else
              {
                int[] prevPos = {pos[0],pos[1]};
                int[] newPos = {(pos[0]+1)%2,pos[1]+offset};
                board.setEndValues(choose1,choose2);
                board.setEndPositions(prevPos,newPos);
              }

              choose1 = Integer.MIN_VALUE;
              choose2 = Integer.MIN_VALUE;
              message.setText("");
              user.use(index);
              userCards.setText("User\n"+user.dominoNum()+"\nLeft");
              displayDominos(user);
              newGame=0;
              checkEndGame();
              computerPlay();
            }

            else
            {
              message.setText("Illegal position");
            }

          });
          grid.add(button, i, j+10);
      }
    }
    initializeDominos();
    start.setOnAction(e -> {
      if (start.getText()=="Exit") System.exit(0);
      selectDominos(user,7);
      selectDominos(computer,7);
      displayDominos(user);
      setCardsLeft();
      start.setText("Exit");
      primaryStage.show();
    });
    draw.setOnAction(e->{
      if (!draw(user))
      {
        draw.setText("Pass");
        computerPlay();
      }
    });
    primaryStage.setTitle("Domino");
    primaryStage.show();
  }
  public static void main(String[] args) {
        launch(args);
    }
  private void setCardsLeft(){
    userCards.setText("User\n"+user.dominoNum()+"\nLeft");
    computerCards.setText("Computer\n"+computer.dominoNum()+"\nLeft");
  }
  private boolean draw(Player player) {
    if (boneyard.isEmpty())
    {
      message.setText(player.getName()+" Pass");
      return false;
    }
    else
    {
      message.setText(player.getName()+" Draw From Boneyard");
      selectDominos(player,1);
      if(player.getName()=="user") displayDominos(player);
      setCardsLeft();
      checkEndGame();
      return true;
    }
  }
  private void initializeDominos()
  {
    int dominoNum=0;
    for(int i=0;i<7;i++)
    {
      for(int j=0;j<=i;j++)
      {
        dominos[dominoNum] = new Domino();
        dominos[dominoNum].setValue(i,j);
        dominos[dominoNum].setOwner(0);
        boneyard.addDomino(dominos[dominoNum]);
        dominoNum++;
      }
    }
  }
  private void selectDominos(Player player, int numSelect)
  {
    for(int i=0; i<numSelect;i++)
    {
      player.addDomino(boneyard.draw());
    }
  }
  private void putDotImage(Button button, int value, boolean isBoard)
  {
    if (isBoard)button.setStyle("-fx-background-image: url('img/"+value+".png');-fx-background-size: 100%;-fx-background-color:rgb(207,208,205);");
    else button.setStyle("-fx-background-image: url('img/"+value+".png');-fx-background-size: 100%;");
  }
  private void displayDominos(Player player)
  {
    grid.getChildren().removeAll(hand);
    List<Button> temp = new ArrayList<>();
    for (int i=0; i<player.dominoNum(); i++) {
      for (int j=0; j<2; j++) {
        Button button = new Button();
        putDotImage(button,user.showDominos(i,j),false);
        button.setId(""+i+","+j);
        temp.add(button);
        button.getStyleClass().add("hand");
        if (i%2==0)button.getStyleClass().add("hand1");
        else button.getStyleClass().add("hand2");
        button.setPrefSize(50, 50);
        button.setOnAction(e->{
          String[] id = button.getId().split(",");
          choose1 = user.showDominos(Integer.parseInt(id[0]),Integer.parseInt(id[1]));
          choose2 = user.showDominos(Integer.parseInt(id[0]),(Integer.parseInt(id[1])+1)%2);
          index = Integer.parseInt(id[0]);
        });
        grid.add(button, (i*2+j)%COLUMNS, i/15+13);
      }
    }
    hand = temp;
  }
  private boolean checkEndGame()
  {
    if (user.isEmpty()) {
      message.setText("User Wins!");
      return true;
    }
    else if (computer.isEmpty())
    {
      message.setText("Computer Wins!");
      return true;
    }
    else if (boneyard.isEmpty())
    {
      if (!board.canMove(user.allValues()) && !board.canMove(computer.allValues())) {
        message.setText("No More Move, Tie!");
        return true;
      }
    }
    return false;
  }
  private void computerPlay()
  {
    turn = "COMPUTER";
    boolean end = false;
    while(true) {
      if (end==true) break;
      for (int i = 0; i < computer.dominoNum(); i++)
      {
        if (end == true) break;
        for (int j = 0; j < 2; j++)
        {
          if (end == true) break;
          int toTest = computer.showDominos(i, j);
          for (int k = 0; k < 2; k++)
          {
            if (end == true) break;
            int[] toPut = board.getEndPosition()[k];
            if (board.match(toPut[0], toPut[1], toTest))
            {
              int another = computer.showDominos(i, (j + 1) % 2);
              int offset = k * 2 - 1;
              board.addValue(toPut[0], toPut[1], toTest);
              board.setEndValues(toTest, another);
              Button button1 = (Button) scene.lookup("#" + toPut[0] + "," + toPut[1]);
              putDotImage(button1,toTest,true);
              int[] newPos = {(toPut[0] + 1) % 2, toPut[1] + offset};
              board.addValue(toPut[0], toPut[1] + offset, another);
              board.setEndPositions(toPut, newPos);
              Button button2 = (Button) scene.lookup("#" + toPut[0] + "," + (toPut[1] + offset));
              putDotImage(button2,another,true);
              end = true;
              computer.use(i);
              message.setText("");
              setCardsLeft();
            }
          }
        }
      }
      if (end == false)
      {
        if (!draw(computer))end=true;
      }
    }
    if (DEBUG_MODE)
    {
      System.out.print("computer cards: ");
      for (int x : computer.allValues())
      {
        System.out.print(x+" ");
      }
    }
    checkEndGame();
  }
}