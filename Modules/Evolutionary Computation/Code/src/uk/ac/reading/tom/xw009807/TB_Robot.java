package uk.ac.reading.tom.xw009807;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

import robocode.AdvancedRobot;
import robocode.BattleEndedEvent;
import robocode.BulletHitEvent;
import robocode.DeathEvent;
import robocode.HitByBulletEvent;
import robocode.RobocodeFileOutputStream;
import robocode.RoundEndedEvent;
import robocode.ScannedRobotEvent;
import robocode.StatusEvent;
import robocode.WinEvent;

public class TB_Robot extends AdvancedRobot {

	private final String data2UseFilename = "data2use.txt";
	private final String childFilename = "children.txt";
	private final String scoreFilename = "scores.txt";
	private final String statsFilename = "statistics.txt";
	private StringBuffer previousFile = new StringBuffer();
	private Genotype behaviours = null;
	private Genotype onScan = null;
	private Genotype onHit = null;
	private Genotype onTake = null;
	private int roundDamage = 0;
	private int numOfStoredResults = 0;
	private int numOfChildren = 100;
	private int numOfParents = 10;
	public static int mutationWeight = 1;
	
	@Override
	public void run() {
		// check for existing move set and load if any
		boolean dataToLoad = getDataFile(data2UseFilename).exists();
		if(dataToLoad && getDataFile(data2UseFilename).length() > 0) {
			loadMoveSet();
		}
		else 
		{
			boolean childExist = getDataFile(childFilename).exists();
			if(childExist && getDataFile(childFilename).length() == 0) {
				// read previous generations data from file
				loadPrevGen(childExist);
			}
			if(childExist) {
				// read children data from file
				loadChildGen();
			}
		}
		
		while(true) {
			runGenotype(behaviours, false);
		}
	}
	
	private void loadMoveSet() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(getDataFile(data2UseFilename)));
			String line = reader.readLine();
			reader.close();
			// error handling
			if(line == null) {			
				System.out.println(data2UseFilename + ": file empty, nothing to read");
			}
			// decode loaded move set
			decodeLine(line, behaviours, onScan, onHit, onTake);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean loadPrevGen(boolean childrenExist) {
		// read in previous genetics
		try {
			BufferedReader br = new BufferedReader(new FileReader(getDataFile(scoreFilename)));
			int counter = 0;
			String line = null;
			
			// array lists for storing read in lines and scores
			ArrayList<Integer> scores = new ArrayList<Integer>();
			ArrayList<String> lines = new ArrayList<String>();
			
			while((line = br.readLine()) != null) {
				previousFile.append(",");
				previousFile.append(System.lineSeparator());
				if(line.length() > 1) {
					counter++;
					int line_score = Integer.parseInt(line.split(",")[0]);
					lines.add(line);
					scores.add(line_score);
				}		
			}
			numOfStoredResults = counter;
			if(numOfStoredResults >= numOfChildren) {
				br.close();
				previousFile = new StringBuffer();
				numOfStoredResults = 0;
				// get mutation data
				getMutatedData(scores.toArray(new Integer[scores.size()]),lines.toArray(new String[lines.size()])); 
				return true;
			}
			else if(!childrenExist) {
				br.close();
				// create data
				behaviours = new Genotype(15);
				onScan = new Genotype(10);
				onHit = new Genotype(10);
				onTake = new Genotype(10);
				return false;
			}
			br.close();	
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private void loadChildGen() {
		// load child data
		try {
			BufferedReader br = new BufferedReader(new FileReader(getDataFile(childFilename)));
			String line = null;
			while((line = br.readLine()) != null) {
				int counter = 0;
				if(counter > numOfStoredResults) {
					String[] parts = line.split(",");
					behaviours = new Genotype(parts[0]);
					onScan = new Genotype(parts[1]);
					onHit = new Genotype(parts[2]);
					onTake = new Genotype(parts[3]);
					br.close();
					return;
				}
			}
			br.close();
			System.out.println("ERROR: No children data available");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void getMutatedData(Integer[] scores, String[] lines) {
		// sort scores
		sortScores(scores, lines);
		// pick parents
		String[] parents = selectParents(lines);
		// create children from adults
		String[] children = populateChildren(parents);
		// save children to file
		try {
			PrintStream writer = new PrintStream(new RobocodeFileOutputStream(getDataFile(childFilename)));
			for(String child: children) {
				writer.println(child);
			}
			writer.close();
		}
		catch(Exception e) {
			e.printStackTrace(out);
		}
	}
	
	private void sortScores(Integer[] scores, String[] lines) {
		
		for(int i=0; i<scores.length;i++) {
			int score = scores[i];
			int nextScore = scores[i+1];
			if(score < nextScore) {
				scores[i] = nextScore;
				scores[i+1] = score;
				String temp = lines[i];
				lines[i] = lines[i+1];
				lines[i+1] = temp;
			}
		}
		try {
			
			// output statistics
			BufferedReader br = new BufferedReader(new FileReader(getDataFile(statsFilename)));
			String line = null;
			StringBuffer buffer = new StringBuffer();
			while((line = br.readLine()) != null) {
				buffer.append(line + "\n");
			}
			br.close();
			
			PrintStream writer = new PrintStream(new RobocodeFileOutputStream(getDataFile(statsFilename)));
			double average = 0;
			for(int score: scores ) {
				average += score;
				average /= scores.length;
				writer.print(buffer.toString());
				writer.println(scores[0] + "," + scores[scores.length - 1] + ",");
				writer.close();
			}
		}
		catch(Exception e) {
			e.printStackTrace(out);
		}
	}
	
	private String[] selectParents(String[] lines) {
		String[] parents = new String[numOfParents];
		for(int i=0;i<numOfParents;i++) {
			int select = randomNum(1, 100);
			if(select > 70) {
				parents[i] = lines[randomNum(0,5)];
			}
			else if(select > 50) {
				parents[i] = lines[randomNum(6, 11)];
			}
			else if(select > 20) {
				parents[i] = lines[randomNum(12, 21)];
			}
			else
				parents[i] = lines[randomNum(22, 35)];
		}
		return parents;
	}
	
	private String[] populateChildren(String[] parents) {
		String[] children = new String[numOfChildren];
		for(int i=0;i<numOfChildren;i++) {
			// pick 2 random parents
			int parent1 = randomNum(1, numOfParents - 1);
			int parent2 = parent1;
			// ensure both random parents are not the same
			while(parent1 == parent2) {
				parent2 = randomNum(0, numOfParents - 1);
			}
			// get parents as string
			String lineP1 = parents[parent1];
			String lineP2 = parents[parent2];
			// remove score from string
			lineP1 = lineP1.substring(lineP1.indexOf(",") + 1, lineP1.length());
			lineP2 = lineP2.substring(lineP2.indexOf(",") + 1, lineP2.length());	
			// swap parts of string at random
			String[] parts1 = lineP1.split(",");
			String[] parts2 = lineP2.split(",");	
			// pick random part
			int part = randomNum(0, parts1.length - 2);
			// swap key nodes in genotype tree
			Genotype a = new Genotype(parts1[part]);
			Genotype b = new Genotype(parts2[part]);
			a.swap(b, randomNum(2, a.length() - 2 ), randomNum(2, b.length() - 2));
			// reconstruct string
			lineP1 = "";
			lineP2 = "";
			for(int p=0; p<parts1.length;p++) {
				if(p == part) {
					lineP1 += a.getStorageString();
					lineP2 += b.getStorageString();
					continue;
				}
			}
			lineP1 = lineP1.substring(0, lineP1.length() - 1);
			lineP2 = lineP2.substring(0, lineP2.length() - 1);
			//mutate data
			StringBuffer bA = new StringBuffer();
			StringBuffer bB = new StringBuffer();
			parts1 = lineP1.split(",");
			parts2 = lineP2.split(",");
			for(int p=0;p<parts1.length -1;p++) {
				Genotype g = new Genotype(parts1[p]);
				g.mutate();
				parts1[p] = g.getStorageString();
				g = new Genotype(parts2[p]);
				g.mutate();
				parts2[p] = g.getStorageString();
				bA.append(parts1[p] + ",");
				bB.append(parts2[p] + ",");		
			}
			bA.deleteCharAt(bA.length() - 1);
			bB.deleteCharAt(bB.length() - 1);
			children[i] = bA.toString();
			children[i + 1] = bB.toString();
		}
		return children;	
	}
	
	private void decodeLine(String line, Genotype behavours, Genotype onScan, Genotype onHit, Genotype onTake) {
		String parts[] = line.split(",");
		behavours = new Genotype(parts[0]);
		onScan = new Genotype(parts[1]);
		onHit = new Genotype(parts[2]);
		onTake = new Genotype(parts[3]); 
	}
	
	private void runGenotype(Genotype g, boolean event) {
		// debug
		System.out.println("genotype: " + g);
		
		Integer result = event ? handleBehaviour(g.key) : performBehaviour(g.key); 
		if(result == 0) {
			if(g.leftNode != null) {
				runGenotype(g.leftNode, event);
			}
			if(g.rightNode != null) {
				runGenotype(g.rightNode, event);
			}
		}
		else if(result == 1)
			runGenotype(g.leftNode, event);
		else if(result == -1)
			runGenotype(g.rightNode, event);
		if(event) {
			execute();
			while(getDistanceRemaining() > 0 || getTurnRemaining() > 0) {
				execute();
			}	
		}
	}
	
	private int handleBehaviour(GenoKey gk) {
		int power = gk.getPower();
		switch(gk.getKey()) {
		case TURN_LEFT:
			setTurnLeft(power);
			break;
		case TURN_RIGHT:
			setTurnRight(power);
			break;
		case MOVE_FORWARD:
			setAhead(power);
			break;
		case MOVE_BACKWARD:
			setBack(power);
			break;
		case TURRET_LEFT:
			setTurnGunLeft(power);
			break;
		case TURRET_RIGHT:
			setTurnGunRight(power);
			break;
		case FIRE_GUN:
			setFire(power);
			break;
		case HEALTH:
			getEnergy();
		}
		return 0;
	}
	
	private int performBehaviour(GenoKey gk) {
		int power = gk.getPower();
		switch(gk.getKey()) {
		case TURN_LEFT:
			turnLeft(power);
			break;
		case TURN_RIGHT:
			turnRight(power);
			break;
		case MOVE_FORWARD:
			ahead(power);
			break;
		case MOVE_BACKWARD:
			back(power);
			break;
		case TURRET_LEFT:
			turnGunLeft(power);
			break;
		case TURRET_RIGHT:
			turnGunRight(power);
			break;
		case FIRE_GUN:
			fire(power);
			break;
		case HEALTH:
			getEnergy();
			break;
		}
		return 0;
	}
	
	@Override
	public void onScannedRobot(ScannedRobotEvent e) {
		if(onScan != null)
			runGenotype(onScan, true);
	}
	
	@Override
	public void onHitByBullet(HitByBulletEvent e) {
		if(onTake != null)
			runGenotype(onTake, true);
	}
	
	@Override
	public void onBulletHit(BulletHitEvent e) {
		roundDamage += (int)e.getBullet().getPower();
		
		if(onHit != null)
			runGenotype(onHit, true);
	}
	
	@Override
	public void onRoundEnded(RoundEndedEvent event) {
		// save robot scores and statistics
		StringBuilder sb = new StringBuilder();
		sb.append((roundDamage + ((int) getEnergy())) + ";");
		sb.append(behaviours.getStorageString());
		sb.append(",");
		sb.append(onScan.getStorageString());
		sb.append(",");
		sb.append(onHit.getStorageString());
		sb.append(",");
		sb.append(onTake.getStorageString());
		sb.append(",");
		sb.append(System.lineSeparator());
		try {
			PrintStream ps = new PrintStream(new RobocodeFileOutputStream(getDataFile(scoreFilename)));
			ps.print(sb.toString());
			ps.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onStatus(StatusEvent e) {	
	}
	
	@Override
	public void onBattleEnded(BattleEndedEvent e) {	
	}
	
	@Override
	public void onWin(WinEvent e) {
	}
	
	@Override
	public void onDeath(DeathEvent e) {	
	}
	
	private int randomNum(int low, int high) {
		Random r = new Random();
		int result = r.nextInt(high-low) + low;
		return result;
	}
}
