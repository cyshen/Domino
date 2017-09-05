package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class SimpleTextInputFX_WithSyntexChecking extends Application implements EventHandler<ActionEvent>
{
    private Text labelDescription = new Text("Welcome To Crypto with Friends! \nPlayer " +
            "One enters a message and an encryption key. Then I will use the key " +
            "to encrypt the message with a substitution cypher for player two to decrypt.");
    private Label labelInput = new Label("Enter Message:");

    private Label labelErrorMsg = new Label();
    private Label labelErrorKey = new Label();
    private TextField textMsg = new TextField();
    private TextField textKey = new TextField();

    private boolean msgGood = false;
    private boolean keyGood = false;

    private Button buttonSubmitMsg;

    private static final int MAX_MSG_LENGTH = 150;
    private static final int MIN_KEY_LENGTH = 3;
    private static final int MAX_KEY_LENGTH = 10;
    private static final int MIN_WORD_COUNT = 7;
    private static final int FONT_SIZE = 28;

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 500;

    @Override
    public void start(Stage stage) throws Exception
    {
        buttonSubmitMsg = new Button("Encrypt");
        buttonSubmitMsg.setOnAction(this);
        buttonSubmitMsg.setDisable(true);

        textMsg.textProperty().addListener((observable) -> update());
        textKey.textProperty().addListener((observable) -> update());


        labelDescription.setFont(Font.font("Verdana", FONT_SIZE));
        labelInput.setFont(Font.font("Verdana", FONT_SIZE));

        labelDescription.setWrappingWidth(WINDOW_WIDTH - 20);

        labelErrorMsg.setFont(Font.font("Verdana", FONT_SIZE));
        labelErrorKey.setFont(Font.font("Verdana", FONT_SIZE));
        textMsg.setFont(Font.font("Verdana", FONT_SIZE));
        textKey.setFont(Font.font("Verdana", FONT_SIZE));

        labelErrorMsg.setTextFill(Color.RED);
        labelErrorKey.setTextFill(Color.RED);


        Tooltip msgToolTip = new Tooltip(
                "Must have at least " + MIN_WORD_COUNT + " words.\n" +
                        "Must be at least one full sentence.\n" +
                        "Must be under " + MAX_MSG_LENGTH +" characters."
        );
        msgToolTip.setStyle("-fx-background-radius: 7 7 7 7;");
        Tooltip.install(textMsg, msgToolTip);


        VBox vBox = new VBox();
        vBox.getChildren().addAll(labelDescription, labelInput,
                textMsg, labelErrorMsg, textKey, labelErrorKey,
                buttonSubmitMsg);

        Scene scene = new Scene(vBox, WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.setScene(scene);
        stage.show();
    }

    private void submitMsg()
    {

    }


    //============================================================================================
    // Called by JavaFX when the user clicks a button.
    //============================================================================================
    @Override
    public void handle(ActionEvent event)
    {
        Object source = event.getSource();

        if (source == buttonSubmitMsg) submitMsg();
    }

    private boolean updateTextField(String errStr, TextField field)
    {
        if (errStr.length() == 0)
        {
            field.setStyle("-fx-text-inner-color: black;");
            return true;
        }
        else
        {
            field.setStyle("-fx-text-inner-color: red;");
            return false;
        }
    }



    private void update()
    {
        String msgResult = checkTextMsg();
        labelErrorMsg.setText(msgResult);
        msgGood = updateTextField(msgResult, textMsg);


        String keyResult = checkTextKey();
        labelErrorKey.setText(keyResult);
        keyGood = updateTextField(keyResult, textKey);


        if (msgGood && keyGood) buttonSubmitMsg.setDisable(false);
        else buttonSubmitMsg.setDisable(true);
    }

    private String checkTextMsg()
    {
        String originalMessage = textMsg.getText();
        boolean inWord = false;
        int wordCount = 0;
        if (originalMessage.length() != 0) //sries of if checks to make sure the message meets proper criteria
        {
            if (originalMessage.length() <= MAX_MSG_LENGTH)
            {
                char end = originalMessage.charAt(originalMessage.length() - 1);
                if (end == '.' || end == '?' || end == '!')
                {
                    for (int i = 0; i < originalMessage.length(); i++)
                    {
                        char c = originalMessage.charAt(i);
                        if (!inWord && Character.isLetter(c))
                        {
                            inWord = true;
                            wordCount++;
                        }
                        if (inWord && Character.isSpaceChar(c))
                        {
                            inWord = false;
                        }
                        if (!Character.isLetter(c))
                        {
                            if (c == '!' || c == ',' || c == '.' || c == '?' || c == '-' || c == ':' || c == ';' || c == '('
                                    || c == ')' || c == '[' || c == ']' || c == '/' || c == '"' || c == 39 || c == 32) { ; }
                            else
                            {
                                return "invalid character";
                            }
                        }
                    } //end of for loop through originalMessage
                    if (wordCount < MIN_WORD_COUNT) return "not enough words";
                    else return "";
                }
                return "invalid ending punctuation";
            }
            return "to many characters " + originalMessage.length();
        }
        return "empty string";
    }

    private String checkTextKey()
    {
        String key = textKey.getText();
        System.out.println("checkTextKey(): key=" + key);

        if (key.length() <= MAX_KEY_LENGTH && key.length() > MIN_KEY_LENGTH) //checks key criteria
        {
            for (int i = 0; i < key.length(); i++)
            {
                char c = key.charAt(i);
                if (!Character.isLetter(c))
                {
                    return "contains invalid characters";
                }
            }
            return "";

        }
        return "key length invalid";
    }


    public static void main(String[] args)
    {
        launch(args);
    }
}