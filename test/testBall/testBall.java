package testBall;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.Ball;

class testBall {

	//Tested Class
	private Ball ball;
	
	//Scenes
	private void setUpSceneBall1(){
		double[] vector= {1,0};
		ball=new Ball(50, 70, 90, vector, 0, 10, true);
	}
	
	//Tests
	@Test
	void testUnitVector() {
		setUpSceneBall1();
		
		double[] vectorMax={12345, -98765};
		double[] unitVectorMax=ball.unitVector(vectorMax);
		double distanceMax=Math.sqrt( (unitVectorMax[0]*unitVectorMax[0]) + (unitVectorMax[1]*unitVectorMax[1]) );;
		assertTrue((distanceMax>=9.9)&&(distanceMax<=10));
		
		double[] vectorMin={0.00058, 0.0007};
		double[] unitVectorMin=ball.unitVector(vectorMin);
		double distanceMin=Math.sqrt( (unitVectorMin[0]*unitVectorMin[0]) + (unitVectorMin[1]*unitVectorMin[1]) );
		assertTrue((distanceMin>=9.9)&&(distanceMin<=10));
	}

}
