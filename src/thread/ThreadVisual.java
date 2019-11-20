package thread;

import controller.ControlGame;

public class ThreadVisual extends Thread{
	
	private ControlGame game;
	private boolean run;
	
	public ThreadVisual(ControlGame game){
		this.game=game;
		this.run=true;
		setDaemon(true);
	}
	
	public void run(){
		while(!game.getFinshed()){
			game.moveBalls();
			try {sleep(2);}
			catch (InterruptedException e) {e.printStackTrace();}
		}
		game.win();
	}
}
