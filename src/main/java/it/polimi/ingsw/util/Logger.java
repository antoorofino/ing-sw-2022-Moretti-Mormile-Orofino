package it.polimi.ingsw.util;

/**
 * Class used to show messages in console with type and string message
 */
public class Logger {

	private final int level;

	/**
	 * Constructor: build a new log with level of priority
	 * @param level level of priority for the message
	 */
	public Logger(int level){
		this.level = level;
		if (level < 0 )
			throw new RuntimeException("Logger level must be positive");
	}

	/**
	 * If lev < level print type before message
	 * @param lev level of priority for the message
	 * @param type type of message
	 * @param message message
	 */
	public void log(int lev, char type, String message) {
		if (lev <= level)
			log(type, message);
	}

	/**
	 * Print the type before the message
	 * @param type type of message
	 * @param message message
	 */
	public static void log(char type, String message) {
		String typePrefix;
		char color;
		switch (type) {
			case 'f':
				typePrefix = "[FATAL ERROR] ";
				color = 'r';
				break;
			case 'e':
				typePrefix = "[ERROR] ";
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
