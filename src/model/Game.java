package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Game {

	//Constant
	public final static int[] LEVELS={0, 1, 2};
	public final static int TOP=10;
	
	//Attributes
	private int level;
	private Score[][] scores;
	private ArrayList<Ball> balls;
	
	//Constructor
	public Game(int level){
		if(Arrays.asList(LEVELS).contains(level))this.level=level;
		this.scores=new Score[LEVELS.length][TOP];
		
	}
	
	//Methods
	public boolean loadGame(String path){
		boolean possible=true;
		
		try {
			String data=read(path);
			if(data!=null){
				String[] globalData=data.split("\n");
				int index=0;
				for(String individualData: globalData){
					if(individualData.charAt(0)!='#'){//Ignore->#
						index++;
						if(index==0){//Level
							if(Arrays.asList(LEVELS).contains(Integer.parseInt(individualData)))
								this.level=level;
						}
						else{//Balls
							String[] ballData=data.split("	");
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
		catch(IOException e){
			possible=false;
		}
		
		return possible;
	}
	
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
	
}
