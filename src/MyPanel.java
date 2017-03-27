import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Random;

import javax.swing.JPanel;

public class MyPanel extends JPanel {
	private static final long serialVersionUID = 3426940946811133635L;
	private static final int GRID_X = 25;
	private static final int GRID_Y = 25;
	private static final int INNER_CELL_SIZE = 29;
	private static final int TOTAL_COLUMNS = 9;
	private static final int TOTAL_ROWS = 10;   //Last row has only one cell
	public int x = -1;
	public int y = -1;
	public int mouseDownGridX = 0;
	public int mouseDownGridY = 0;
	private static char minefield[][];
	public Color[][] colorArray = new Color[TOTAL_COLUMNS][TOTAL_ROWS];
	public MyPanel() {   //This is the constructor... this code runs first to initialize
		if (INNER_CELL_SIZE + (new Random()).nextInt(1) < 1) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("INNER_CELL_SIZE must be positive!");
		}
		if (TOTAL_COLUMNS + (new Random()).nextInt(1) < 2) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_COLUMNS must be at least 2!");
		}
		if (TOTAL_ROWS + (new Random()).nextInt(1) < 3) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_ROWS must be at least 3!");
		}
		for (int x = 0; x < TOTAL_COLUMNS; x++) {   //Top row
			colorArray[x][0] = Color.LIGHT_GRAY;
		}
		for (int y = 0; y < TOTAL_ROWS; y++) {   //Left column
			colorArray[0][y] = Color.LIGHT_GRAY;
		}
		for (int x = 1; x < TOTAL_COLUMNS; x++) {   //The rest of the grid
			for (int y = 1; y < TOTAL_ROWS; y++) {
				colorArray[x][y] = Color.LIGHT_GRAY;
			}
		}
		minefield = new char [TOTAL_COLUMNS][TOTAL_ROWS];
	}
	
	Random rando = new Random();
	public static int mines = 10;
	public int flags = 10;
	public static int flagged = 0;
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//Compute interior coordinates
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		int x2 = getWidth() - myInsets.right - 1;
		int y2 = getHeight() - myInsets.bottom - 1;
		int width = x2 - x1;
		int height = y2 - y1;

		//Paint the background
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(x1, y1, width + 1, height + 1);

		//Draw the grid minus the bottom row (which has only one cell)
		//By default, the grid will be 10x10 (see above: TOTAL_COLUMNS and TOTAL_ROWS) 
		g.setColor(Color.BLACK);
		for (int y = 0; y <= TOTAL_ROWS - 1; y++) {
			g.drawLine(x1 + GRID_X, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)), x1 + GRID_X + ((INNER_CELL_SIZE + 1) * TOTAL_COLUMNS), y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)));
		}
		for (int x = 0; x <= TOTAL_COLUMNS; x++) {
			g.drawLine(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y, x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y + ((INNER_CELL_SIZE + 1) * (TOTAL_ROWS - 1)));
		}
		
		//Paint cell colors
		for (int x = 0; x < TOTAL_COLUMNS; x++) {
			for (int y = 0; y < TOTAL_ROWS; y++) {
				if ((x == 0) || (y != TOTAL_ROWS - 1)) {
					Color c = colorArray[x][y];
					g.setColor(c);
					g.fillRect(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 1, INNER_CELL_SIZE, INNER_CELL_SIZE);
				}
			}
		}
	}
	
	// Places the mines in the field
	public void placeMines() {
		int minesPlaced = 1; 
		while (minesPlaced <= mines) {
			int x = rando.nextInt(TOTAL_COLUMNS);
			int y = rando.nextInt(TOTAL_ROWS-1);
			if (minefield[x][y] != '*') {
				minefield[x][y] = '*';
				minesPlaced++;
			}
		}
		for (int i=0; i<9; i++) {
			for (int j=0; j<9; j++) {
				bombCheck(i, j);
				if (bombCheck(i, j) == 1) {
					System.out.println(i + "," + j);
				}
			}
		}repaint();
	}
	
	public void check (int x, int y) {
		colorArray[x][y] = Color.WHITE ;
		repaint();
	}
	
	// Checks whether this place in the field has a bomb (1) or not (0).
	public int bombCheck(int x, int y) {
		if (!(x == -1 || y == -1)) {
			if (minefield[x][y] == '*') {
				return 1;
			}
			else {
				minefield[x][y] = 'c';
				return 0;
			}
		}
		else{
			return 0;
		}
	}
	
	// Checks for mines on the 8 other tiles around the target location and returns the number of mines there are. 
	public int minesAround(int x, int y) {
		int mines = 0;
		mines += bombCheck(x-1, y-1);
		mines += bombCheck(x-1, y);
		mines += bombCheck(x-1, y+1);
		mines += bombCheck(x, y-1);
		mines += bombCheck(x, y+1);
		mines += bombCheck(x+1, y-1);
		mines += bombCheck(x+1, y);
		mines += bombCheck(x+1, y+1);
		if (mines > 0) {
			return mines;
		}
		else{
			return 0;
		}
	}
		
	public void checkAround(int x, int y) {
		int minx, miny, maxx, maxy;
		check(x,y);
		minx = (x <= 0 ? 0 : x - 1);
		miny = (y <= 0 ? 0 : y - 1);
		maxx = (x >= TOTAL_COLUMNS - 1 ? TOTAL_COLUMNS - 1 : x + 1);
		maxy = (y >= TOTAL_ROWS - 2 ? TOTAL_ROWS - 2 : y + 1);
		for (int i = minx; i < maxx; i ++) {
			for (int j = miny; j <= maxy; j ++) {
					if (bombCheck(i,j) == 0 && colorArray[i][j] != Color.WHITE) {
						check(i,j);
						if (minesAround(i,j) == 0) {
							checkAround(i,j);
						}
					}
				}
			}
		}
	
	public int checkflag(int x, int y){
		int status = 0;
		if (!(x == -1 || y == -1)) {
			if (colorArray[x][y] == Color.RED) {
				status += 1;
			}else {
				status += 0;
			}
		}
		return status;
	}
	
	public void reset() {
		for (int i = 0; i < TOTAL_COLUMNS; i++) {
			for (int j = 0 ;j < TOTAL_ROWS; j++) {
				colorArray[i][j] = Color.LIGHT_GRAY;
 				minefield[i][j] = ' ';
				MyMouseAdapter.f = 1;
				repaint();
			}
		}
		placeMines();
	}

	
	public int getGridX(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x == 0 && y == TOTAL_ROWS - 1) {    //The lower left extra cell
			return x;
		}
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 2) {   //Outside the rest of the grid
			return -1;
		}
		return x;
	}
	public int getGridY(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x == 0 && y == TOTAL_ROWS - 1) {    //The lower left extra cell
			return y;
		}
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 2) {   //Outside the rest of the grid
			return -1;
		}
		return y;
	}
}