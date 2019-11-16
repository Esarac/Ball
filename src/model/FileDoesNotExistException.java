package model;

public class FileDoesNotExistException extends Exception{

	public FileDoesNotExistException(){
		super("The file do not exist. It is impossible to create this Game.");
	}
	
}
