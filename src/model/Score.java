package model;

import java.io.Serializable;

public class Score implements Comparable<Score>, Serializable{

	//Constants
	private static final long serialVersionUID = 1L;
	
	//Attributes
	private String nickname;
	private int score;
	
	//Constructor
	public Score(String nickname, int score){
		this.nickname=nickname;
		this.score=score;
	}
	
	//Methods
	public int getScore(){
		return score;
	}

	@Override
	public int compareTo(Score score){
		int delta;
		if(score!=null){delta=this.score-score.score;}
		else{delta=-1;}
		return delta;
	}
	
	public String toString(){
		return nickname+":"+score+"bounces";
	}
	
}
