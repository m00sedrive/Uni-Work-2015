package uk.ac.robocode.tom;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.Action;

import robocode.AdvancedRobot;
import robocode.BattleEndedEvent;
import robocode.BulletHitEvent;
import robocode.DeathEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.RobocodeFileOutputStream;
import robocode.RoundEndedEvent;
import robocode.ScannedRobotEvent;

public class TBedford_Robot extends AdvancedRobot {

	private Random rnd = new Random();
	private String scoresFilename = "scores.txt";
	private String sortedFilename = "sorted.txt";
	private String dataFilename = "data.txt";
	private int roundDamageTaken;
	private int attackSkills = 0;
	private int defenseSkills = 0;
	private int navigationSkills = 0;
	private StringBuilder movesBuffer = new StringBuilder();
	private StringBuilder scoresBuffer = new StringBuilder();
	private ArrayList<String> previousMoves = new ArrayList<String>();
	private ArrayList<String> previousScores = new ArrayList<String>();
	private ArrayList<String> previousRandom = new ArrayList<String>();	
	private String behaviour = null;
	private String power = null;
	private enum DECODE_TYPE {
		MOVES,
		SCORES;
	}
	private enum ENCODE_TYPE {
		MOVES,
		SCORES;
	}
	private enum READ_PREVIOUS { 
		MOVES,
		SCORES;
	}
	
	@Override
	public void run() {
		
		// if saved scores available sort scores
		// select 2 parents at random (roullette selection) make sure 2 parents are different
		// populate children for 100 children
		//   - crossover
		//   - mutation
		// save children
		// decode children
		// Run evolution - using children data. Consider using a boolean flag to notify all children scored
		
		// else create population
		
		boolean dataAvailable = getDataFile(dataFilename).exists();
		System.out.println(dataAvailable);
		ArrayList<String> tempBuffer = new ArrayList<String>();
		// create random population moves list
		for (int i = 0; i < 100; i++) {
			// clear buffer
			StringBuilder movesBuffer = new StringBuilder();
			while (movesBuffer.length() < 7*20) { // 7 = number of chars in set
				movesBuffer.append(";");
				movesBuffer.append(String.format("%02d", getRandNum(1, 7)));
				movesBuffer.append(",");
				movesBuffer.append(String.format("%02d", getRandNum(1, 99)));
				movesBuffer.append(",");
			}
			System.out.println(i + " = " + movesBuffer.toString());
			tempBuffer.add(movesBuffer.toString());
		}
		try {
			PrintStream writer = new PrintStream(new RobocodeFileOutputStream(getDataFile(dataFilename)));		
			for (int i = 0; i < tempBuffer.size(); ++i) {
				writer.println(tempBuffer.get(i));
			}
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Error: could not write strings to data file");
		}

		
		if (dataAvailable && getDataFile(dataFilename).length() > 0) {

		} else {

		}
		
		//decodeLine("", DECODE_TYPES.DECODE_TYPE_A);

		/*while (true) {
			//decode robot line into ArrayList
			for (int i = 0; i < ArrayList.length; ++i) {
				Behaviour = ArrayList[0];
				Power = ArrayList[1];
				performBehaviour(behaviourm, power);
			}
			//performBehaviour
			//calculate and save score
			//
			{
				decodeLine();
				performBehaviour();
			}
		}*/
		// sortscores
	}

	@Override
	public void onDeath(DeathEvent e) {
	}

	public void readPrevious(String filename, READ_PREVIOUS read_previous) {
		
		switch(read_previous) {
		case MOVES:
			try {
				BufferedReader reader = new BufferedReader(new FileReader(getDataFile(filename)));
				String line = null;
					while ((line = reader.readLine()) != null) {
						previousMoves.add(line);
					}
				reader.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		case SCORES:
			try {
				BufferedReader reader = new BufferedReader(new FileReader(getDataFile(filename)));
				String line = null;
					while ((line = reader.readLine()) != null) {
						previousMoves.add(line);
					}
				reader.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<String> getScores(ArrayList<String> scores) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(getDataFile(scoresFilename)));
			String line = null;
			int count = 0;
			while ((line = reader.readLine()) != null) {
				scores.set(count, line);
				count++;
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return scores;
	}

	public ArrayList<String> sortScores(ArrayList<String> scores) {
		int scoreSize = scores.size();
		int[] t_roundDamageTaken = new int[scoreSize];
		int[] t_attackSkills = new int[scoreSize];
		int[] t_defenceSkills = new int[scoreSize];
		int[] t_navigationSkills = new int[scoreSize];
		int nextScore = 0;
		int currentScore = 0;

		// bubble sort
		for (int i = 0; i < scoreSize; i++) {
			currentScore = t_roundDamageTaken[i];
			nextScore = t_roundDamageTaken[i + 1];
			if (t_roundDamageTaken[i] > t_roundDamageTaken[i + 1])
				t_roundDamageTaken[i] = nextScore;
			t_roundDamageTaken[i + 1] = currentScore;
			String temp = scores.get(i);
			scores.set(i, scores.get(i + 1));
			scores.set(i + 1, temp);
		}

		// save sorted scores to file
		try {
			PrintStream writer = new PrintStream(new RobocodeFileOutputStream(getDataFile(sortedFilename)));
			for (int i = 0; i < scores.size(); i++) {
				writer.print(scores.get(i));
				writer.println();
			}
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace(out);
			System.out.println("Error: could not write scores to sorted file!");
		}
		return scores;
	}

	private String encodeLine() {
		String encodedLine = String.format(";%02d,%02d,", behaviour, power);
		return encodedLine;
	}
	
	private void decodeLine(String line, DECODE_TYPE decode_line_type) {
		List<Token> returnVal = new ArrayList<Token>();

		switch (decode_line_type) {
		case MOVES: {
			String[] parts = line.split(";");
			for (int i = 1; i < parts.length; ++i) {
				String[] values = parts[i].split(",");
				String behaviour = values[0];
				String power = values[1];
				returnVal.add(new Token(behaviour, power));
			}
			break;
		} case SCORES:
			String[] parts = line.split(";");
			for(int i = 1; i < parts.length; ++i) {
				String[] values = parts[i].split(",");
				String roundDamageTaken = values[0];
				String attackSkills = values[1];
				String defenseSkills = values[2];
				String navigationSkills = values[3];
				returnVal.add(new Token(roundDamageTaken, attackSkills, defenseSkills, navigationSkills));
			}
			break;
		default:
			break;
		}
	}

	private int getRandNum(int min, int max) {
		return rnd.nextInt(max - min) + min;
	}

	@Override
	public void onScannedRobot(ScannedRobotEvent e) {
		attackSkills++;
	}

	private void printArrayList(ArrayList<?> al) {
		for (int i = 0; i < al.size(); i++) {
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
	public void onHitRobot(HitRobotEvent e) {
		navigationSkills--;
		roundDamageTaken--;
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
		score.append(";");
		score.append((roundDamageTaken + ((int) getEnergy())) + ",");
		score.append(attackSkills);
		score.append(",");
		score.append(defenseSkills);
		score.append(",");
		score.append(navigationSkills);
		score.append(",");

		// save score strings
		try {
			readPrevious(scoresFilename, READ_PREVIOUS.SCORES);
			PrintStream writer = new PrintStream(new RobocodeFileOutputStream(getDataFile(scoresFilename)));
			for (int i = 0; i < previousScores.size(); i++) {
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
			readPrevious(dataFilename, READ_PREVIOUS.MOVES);
			PrintStream writer = new PrintStream(new RobocodeFileOutputStream(getDataFile(dataFilename)));
			for (int i = 0; i < previousMoves.size(); i++) {
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
	}

	public void performBehaviour(int behaviour, int power) {

		switch (behaviour) {

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