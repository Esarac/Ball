package thread;

import controller.ControlGame;

public class ThreadVisual extends Thread{
	
	private ControlGame game;
	
	public ThreadVisual(ControlGame game){
		this.game=game;
	}
	
	public void run(){
		while(!game.getWin()){
			game.moveBalls();
			try {sleep(10);}
			catch (InterruptedException e) {e.printStackTrace();}
		}
		game.win();
	}
	
}
