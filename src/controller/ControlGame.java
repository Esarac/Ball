package controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import model.Ball;
import model.Game;
import thread.ThreadBall;
import thread.ThreadVisual;

public class ControlGame implements Initializable{
	
	//Attributes
	private Game game;
	
	//Nodes
	private HBox scoreBox;
	private Canvas canvas;
	@FXML
	private Menu file;
	@FXML
	private Menu view;
	@FXML
	private AnchorPane pane;
	
	//Methods
	public void initialize(URL location, ResourceBundle resources) {
		this.game=new Game();
	}
	
	public double[] screenDimnesions(){
		double[] rectangle={pane.getWidth(), pane.getHeight()};
		return rectangle;
	}
	
	public boolean getFinshed(){
		return game.finshed();
	}
	
	//Threads
	public void win(){
		System.out.println("YOU WIN! -Score:"+game.totalBounces());
		if(game.totalBounces()>=0){
			if(game.record()){
		        TextInputDialog td = new TextInputDialog(); 
		        td.setHeaderText("New Record!"); 
		        td.showAndWait();
		        
				game.addScore(td.getEditor().getText());
			}
			else{
				ButtonType ok=new ButtonType("Ok",ButtonBar.ButtonData.OK_DONE);
				Alert alert=new Alert(AlertType.ERROR, "Try again.", ok);
				alert.setHeaderText("Invalid file!");
				alert.show();
			}
			game.saveScores();
		}
	}
	
	public void moveBalls(){
		//CanvasGenerator
	    GraphicsContext gc = canvas.getGraphicsContext2D();
	    //...
	    
	    //Background
	    gc.clearRect(0, 0, pane.getWidth(), pane.getHeight());
		//...
		
		//Balls
		ArrayList<Ball> balls=game.getBalls();
		for(int i=0; i<balls.size(); i++){
			double radius=balls.get(i).getRadius()*2;
			double posX=balls.get(i).getPosX()-balls.get(i).getRadius();
			double posY=balls.get(i).getPosY()-balls.get(i).getRadius();
			
			//Border
			gc.setFill(Color.BLACK);
			gc.fillOval(posX-2, posY-2, (radius)+5, (radius)+5);
			//...
			
			//Ball
			gc.setFill(Color.rgb(255/((i+1)*2), 255/((i+1)*3), 255/(i+1)));
			gc.fillOval(posX, posY, radius, radius);
			//...
		}
		//...
	}
	
	//OnAction
	public void loadGame() {
		game.resetGame();
		try{
			//ChooseFile
			Stage stage = (Stage) pane.getScene().getWindow();
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Game Selector");
			fileChooser.getExtensionFilters().add(new ExtensionFilter("SAV", Game.FILE_TYPE));
			fileChooser.getExtensionFilters().add(new ExtensionFilter("TXT", "*.txt"));
			File dir = new File("dat/games");
			fileChooser.setInitialDirectory(dir);
			File file=fileChooser.showOpenDialog(stage);
			//...
			if(game.loadGame(file.toString())){
				//Canvas
				pane.getChildren().remove(canvas);pane.getChildren().remove(scoreBox);//Remove
				canvas = new Canvas(pane.getWidth(), pane.getHeight());
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
			else{
				ButtonType ok=new ButtonType("Ok",ButtonBar.ButtonData.OK_DONE);
				Alert alert=new Alert(AlertType.ERROR, "Try again.", ok);
				alert.setHeaderText("Invalid file!");
				alert.show();
			}
		}
		catch(NullPointerException e){}
		
	}
	
	public void saveGame(){
		try{
			//ChooseFile
			Stage stage = (Stage) pane.getScene().getWindow();
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Game Saver");
			fileChooser.getExtensionFilters().add(new ExtensionFilter("SAV", Game.FILE_TYPE));
			fileChooser.getExtensionFilters().add(new ExtensionFilter("TXT", "*.txt"));
			File dir = new File("dat/games");
			fileChooser.setInitialDirectory(dir);
			File file=fileChooser.showSaveDialog(stage);;
			//...
			
			game.saveGame(file.getPath());
		}
		catch(NullPointerException e){
			
		}
		
	}
	
	public void stopBall(MouseEvent e){
		if(!getFinshed()){
			double x = e.getX();
			double y = e.getY();
			ArrayList<Ball> balls=game.getBalls();
			for(Ball ball: balls){
				ball.stop(x,y);
			}
		}
	}
	
	public void showRecords(){
		pane.getChildren().remove(canvas);pane.getChildren().remove(scoreBox);//Remove
		scoreBox=new HBox();
		scoreBox.setSpacing(25);
		for(int level:Game.LEVELS){
			Label levelRecords=new Label(game.showRecords(level));
			scoreBox.getChildren().add(levelRecords);
		}
		pane.getChildren().add(scoreBox);
	}
	
}
