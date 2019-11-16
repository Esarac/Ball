package thread;

import controller.ControlGame;
import model.Ball;

public class ThreadBall extends Thread{

	//Attributes
	private Ball ball;
	private ControlGame game;
	
	public ThreadBall(ControlGame game, Ball ball){
		this.game=game;
		this.ball=ball;
		setDaemon(true);
	}
	
	public void run(){
		while(ball.isMoving()){
			//System.out.println("ThreadBall "+ball);
			double[] stage=game.screenDimnesions();
			
			ball.move();
			ball.bounce(stage[0], stage[1]);
			
			try {sleep(ball.getWait());}
			catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
}
