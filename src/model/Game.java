package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Game {

	//Constant
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
	public boolean win(){
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
			levelRecords+="\n"+scores[level][i];
		}
		return levelRecords;
	}
	
	//Reset
	public void resetGame(){
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
							if(Arrays.asList(LEVELS).contains(Integer.parseInt(individualData)))
								this.level=Integer.parseInt(individualData);
						}
						else{//Balls
							String[] ballData=individualData.split("	");
							double radius=Double.parseDouble(ballData[0]);
							double posX=Double.parseDouble(ballData[1]);
							double posY=Double.parseDouble(ballData[2]);
							int wait=Integer.parseInt(ballData[3]);
							//Direction
							String[] dir=ballData[4].split(",");
							double dirX=Double.parseDouble(dir[0]);
							double dirY=Double.parseDouble(dir[1]);
							double[] direction= {dirX, dirY};
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
					throw new FileDontExistException();
				}
				catch(FileDontExistException e){
					possible=false;
				}
			}
		}
		catch(IOException | NumberFormatException | IndexOutOfBoundsException e){
			possible=false;
			resetGame();
		}
		
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
