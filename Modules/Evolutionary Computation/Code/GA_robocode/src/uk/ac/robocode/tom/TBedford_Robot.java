package uk.ac.robocode.tom;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import robocode.AdvancedRobot;
import robocode.BulletHitEvent;
import robocode.DeathEvent;
import robocode.HitByBulletEvent;
import robocode.HitWallEvent;
import robocode.RobocodeFileOutputStream;
import robocode.RoundEndedEvent;
import robocode.ScannedRobotEvent;

public class TBedford_Robot extends AdvancedRobot{

	private String scoresFilename = "stats.txt";
	private String sortedSFilename = "sorted.txt";
	private String dataFilename = "data.txt";
	private Integer roundDamageTaken;
	private Integer attackSkills = 0;
	private Integer defenseSkills = 0;
	private Integer navigationSkills = 0;
	private StringBuilder movesBuffer = new StringBuilder();
	private StringBuilder scoresBuffer = new StringBuilder();
	private ArrayList<String> previousMoves = new ArrayList<String>();
	private ArrayList<String> previousScores = new ArrayList<String>();

	@Override
	public void run() {
		
		boolean dataAvailable = getDataFile(dataFilename).exists();
		if(dataAvailable && getDataFile(dataFilename).length() > 0) {
			
		}
		else {

		}
		
		while(true) {
			Integer b = getRandNum(7) + 1;
			Integer p = getRandNum(71) + 30;
			performBehaviour(b, p);
			updateBuffer(b,p);
		}
	}		
	
	private void updateBuffer(int b, int p) {

		// add moves to buffer
		movesBuffer.append(b);
		movesBuffer.append(",");
		movesBuffer.append(p);
		movesBuffer.append(",");		 	
	}
	
	@Override
	public void onDeath(DeathEvent e) {	
	}
	
	public void readPrevious(String filename, int num) {	
		try {
			BufferedReader reader = new BufferedReader
					(new FileReader(getDataFile(filename)));
		    String line = null;
			while((line = reader.readLine()) != null) {
		    	if(num == 2)
		    		previousMoves.add(line);
		    	if(num == 1)
		    		previousScores.add(line);
	    	}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public ArrayList getScores(ArrayList scores) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(getDataFile(scoresFilename)));
			String line = null;
			while((line = reader.readLine()) != null) {
				scores.add(line);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return scores;
	}

	public ArrayList<String> sortScores(ArrayList<String> scores, int sortBy) {
		ArrayList<String> sortedList = new ArrayList<String>();
		int temp = 0;
		int[] tempList = null;
		for(int i=0;i<scores.size();i++) {
			// decode first line
			decodeLine(scores.get(i), this.roundDamageTaken, this.attackSkills, this.defenseSkills, this.navigationSkills);
			// decode second line and store in temp vars
			Integer t_roundDamageTaken = 0;
			Integer t_attackSkills = 0;
			Integer t_defenceSkills = 0;
			Integer t_navigationSkills = 0;
			decodeLine(scores.get(i + 1), t_roundDamageTaken, t_attackSkills, t_defenceSkills, t_navigationSkills);
			
			// all sort types use bubble sort method
			// if sort by round damage taken  
			if(sortBy == 1) {
				 if(this.roundDamageTaken > t_roundDamageTaken)
					sortedList.set(i, t_roundDamageTaken.toString());
				 	sortedList.set(i + 1, this.roundDamageTaken.toString());
				 	
			}
			// if sort by attack skills
			if(sortBy == 2) {
				
			}
			// is sort by defence skills
			if(sortBy == 3) {
			
			}
			// is sort by navigation skills
			if(sortBy == 4) {
			
			}
		}
		// debug
		printArrayList(sortedList);
		return sortedList;
	}
	
	private void decodeLine(String line, int roundDamageTaken, int attackSkills, int defenseSkills, int navigationSkills) {
		String[] parts = line.split(",");
		roundDamageTaken = Integer.parseInt(parts[0]);
		attackSkills = Integer.parseInt(parts[1]);
		defenseSkills = Integer.parseInt(parts[2]);
		navigationSkills = Integer.parseInt(parts[3]);
	}
	
	protected Integer getRandNum(int max) {
		Random rnd = new Random();
		return rnd.nextInt(max);
	}
	
	@Override
	public void onScannedRobot(ScannedRobotEvent e) {
		attackSkills++;
	}
	
	private void printArrayList(ArrayList al) {
		for(int i=0; i<al.size();i++) {
			System.out.println(al.get(i));
		}
	}
	
	@Override
	public void onBulletHit(BulletHitEvent e) {
		// store bullet hit event
		// add to attack skills
		attackSkills++;
	}
	
	@Override
	public void onHitByBullet(HitByBulletEvent e) {
		defenseSkills--;
		roundDamageTaken++;
	}
	
	@Override
	public void onHitWall(HitWallEvent e) {
		navigationSkills--;
		roundDamageTaken++;
	}
	
	@Override 
 	public void onRoundEnded(RoundEndedEvent e) {  
 		
		StringBuilder score = new StringBuilder();
 		// get last score string
 		score.append((roundDamageTaken + ((int) getEnergy())) + ","); 
 		score.append(attackSkills);
 		score.append(",");
 		score.append(defenseSkills); 
 		score.append(","); 
 		score.append(navigationSkills); 
 		score.append(",");
 		
 		// save score strings
 		try {
 			readPrevious(scoresFilename, 1);
 			PrintStream writer = new PrintStream(new RobocodeFileOutputStream( 
 					getDataFile(scoresFilename)));  
 			for(int i=0;i<previousScores.size();i++) {
 				writer.print(previousScores.get(i));
 				writer.println();
 			}
 			writer.print(score.toString());
 			writer.println();
 			writer.close(); 
 		} catch (Exception ex) { 
 			ex.printStackTrace(out);
 			System.out.println("Error: could not write stats to stats file!");
 		}
 		
 		// save moves set
 		try { 
 			readPrevious(dataFilename, 2);
 			PrintStream writer = new PrintStream(new RobocodeFileOutputStream( 
 					getDataFile(dataFilename)));  
 			for(int i=0;i<previousMoves.size();i++) {
 				writer.print(previousMoves.get(i));
 				writer.println();
 			}
 			writer.print(movesBuffer.toString());
 			writer.println();
 			writer.close(); 
 		} catch (Exception ex) { 
 			ex.printStackTrace(out);
 			System.out.println("Error: could not write moves to data file");
 		}
 		
 		// get and sort scores
 		ArrayList<String> scores = new ArrayList<String>();
 		//getScores(sortScores(scores));
 	} 
	
    public void performBehaviour(int behaviour, int power) {
    	
    	switch(behaviour) {
    	
    	case 1:
    		turnLeft(power);
    		break;
    	case 2:
    		turnRight(power);
    		break;
    	case 3:
    		ahead(power);
    		break;
    	case 4:
    		back(power);
    		break;
    	case 5:
    		turnGunLeft(power);
    		break;
    	case 6:
    		turnGunRight(power);
    		break;
    	case 7:
    		fire(power);
    		break;
    	default:
    		break;
    	}
    }
}
