# Domino
### 101816071 Chih-Yu Shen

### How to use the program
1. At the very beginning, the window displays a 30x2 table as a board to put dominos.
2. Click on "Start" to draw 7 dominos from boneyard for both computer and you. Your dominos are showed on the bottom of the window, and the number of remaining domino shows on the top corner of each side.
3. Click on one of your dominos to select. Then click on one cell from the board to put the domino on. 
4. If the cell you click is not available, there will be a warning message.
5. If you are not able to extend the row, click on "Draw" to draw a domino from boneyard.

### Design structure
- Classes
  - **Main**: Control working flow and display visualized canvas.
  - **Board**: Utilize array of size 30x2 to record the usage of the board. 
  - **Boneyard**: Contains a list of dominos which are free to take, and functions to remove/add/display domino.
  - **Player**: Contain a list of dominos the player has, and functions to remove/add/display domino.
  - **Domino**: A class for a single domino. 
  
  
  
