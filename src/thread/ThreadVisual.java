package thread;

import controller.ControlGame;
import javafx.application.Platform;

public class ThreadVisual extends Thread{
	
	private ControlGame game;
	
	public ThreadVisual(ControlGame game){
		this.game=game;
		setDaemon(true);
	}
	
	public void run(){
		//JavaFx Win Thread
		Runnable win=new Runnable(){
			public void run() {
				game.win();
			}
		};
		//JavaFx Show Thread
		Runnable show=new Runnable(){
			public void run() {
				game.moveBalls();
			}
		};
		
		while(!game.getFinished()){
			//Call Show Thread
			Platform.runLater(show);
			try {sleep(2);}
			catch (InterruptedException e) {e.printStackTrace();}
		}
		
		//Call Win Thread
		Platform.runLater(win);
	}
}
