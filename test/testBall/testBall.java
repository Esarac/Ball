package testBall;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.Ball;
import model.Ball.Direction;

class TestBall {

	//Tested Class
	private Ball ball;
	
	//Scenes
	private void setUpSceneBallRight(){
		ball=new Ball(50, 100, 100, Direction.RIGHT.getDirection(), 0, 10, true);
	}
	
	private void setUpSceneBallLeft(){
		ball=new Ball(50, 50, 50, Direction.LEFT.getDirection(), 0, 10, true);
	}
	
	private void setUpSceneBallUp(){
		ball=new Ball(50, 50, 50, Direction.UP.getDirection(), 0, 10, true);
	}
	
	private void setUpSceneBallDown(){
		ball=new Ball(50, 100, 100, Direction.DOWN.getDirection(), 0, 10, true);
	}
	
	//Tests
	@Test
	void testMove(){
		//Right
		setUpSceneBallRight();
		ball.move();
		assertEquals(ball.getPosX(), 110);
		//Left
		setUpSceneBallLeft();
		ball.move();
		assertEquals(ball.getPosX(), 40);
		//Up
		setUpSceneBallUp();
		ball.move();
		assertEquals(ball.getPosY(), 40);
		//Down
		setUpSceneBallDown();
		ball.move();
		assertEquals(ball.getPosY(), 110);
	}
	
	@Test
	void testBounce(){
		//Right
		setUpSceneBallRight();
		ball.bounce(150, 150);
		assertEquals(ball.getDirection()[0], -10);
		//Left
		setUpSceneBallLeft();
		ball.bounce(150, 150);
		assertEquals(ball.getDirection()[0], 10);
		//Up
		setUpSceneBallUp();
		ball.bounce(150, 150);
		assertEquals(ball.getDirection()[1], 10);
		//Down
		setUpSceneBallDown();
		ball.bounce(150, 150);
		assertEquals(ball.getDirection()[1], -10);
	}
	
	@Test
	void testStop(){
		//Normal
		setUpSceneBallRight();
		ball.stop(100, 100);
		assertFalse(ball.isMoving());
		//Left
		setUpSceneBallRight();
		ball.stop(50, 100);
		assertFalse(ball.isMoving());
		//Right
		setUpSceneBallRight();
		ball.stop(100, 50);
		assertFalse(ball.isMoving());
		//Down
		setUpSceneBallRight();
		ball.stop(150, 100);
		assertFalse(ball.isMoving());
		//Up
		setUpSceneBallRight();
		ball.stop(100, 150);
		assertFalse(ball.isMoving());
	}
	
	@Test
	void testUnitVector() {
		setUpSceneBallRight();
		//Max
		double[] vectorMax={12345, -98765};
		double[] unitVectorMax=ball.unitVector(vectorMax);
		double distanceMax=Math.sqrt( (unitVectorMax[0]*unitVectorMax[0]) + (unitVectorMax[1]*unitVectorMax[1]) );;
		assertTrue((distanceMax>=9.9)&&(distanceMax<=10));
		//Min
		double[] vectorMin={0.00058, 0.0007};
		double[] unitVectorMin=ball.unitVector(vectorMin);
		double distanceMin=Math.sqrt( (unitVectorMin[0]*unitVectorMin[0]) + (unitVectorMin[1]*unitVectorMin[1]) );
		assertTrue((distanceMin>=9.9)&&(distanceMin<=10));
		//Nor
		double[] vectorNor={10, 0};
		double[] unitVectorNor=ball.unitVector(vectorNor);
		double distanceNor=Math.sqrt( (unitVectorNor[0]*unitVectorNor[0]) + (unitVectorNor[1]*unitVectorNor[1]) );
		assertTrue(distanceNor==10);
	}
	
}