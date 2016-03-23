package uk.ac.robocode.tom;

public class Token {
	public String behaviour;
	public String power;
	public String roundDamageTaken;
	public String attackSkills;
	public String defenseSkills;
	public String navigationSkills;

	public Token(String behaviour, String power) {
		this.behaviour = behaviour;
		this.power = power;
	}

	public Token(String roundDamageTaken, String attackSkills, String defenseSkills, String navigationSkills) {
		this.roundDamageTaken = roundDamageTaken;
		this.attackSkills = attackSkills;
		this.defenseSkills = defenseSkills;
		this.navigationSkills = navigationSkills;
		
	}
}
