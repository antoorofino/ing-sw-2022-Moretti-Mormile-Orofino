package it.polimi.ingsw.client.cli;

public class CLIGameSettings{

	public static void drawTitle(AnsiColor color, AnsiBackColor backColor) {
		CLIMatrix matrix = new CLIMatrix(200, 20, color, backColor);
		int x = 60;
		int y = 11;
		matrix.drawText(" ▄████████▄   ▄████████▄   ▄█    ▄████████  ███▄▄▄▄       ███     ▄██   ▄      ▄████████",1,y,x);
		matrix.drawText(" ███    ███   ███    ███   ███   ███    ███ ███▀▀▀██▄ ▀█████████▄ ███   ██▄   ███    ███",1,y+1,x);
		matrix.drawText(" ███    █▀    ███    ███   ███   ███    ███ ███   ███    ▀███▀▀██ ███▄▄▄███   ███    █▀ ",1,y+2,x);
		matrix.drawText("▄███▄▄▄      ▄███▄▄▄▄██▀   ███   ███    ███ ███   ███     ███   ▀ ▀▀▀▀▀▀███   ███      ",1,y+3,x);
		matrix.drawText("▀▀███▀▀▀     ▀██████████▄  ███ ▀███████████ ███   ███     ███     ▄██   ███   ▀█████████ ",1,y+4,x);
		matrix.drawText("  ███    █▄   ███     ███  ███   ███    ███ ███   ███     ███     ███   ███          ███ ",1,y+5,x);
		matrix.drawText("  ███    ███  ███     ███  ███   ███    ███ ███   ███     ███     ███   ███    ▄█    ███ ",1,y+6,x);
		matrix.drawText("  ██████████  ███     █▀    ▀█   ███    █▀   ▀█   █▀    ▄█████▀    ▀█████▀   ▄████████▀  ",1,y+7,x);
		matrix.display();
	}

}
