import java.util.List;
import java.util.Scanner;
import java.util.Arrays;
import java.text.DecimalFormat;
import java.util.Random;


public class rpsGame {
	
	public enum Item {
		ROCK, PAPER, SCISSORS, LIZARD, SPOCK;
		
		public List<Item> losesTo;
		
		public boolean losesTo(Item other) {
			return losesTo.contains(other);
		}
		
		static {
			SCISSORS.losesTo = Arrays.asList(ROCK, SPOCK);
			ROCK.losesTo = Arrays.asList(PAPER, SPOCK);
			PAPER.losesTo = Arrays.asList(PAPER, LIZARD);
			SPOCK.losesTo = Arrays.asList(PAPER, LIZARD);
			LIZARD.losesTo = Arrays.asList(SCISSORS, ROCK);
		}

	}
	
	private static DecimalFormat DECIMAL_FORMATTER = new DecimalFormat(".##");
	public static final Random RANDOM = new Random();
	
	private int[] stats = new int[] {0, 0, 0};
	private int [][] markovChain;
	private int nbThrows = 0;
	private Item last = null;
	
	private void init() {
		
		int length = Item.values().length;
		markovChain = new int [length][length];
		
		for (int i = 0; i < length; i++) {
			
			for (int j = 0; j < length; j++) {
				
				markovChain[i][j] = 0;
				
			}
			
		}
		
	}
	
	private void updateMarkovChain(Item prev, Item next) {
		markovChain[prev.ordinal()][next.ordinal()]++;
	}
	
	private Item nextMove(Item prev) {
		if (nbThrows < 1) {
			return Item.values()[RANDOM.nextInt(Item.values().length)];
		}
		
		int nextIndex = 0;
		
		for (int i = 0; i < Item.values().length; i++) {
			int prevIndex = prev.ordinal();
			
			if (markovChain[prevIndex][i] > markovChain[prevIndex][nextIndex]) {
				nextIndex = i;
			}
		}
		
		Item predictedNext = Item.values()[nextIndex];
		
		List<Item> losesTo = predictedNext.losesTo;
		return losesTo.get(RANDOM.nextInt(losesTo.size()));
}
	
	public void play() {
		init();
		
		Scanner in = new Scanner(System.in);
		System.out.println("Make your choice: ");
		
		while (in.hasNextLine()) {
			
			String input = in.nextLine();
			
			if("STOP".equals(input))
				break;
			
			Item choice;
			
			try {
				choice = Item.valueOf(input.toUpperCase());
			} catch (Exception e) {
				System.out.println("invalid");
				continue;
			}
			
			Item aiChoice = nextMove(last);
			nbThrows++;
			
			if(last != null) {
				updateMarkovChain(last, choice);
			}
			
			last = choice;
			
			System.out.println("Computer Choice: " + aiChoice);
			
			if (aiChoice.equals(choice)) {
				System.out.println(" ==> Tie !\n ");
				stats[1]++;
			} else if(aiChoice.losesTo(choice)) {
				System.out.println(" ==> You win !\n");
				stats[0]++;
			} else {
				System.out.println(" ==> You Lose !\n");
				stats[2]++;
			}
			
			System.out.println("Make your choice: ");
			
		}
		
		in.close();
		
		System.out.println("/n");
		System.out.println("Win Stats");
		int total = stats[0] + stats[1] + stats[2];
		System.out.println("You: " + stats[0] + " - " + 
				DECIMAL_FORMATTER.format(stats[0] / (float) total *100f) + "%");
		System.out.println("Tie: " + stats[1] + " - " + 
				DECIMAL_FORMATTER.format(stats[1] / (float) total *100f) + "%");
		System.out.println("Computer: " + stats[2] + " - " + 
				DECIMAL_FORMATTER.format(stats[2] / (float) total *100f) + "%");
		
	}
	
	public static void main(String[] args) {
		rpsGame rpsls = new rpsGame();
		rpsls.play();
	}
	
}
