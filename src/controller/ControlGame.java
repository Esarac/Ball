package controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Ball;
import model.Game;
import thread.VisualBall;

public class ControlGame implements Initializable{

	//Attributes
	private Game game;
	
	//Nodes
	private ArrayList<Circle> balls;
	@FXML
	private Menu file;
	@FXML
	private Menu view;
	@FXML
	private AnchorPane pane;
	
	//Methods
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.game=new Game();
		this.balls=new ArrayList<Circle>();
	}

	public void loadGame() {
		Stage stage = (Stage) pane.getScene().getWindow();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Game Selector");
		File file=fileChooser.showOpenDialog(stage);
		game.loadGame(file.toString());
		
//		Canvas canvas = new Canvas();
//	    pane.getChildren().add( canvas );
//	    GraphicsContext gc = canvas.getGraphicsContext2D();
		
		ArrayList<Ball> balls=game.getBalls();
		for(int i=0; i<balls.size(); i++){
			VisualBall vBall=new VisualBall(this, balls.get(i), i);
			vBall.start();
			Circle circle=new Circle(balls.get(i).getPosX(), balls.get(i).getPosY(), balls.get(i).getRadius(), Color.rgb((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255)));
			this.balls.add(circle);
//			gc.fillOval(balls.get(i).getPosX(), balls.get(i).getPosY(), balls.get(i).getRadius(), balls.get(i).getRadius());
			pane.getChildren().add(circle);
		}
	}
	
	public void moveBall(int index, double posX, double posY){
		try{
			Circle vBall=this.balls.get(index);
			vBall.setCenterX(posX);
			vBall.setCenterY(posY);
		}
		catch(IndexOutOfBoundsException e){}
	}
	
	public double[] screenDimnesions(){
		Stage stage = (Stage) pane.getScene().getWindow();
		double[] rectangle={stage.getWidth(), stage.getHeight()};
		return rectangle;
	}
	
}
