package controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Screen;
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
	private AnchorPane pane;
	
	//Initialize
	public void initialize(URL location, ResourceBundle resources) {
		this.game=new Game();
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
				pane.getChildren().clear();//Remove
				Rectangle2D bounds=Screen.getPrimary().getVisualBounds();
				canvas = new Canvas(bounds.getWidth(), bounds.getHeight());
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
	
	public void showRecords(){
		pane.getChildren().clear();//Remove
		scoreBox=new HBox();
		scoreBox.setSpacing(25);
		for(int level:Game.LEVELS){
			Label levelRecords=new Label("\n"+game.showRecords(level));
			scoreBox.getChildren().add(levelRecords);
		}
		pane.getChildren().add(scoreBox);
	}
	
	public void showInformation(){
		pane.getChildren().clear();//Remove
		scoreBox=new HBox();
		scoreBox.setSpacing(25);
		String info="\nAparecen unas esferas en la pantalla moviéndose,\n"
				+ "algunas horizontal y otras verticalmente. Durante su\n"
				+ "movimiento, si la esfera alcanza un extremo de la ventana\n"
				+ "de juego, ésta rebotará  y se moverá ahora en sentido\n"
				+ "contrario. El jugador debe detenerlas haciendo clic \n"
				+ "sobre cada una de las esferas que aparecen en la pantalla,\n"
				+ "lo más rápido posible y antes de que reboten. Por cada\n"
				+ "rebote, el contador de rebotes aumentará. El mejor\n"
				+ "jugador es aquel que detenga todas las esferas con la\n"
				+ "menor cantidad de rebotes.";
		Label levelRecords=new Label(info);
		scoreBox.getChildren().add(levelRecords);
		pane.getChildren().add(scoreBox);
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
		if(!getFinished()){
			double x = e.getX();
			double y = e.getY();
			ArrayList<Ball> balls=game.getBalls();
			for(Ball ball: balls){
				ball.stop(x,y);
			}
		}
	}
	
	//Threads
	public void win(){
		System.out.println("YOU WIN! -Score:"+game.totalBounces());
		if(game.totalBounces()>=0){
			if(game.record()){
		        TextInputDialog td = new TextInputDialog(); 
		        td.setHeaderText("New Record!\nScore: "+game.totalBounces()+"bounces"); 
		        td.showAndWait();
		        
				game.addScore(td.getEditor().getText());
			}
			else{
				ButtonType ok=new ButtonType("Ok",ButtonBar.ButtonData.OK_DONE);
				Alert alert=new Alert(AlertType.INFORMATION,"Score: "+game.totalBounces()+"bounces" , ok);
				alert.setHeaderText("Buena Partida!");
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
	    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		//...
		
		//Balls
		ArrayList<Ball> balls=game.getBalls();
		for(int i=0; i<balls.size(); i++){
			double radius=balls.get(i).getRadius()*2;
			double posX=balls.get(i).getPosX()-balls.get(i).getRadius();
			double posY=balls.get(i).getPosY()-balls.get(i).getRadius();
			
			//Border
			gc.setFill(Color.BLACK);
			gc.fillOval(posX, posY, radius, radius);
			//...
			
			//Ball
			gc.setFill(Color.rgb(255/((i+1)*2), 255/((i+1)*3), 255/(i+1)));
			gc.fillOval(posX+2, posY+2, radius-4, radius-4);
			//...
			
		}
		//...
	}
	
	//Methods
	public double[] screenDimensions(){
		double[] rectangle={pane.getWidth(), pane.getHeight()};
		return rectangle;
	}
	
	public boolean getFinished(){
		return game.finished();
	}
	
}
