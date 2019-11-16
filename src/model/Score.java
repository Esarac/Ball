package model;

public class Score implements Comparable<Score>{

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
