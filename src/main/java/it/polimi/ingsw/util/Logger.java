package it.polimi.ingsw.util;

public class Logger {

	private final int level;

	public Logger(int level){
		this.level = level;
		if (level <= 0 )
			throw new RuntimeException("Logger level must be positive");
	}

	public void log(int lev, char type, String message) {
		if (lev <= level)
			log(type, message);
	}

	public static void log(char type, String message) {
		String typePrefix;
		char color;
		switch (type) {
			case 'f':
				typePrefix = "[FATAL ERROR] ";
				color = 'r';
				break;
			case 'w':
				typePrefix = "[WARNING] ";
				color = 'y';
				break;
			case 'g':
				typePrefix = "[INFO-GAME] ";
				color = 'b';
				break;
			default:
				typePrefix = "[INFO] ";
				color = 'd';
				break;
		}
		System.out.println(CLIFrmt.print(color, typePrefix + message));
	}
}
