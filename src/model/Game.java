package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import model.Ball.Direction;

public class Game {

	//Constant
	public final static String FILE_TYPE="*.sav";
	public final static String SCORES_PATH="dat/scores.mtx";
	
	public final static int[] LEVELS={0, 1, 2};
	public final static int TOP=10;
	
	//Attributes
	private int level;
	private Score[][] scores;
	private ArrayList<Ball> balls;
	
	//Constructor
	public Game(){
		this.balls=new ArrayList<Ball>();
		this.scores=new Score[LEVELS.length][TOP];
		loadScores();
	}
	
	//Add
	public void addScore(String nickname){
		scores[level][TOP-1]=new Score(nickname, totalBounces());
		for(int i=0; i<scores[level].length; i++){
			for(int j=0; j<(scores[level].length-(i+1)); j++){
				if(scores[level][j+1]!=null){
					if( (scores[level][j+1].compareTo(scores[level][j]))<0 ){
						Score actualScore=scores[level][j];
						
						scores[level][j]=scores[level][j+1];
						scores[level][j+1]=actualScore;
					}
				}
			}
		}
	}
	
	//Calculate
	public boolean finshed(){
		boolean win=true;
		for(int i=0; (i<balls.size()) && win; i++){
			if(balls.get(i).isMoving()){
				win=false;
			}
		}
		return win;
	}
	
	public boolean record(){
		boolean record=false;
		for(int i=0; (i<scores[level].length) &&(!record); i++){
			if(scores[level][i]!=null){
				if(scores[level][i].getScore()>=totalBounces()){
					record=true;
				}
			}
			else{
				record=true;
			}
		}
		return record;
	}
	
	public int totalBounces(){
		int bounces=0;
		if(balls.size()!=0){
			for(int i=0; i<balls.size(); i++){
				bounces+=balls.get(i).getBounces();
			}
		}
		else{
			bounces=-1;
		}
		return bounces;
	}
	
	//Show
	public String showRecords(int level){
		String levelRecords="Level "+level+":";
		for(int i=0; i<scores[level].length; i++){
			if(scores[level][i]!=null){
				levelRecords+="\n"+scores[level][i];
			}
			else{
				levelRecords+="\n"+"------";
			}
			
		}
		return levelRecords;
	}
	
	//Reset
	public void resetGame(){
		for(int i=0; i<balls.size(); i++){
			balls.get(i).setMoving(false);
		}
		this.balls=new ArrayList<Ball>();
	}
	
	//Load
	public boolean loadGame(String path){//[FILE]
		boolean possible=true;
		resetGame();
		
		try {
			String data=read(path);
			if(data!=null){
				String[] globalData=data.split("\n");
				int index=0;
				for(String individualData: globalData){
					if(individualData.charAt(0)!='#'){//Ignore->#
						if(index==0){//Level
							for(int level: LEVELS){
								if(Integer.parseInt(individualData)==level){
									this.level=Integer.parseInt(individualData);
								}
							}
						}
						else{//Balls
							String[] ballData=individualData.split("	");
							double radius=Double.parseDouble(ballData[0]);
							double posX=Double.parseDouble(ballData[1]);
							double posY=Double.parseDouble(ballData[2]);
							int wait=Integer.parseInt(ballData[3]);
							//Direction
							double[] direction=null;
							try{
								String[] dir=ballData[4].split(",");
								double dirX=Double.parseDouble(dir[0]);
								double dirY=Double.parseDouble(dir[1]);
								double[] directionVector= {dirX, dirY};
								direction=directionVector;
							}
							catch(NumberFormatException e){
								Direction directionName=Direction.valueOf(ballData[4]);
								direction=directionName.getDirection();
							}
							//...
							int bounces=Integer.parseInt(ballData[5]);
							boolean moving=Boolean.parseBoolean(ballData[6]);
							
							Ball ball=new Ball(radius, posX, posY, direction, bounces, wait, moving);
							balls.add(ball);
						}
						index++;
					}
				}
			}
			else{
				try{
					throw new FileDoesNotExistException();
				}
				catch(FileDoesNotExistException e){
					possible=false;
				}
			}
		}
		catch(IOException | IllegalArgumentException | IndexOutOfBoundsException e){
			possible=false;
			resetGame();
		}
		
		return possible;
	}
	
	public boolean loadScores(){
		boolean possible=true;
		try{
			FileInputStream file=new FileInputStream(SCORES_PATH);
			ObjectInputStream creator=new ObjectInputStream(file);
			this.scores=(Score[][])creator.readObject();
			creator.close();
		}
		catch (IOException e) {saveScores();} 
		catch (ClassNotFoundException e) {possible=false;}
		return possible;
	}
	
	//Save
	public boolean saveGame(String path){
		boolean possible=true;
		try{
			String text="#Level";
			text+="\n"+level;
			for(int i=0; i<balls.size(); i++){
				text+="\n#radius	posX	posY	wait	dir	bounces	moving";
				text+="\n"+balls.get(i).getRadius()+"	"+balls.get(i).getPosX()+"	"+balls.get(i).getPosY()+"	"+balls.get(i).getWait()+"	"+balls.get(i).getDirection()[0]+","+balls.get(i).getDirection()[1]+"	"+balls.get(i).getBounces()+"	"+balls.get(i).isMoving();
			}
			
			File file=new File(path);
			PrintWriter writer=new PrintWriter(file);
			writer.append(text);
			writer.close();
		}
		catch (IOException e) {possible=false;}
		return possible;
	}
	
	public boolean saveScores(){
		boolean possible=true;
		try {
			File dir=new File("dat//");
			dir.mkdir();
			FileOutputStream file=new FileOutputStream(SCORES_PATH);
			ObjectOutputStream creator=new ObjectOutputStream(file);
			creator.writeObject(scores);
			creator.close();
		}
		catch (IOException e) {possible=false;}
		return possible;
	}
	
	//Read
	private String read(String path) throws IOException{//[File]
		String text="";
		
		File file=new File(path);
		if(file.exists()){
			file.createNewFile();
			FileReader fileReader=new FileReader(file);
			BufferedReader reader=new BufferedReader(fileReader);
			String actualLine;
			while((actualLine=reader.readLine())!=null){
				text+=actualLine+"\n";
			}
			reader.close();
		}
		else{
			text=null;
		}
		
		return text;
	}
	
	public int getLevel(){
		return level;
	}
	
	public Score[][] getScores(){
		return scores;
	}
	
	public ArrayList<Ball> getBalls(){
		return balls;
	}
	
}
