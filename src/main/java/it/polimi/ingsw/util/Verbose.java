package it.polimi.ingsw.util;

public class Verbose {
	boolean log;

	public Verbose(boolean log){
		this.log = log;
	}

	public void printLog(String text){
		if(log)
			System.out.println("LOG: " + text);
	}

	public void printVerbose(String text){
		System.out.println("VERBOSE: " + text);
	}
}
