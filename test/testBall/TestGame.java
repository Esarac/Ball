package testBall;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import model.Ball;
import model.Ball.Direction;
import model.Game;
import model.Score;

class TestGame {

	//Tested Class
	private Game game;
	
	//Scenes
	private void setUpSceneGame0(){
		game=new Game();
		game.setLevel(0);
		game.setScores(new Score[Game.LEVELS.length][Game.TOP]);
		ArrayList<Ball> balls=new ArrayList<Ball>();
		balls.add(new Ball(50, 100, 100, Direction.UP_RIGHT.getDirection(), 10, 10, true));
		game.setBalls(balls);
	}
	
	private void setUpSceneGame1(){
		game=new Game();
		game.setLevel(1);
		ArrayList<Ball> balls=new ArrayList<Ball>();
		balls.add(new Ball(50, 100, 100, Direction.UP_RIGHT.getDirection(), 10, 10, false));
		game.setBalls(balls);
		game.setScores(new Score[Game.LEVELS.length][Game.TOP]);
	}
	
	private void setUpSceneGame2(){
		game=new Game();
		game.setLevel(2);
		ArrayList<Ball> balls=new ArrayList<Ball>();
		balls.add(new Ball(50, 100, 100, Direction.UP_RIGHT.getDirection(), 10, 10, true));
		game.setBalls(balls);
		game.setScores(new Score[Game.LEVELS.length][Game.TOP]);
	}
	
	private void setUpSceneGameRecord(){
		game=new Game();
		game.setLevel(0);
		ArrayList<Ball> balls=new ArrayList<Ball>();
		balls.add(new Ball(50, 100, 100, Direction.UP_RIGHT.getDirection(), 9, 10, true));
		game.setBalls(balls);
		Score[][] scores= {{new Score("E", 10),new Score("E", 10),new Score("E", 10),new Score("E", 10),new Score("E", 10),new Score("E", 10),new Score("E", 10),new Score("E", 10),new Score("E", 10),new Score("E", 10)},{},{}};
		game.setScores(scores);
	}
	
	private void setUpSceneGameNotRecord(){
		game=new Game();
		game.setLevel(0);
		ArrayList<Ball> balls=new ArrayList<Ball>();
		balls.add(new Ball(50, 100, 100, Direction.UP_RIGHT.getDirection(), 11, 10, true));
		game.setBalls(balls);
		Score[][] scores= {{new Score("E", 10),new Score("E", 10),new Score("E", 10),new Score("E", 10),new Score("E", 10),new Score("E", 10),new Score("E", 10),new Score("E", 10),new Score("E", 10),new Score("E", 10)},{},{}};
		game.setScores(scores);
	}
	
	private void setUpSceneGameEmpty(){
		game=new Game();
		game.setLevel(0);
		game.setScores(new Score[Game.LEVELS.length][Game.TOP]);
	}
	
	//Tests
	@Test
	void testAddScore() {
		//Level0
		setUpSceneGame0();
		game.addScore("Esteban");
		assertEquals(game.getScores()[0][0].getScore(), 10);
		//Level1
		setUpSceneGame1();
		game.addScore("Esteban");
		assertEquals(game.getScores()[1][0].getScore(), 10);
		//Level2
		setUpSceneGame2();
		game.addScore("Esteban");
		assertEquals(game.getScores()[2][0].getScore(), 10);
	}

	@Test
	void testFinished() {
		//NotFinished
		setUpSceneGame0();
		assertFalse(game.finished());
		//Finished
		setUpSceneGame1();
		assertTrue(game.finished());
	}
	
	@Test
	void testRecord(){
		//Empty
		setUpSceneGame0();
		assertTrue(game.record());
		//Record
		setUpSceneGameRecord();
		assertTrue(game.record());
		//NotRecord
		setUpSceneGameNotRecord();
		assertFalse(game.record());
	}
	
	@Test
	void testTotalBounces(){
		//Normal
		setUpSceneGame0();
		assertEquals(game.totalBounces(), 10);
		//Empty
		setUpSceneGameEmpty();
		assertEquals(game.totalBounces(), -1);
	}
	
	@Test
	void testShowRecords(){
		setUpSceneGameRecord();
		//Level0
		assertEquals(game.showRecords(0).split("\n")[0], "Level 0:");
		assertEquals(game.showRecords(0).split("\n")[1], "E:10bounces");
		//Level1
		assertEquals(game.showRecords(1).split("\n")[0], "Level 1:");
		//Level2
		assertEquals(game.showRecords(2).split("\n")[0], "Level 2:");
	}
	
}