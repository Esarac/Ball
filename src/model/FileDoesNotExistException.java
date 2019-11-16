package model;

public class FileDontExistException extends Exception{
	
	public FileDontExistException(){
		super("The file do not exist. It is impossible to create this Game.");
	}
	
}
