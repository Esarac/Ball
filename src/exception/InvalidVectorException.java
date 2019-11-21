package exception;

public class InvalidVectorException extends Exception{

	public InvalidVectorException(){
		super("Impossible vector. Only two dimensions.");
	}
	
	public InvalidVectorException(String message){
		super(message);
	}
	
}
