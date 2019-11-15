package thread;

import controller.ControlGame;
import model.Ball;

public class VisualBall extends Thread{

	//Attributes
	private int index;
	private Ball ball;
	private ControlGame game;
	
	public VisualBall(ControlGame game, Ball ball, int index){
		this.index=index;
		this.game=game;
		this.ball=ball;
	}
	
	public void run(){
		while(ball.isMoving()){
			double size[]=game.screenDimnesions();
			ball.move();
			System.out.println(ball.getPosX()+" - "+ball.getPosY());
			ball.bounce(size[0], size[1]);
			game.moveBall(index, ball.getPosX(), ball.getPosY());
			try {sleep(ball.getWait());}
			catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
}
