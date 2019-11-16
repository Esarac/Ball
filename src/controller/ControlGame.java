package controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Ball;
import model.Game;
import model.Score;
import thread.ThreadBall;
import thread.ThreadVisual;

public class ControlGame implements Initializable{

	//Attributes
	private Game game;
	
	//Nodes
	private Canvas canvas;
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
	}
	
	public void loadGame() {
		//ChooseFile
		Stage stage = (Stage) pane.getScene().getWindow();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Game Selector");
		File file=fileChooser.showOpenDialog(stage);
		game.loadGame(file.toString());
		//...
		
		//Canvas
		canvas = new Canvas(pane.getWidth(), pane.getHeight());
		pane.getChildren().removeAll();
		pane.getChildren().add(canvas);
	    //...
		
		//StopMethod
		pane.setOnMouseClicked(event->stopBall(event));
		//...
		
		//ThreadBall
		ArrayList<Ball> balls=game.getBalls();
		for(Ball ball: balls){
			ThreadBall vBall=new ThreadBall(this, ball);
		    vBall.start();
		}
		//...
		
		//ThreadVisual
		ThreadVisual visual=new ThreadVisual(this);
		visual.start();
		//...
	}
	
	public void stopBall(MouseEvent e){
		if(!getWin()){
			double x = e.getX();
			double y = e.getY();
			ArrayList<Ball> balls=game.getBalls();
			for(Ball ball: balls){
				ball.stop(x,y);
			}
		}
	}
	
	public void win(){
		System.out.println("YOU WIN! -Score:"+game.totalBounces());
		if(game.record()){
			game.addScore("Esarac");
		}
		else{
			
		}
	}
	
	public void showRecords(){
		Score[][] scores=game.getScores();
		GridPane grid=new GridPane();
		pane.getChildren().add(grid);
		
		for(int x=0; x<scores.length; x++){//Filas
			RowConstraints row=new RowConstraints();
			row.setVgrow(Priority.ALWAYS);
			grid.getRowConstraints().add(row);
		}
		for(int y=0; y<scores[0].length; y++){//Columnas
			ColumnConstraints column=new ColumnConstraints();
			column.setHgrow(Priority.ALWAYS);
			grid.getColumnConstraints().add(column);
		}
		
		for(int x=0; x<scores.length; x++){
			for(int y=0; y<scores[0].length; y++){
				Label score=new Label();
				if(scores[x][y]!=null) {score.setText(scores[x][y].toString());}
				score.setMaxWidth(Double.MAX_VALUE);
				score.setMaxHeight(Double.MAX_VALUE);
				grid.add(score, y, x);
			}
		}
	}
	
	public void moveBalls(){
		//CanvasGenerator
	    GraphicsContext gc = canvas.getGraphicsContext2D();
	    //...
	    
	    //Background
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, pane.getWidth(), pane.getHeight());
		//...
		
		//Balls
		ArrayList<Ball> balls=game.getBalls();
		for(Ball ball: balls){
			double radius=ball.getRadius()*2;
			double posX=ball.getPosX()-ball.getRadius();
			double posY=ball.getPosY()-ball.getRadius();
			
			gc.setFill(Color.PURPLE);
			gc.fillOval(posX, posY, radius, radius);
		}
		//...
	}
	
	public double[] screenDimnesions(){
		double[] rectangle={pane.getWidth(), pane.getHeight()};
		return rectangle;
	}
	
	public boolean getWin(){
		return game.win();
	}
	
}
