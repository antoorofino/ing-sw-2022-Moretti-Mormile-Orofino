package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.cli.util.AnsiBackColor;
import it.polimi.ingsw.client.cli.util.AnsiColor;

/**
 * Creates a matrix of empty characters
 */
public class CLIMatrix {
	int width;
	int height;
	CLIElement[][] elements;

	/**
	 * Constructor: builds empty matrix
	 * @param width matrix width
	 * @param height matrix height
	 * @param color matrix character color
	 * @param backColor matrix backcolor
	 */
	public CLIMatrix(int width, int height, AnsiColor color, AnsiBackColor backColor) {
		this.width = width;
		this.height = height;
		this.elements = new CLIElement[height][width];
		reset(color,backColor);
	}

	/**
	 * Draws matrix on system out
	 */
	public void display(){
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				System.out.print(elements[i][j].backColor.getCode() + elements[i][j].color.getCode() + elements[i][j].symbol);
			}
			System.out.print("\n");
		}
	}

	/**
	 * Initializes the empty matrix
	 * @param color the matrix's character color
	 * @param backColor the matrix backcolor
	 */
	public void reset(AnsiColor color,AnsiBackColor backColor){
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				elements[i][j] = new CLIElement();
				elements[i][j].color = color;
				elements[i][j].backColor = backColor;
			}
		}
	}

	/**
	 * Writes some text inside the character matrix
	 * @param text the text to be written
	 * @param line number of lines of text to do
	 * @param row row of the matrix where to write the text
	 * @param column column of the matrix where to write the text
	 */
	public void drawText(String text,int line,int row,int column){
		int sublength = text.length();
		if(line!=0)
			sublength= text.length()/line;
		for(int i = 0; i < line; i++){
			String part = text.substring(i*sublength, (i + 1)*sublength);
			for(int j=0;j<part.length();j++)
				elements[row + i][column + j].symbol = part.charAt(j);
		}
	}

	/**
	 * Copies character matrix within the current
	 * @param y_anchor x coordinate where to start
	 * @param x_anchor y coordinate where to start
	 * @param copy item to copy
	 */
	public void drawElement(int y_anchor, int x_anchor,CLIMatrix copy){
		for(int i = 0; i < copy.height; i++){
			for(int j = 0; j < copy.width; j++){
				if(copy.elements[i][j].symbol != ' ')
					this.elements[i + y_anchor][j + x_anchor].symbol = copy.elements[i][j].symbol;
				this.elements[i + y_anchor][j + x_anchor].color = copy.elements[i][j].color;
				this.elements[i + y_anchor][j + x_anchor].backColor = copy.elements[i][j].backColor;
			}
		}
	}

	/**
	 * Draws border to the matrix
	 * @param charSet character set to use
	 */
	protected void drawBorder(String charSet){
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (i == 0 && j == 0) {
					elements[i][j].symbol = charSet.charAt(0);
				}
				else if (i == 0 && j == width - 1) {
					elements[i][j].symbol = charSet.charAt(1);
				}
				else if ((i == 0 || i == height - 1) && j > 0 && j < width -1)
				{
					elements[i][j].symbol = charSet.charAt(2);
				}
				else if (i == height - 1 && j == 0) {
					elements[i][j].symbol = charSet.charAt(3);
				}
				else if (i == height - 1 && j == width - 1) {
					elements[i][j].symbol = charSet.charAt(4);
				}
				else if (i > 0 && i < height-1 && (j == 0 || j == width - 1)) {
					elements[i][j].symbol = charSet.charAt(5);
				}
			}
		}
	}

	/**
	 * Draws horizontal line in the matrix
	 * @param row row of the matrix where to write the text
	 * @param leftborder column to start the row from
	 * @param rightborder column in which to end the row
	 * @param charSet character set to use
	 */
	protected void drawLine(int row,int leftborder,int rightborder,String charSet) {
		for (int j = leftborder; j < rightborder; j++) {
			if (j == leftborder) {
				elements[row][j].symbol = charSet.charAt(0);
			} else if (j != rightborder - 1) {
				elements[row][j].symbol = charSet.charAt(1);
			} else {
				elements[row][j].symbol = charSet.charAt(2);
			}
		}
	}

	/**
	 * Draws vertical line in the matrix
	 * @param column column in which to draw the row
	 * @param charSet character set to use
	 */
	protected void drawColumn(int column,String charSet){
		for (int i = 0; i < height; i++) {
			if (i == 0) {
				elements[i][column].symbol = charSet.charAt(0);
			} else if (i == height - 1) {
				elements[i][column].symbol = charSet.charAt(1);
			}else if(elements[i][column].symbol != charSet.charAt(2)){
				if(elements[i][column].symbol != charSet.charAt(3))
					elements[i][column].symbol = charSet.charAt(4);
				else
					elements[i][column].symbol = charSet.charAt(5);
			}
		}
	}
}
