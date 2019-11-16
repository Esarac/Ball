package thread;

import controller.ControlGame;

public class ThreadVisual extends Thread{
	
	private ControlGame game;
	
	public ThreadVisual(ControlGame game){
		this.game=game;
		setDaemon(true);
	}
	
	public void run(){
		while(!game.getFinshed()){
			//System.out.println("ThreadVisual");
			game.moveBalls();
			try {sleep(2);}
			catch (InterruptedException e) {e.printStackTrace();}
		}
		game.win();
	}
	
}
