package model;

public class Ball {

	//Constants
	public final static int DIMENSIONS=2;
	
	//Attributes
	private double radius;
	private double posX;
	private double posY;
	private double[] direction;
	private int bounces;
	private int wait;
	private boolean moving;
	
	//Constructor
	public Ball(double radius, double posX, double posY, double[] direction, int bounces, int wait, boolean moving){
		this.radius=radius;
		this.posX=posX;
		this.posY=posY;
		this.bounces=bounces;
		this.wait=wait;
		this.moving=moving;
		if(direction.length==DIMENSIONS){
			this.direction=unitVector(direction);
		}
		else{
			try{throw new InvalidVectorException();}
			catch (InvalidVectorException e){e.printStackTrace();}
		}
	}
	
	//Methods
	public void move(){
		if(moving){
			this.posX+=direction[0];
			this.posY+=direction[1];
		}
	}
	
	public void bounce(double width, double height){
		
		double distanceLeft=posX-radius;
		double distanceRight=posX+radius;
		
		if(distanceLeft<=0){
			this.direction[0]=(Math.abs(this.direction[0]));
			this.bounces++;
		}
		else if(distanceRight>=width){
			this.direction[0]=-1*(Math.abs(this.direction[0]));
			this.bounces++;
		}
		
		double distanceUp=posY-radius;
		double distanceDown=posY+radius;
		
		if(distanceUp<=0){
			this.direction[1]=(Math.abs(this.direction[1]));
			this.bounces++;
		}
		else if(distanceDown>=height){
			this.direction[1]=-1*(Math.abs(this.direction[1]));
			this.bounces++;
		}
		
	}
	
	public void stop(double x, double y){
		
		double deltaX=posX-x;
		double deltaY=posY-y;
		double distance=Math.sqrt( (Math.pow(deltaX, 2)) + (Math.pow(deltaY, 2)) );
		
		if(distance<=radius){
			this.moving=false;
		}
		
	}
	
	public double[] unitVector(double[] vector){
		
		//Constant
		double max=10;
		double min=9.9;
		double changer=1.01;
		//...
		
		double[] newVector=null;
		double length=Math.sqrt( (Math.pow(vector[0], 2)) + (Math.pow(vector[1], 2)) );
		
		if(length>max){
			vector[0]/=changer;
			vector[1]/=changer;
			newVector=unitVector(vector);
		}
		else if(length<min){//(length<min)
			vector[0]*=changer;
			vector[1]*=changer;
			newVector=unitVector(vector);
		}
		else{//(min<=length<=max)
			newVector=vector;
		}
		
		return newVector;
		
	}
	
}
