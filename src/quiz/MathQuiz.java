package quiz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

/**
 * 
 * This program was made to help hone simple math skills.
 * 
 * @author <b>Mark Hudock</b>
 *
 */
public class MathQuiz {

	public static void main(String args[]) {
		new MathQuiz();
	}

	public MathQuiz() {
		askForDifficulty();
		System.out.println("Difficulty: " + getDifficultyName(getDifficulty()) + " (" + getDifficulty() + ").");
		System.out.println("Time limit: " + (TIME_LIMIT / 1000) + " seconds.");
		System.out.println();
		startQuiz();

		System.out.println("Time has expired.");
		System.out.println();
		printResults();
	}

	/**
	 * Executes the quiz until the time has expired.
	 */
	private void startQuiz() {
		startTimer();
		while ((System.currentTimeMillis() - timeStart) < TIME_LIMIT) {
			try {
				System.out.print(getEquation() + "\n");
				startEquationTime();
				numberGuessed = Integer.parseInt(input.readLine());
				equationAttempts++;
				totalAttempts++;
				checkGuess();
			} catch (NumberFormatException e) {
				System.out.println("Invalid input.\n");
				incorrect++;
				resetEquationAttempts();
				continue;
			} catch (IOException e) {
				System.exit(1);
			}
		}
	}

	/**
	 * Checks for correctness of the answer for the equation supplied by the user.
	 */
	private void checkGuess() {
		while (numberGuessed != correctAnswer) { // Does not finish until answer is correct.
			try {
				incorrect++;
				equationAttempts++;
				totalAttempts++;
				if (equationAttempts >= 5) { // Stops after 5 incorrect attempts have been made.
					System.out.println("The answer was: " + correctAnswer + ".");
					System.out.println("Time left: " + (getTimeLeft() / 1000) + " seconds.");
					System.out.println("Correct: " + (getCorrect()) + ".\n");
					resetEquationAttempts();
					return;
				}
				numberGuessed = Integer.parseInt(input.readLine()); // Asks the user for the correct answer again.
			} catch (NumberFormatException e) {
				System.out.println("Invalid input.\n");
				incorrect++;
				resetEquationAttempts();
				return;
			} catch (IOException e) {
				System.exit(1);
			}
		}
		if (numberGuessed == correctAnswer) { // Correct answer.
			System.out.println("Correct! The answer is: " + correctAnswer + ".");
			System.out.println(
					"It took you " + getEquationTime() + " seconds and " + getEquationAttempts() + " attempts.");
			System.out.println("Time left: " + (getTimeLeft() / 1000) + " seconds.");
			System.out.println("Correct: " + (++correct) + ".\n");
			resetEquationAttempts();
		}
	}

	/**
	 * Prompts the user to enter a number signifying a difficulty.
	 */
	private void askForDifficulty() {
		boolean done = false;
		System.out.println("0 Basic");
		System.out.println("1 Easy");
		System.out.println("2 Medium");
		System.out.println("3 Hard");
		while (!done) {
			System.out.println();
			System.out.print("Select difficulty: ");
			int tempDifficulty = 0;
			try {
				tempDifficulty = Integer.parseInt(input.readLine());
			} catch (NumberFormatException nfe) {
				System.out.println("Please enter 0, 1, 2 or 3 for difficulty.");
				continue;
			} catch (IOException ioe) {
				ioe.printStackTrace();
				System.exit(1);
			}
			if (tempDifficulty < 0 || tempDifficulty >= 4) { // Numbers out of range.
				System.out.println("Please enter 0, 1, 2 or 3 for difficulty.");
				continue;
			}
			setDifficulty(tempDifficulty);
			done = true;
		}
	}

	/**
	 * Sets the current difficulty of the program specified by user.
	 * 
	 * @param difficulty The setting of difficulty.
	 */
	private void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	/**
	 * Gets the current difficulty of the program that was specified by user.
	 */
	private int getDifficulty() {
		return difficulty;
	}

	/**
	 * Gets the <b>String</b> equivalent of the specified difficulty.
	 * 
	 * @param diff The difficulty as an integer
	 * @return The difficulty as a String
	 */
	private String getDifficultyName(int difficulty) {
		switch (difficulty) {
			case 0:
				return "Basic";
			case 1:
				return "Easy";
			case 2:
				return "Medium";
			case 3:
				return "Hard";
			default:
				return "UNKNOWN";
		}
	}

	/**
	 * Gets the maximum integer used for generating equations.
	 * 
	 * @param difficulty The difficulty of the formula specified by the user.
	 * @return The maximum integer for the equation.
	 */
	private int getMaxInteger(int difficulty) {
		switch (difficulty) {
			case 0:
				return 9;
			case 1:
				return 50;
			case 2:
				return 100;
			case 3:
				return 200;
			default:
				return 0;
		}
	}

	/**
	 * Creates the equation.
	 * 
	 * @return The finished equation.
	 */
	private String getEquation() {
		String symbol;
		int[] numbers;
		int firstHalf = 0;
		Random random = new Random();
		numbers = new int[3];
		numbers[0] = random.nextInt(getMaxInteger(getDifficulty()));
		numbers[1] = random.nextInt(getMaxInteger(getDifficulty()));
		numbers[2] = random.nextInt(getMaxInteger(getDifficulty()));
		symbol = random.nextInt(2) == 0 ? "+" : "-";
		if (getDifficulty() == 0) {
			correctAnswer = (symbol.equals("+") ? numbers[0] + numbers[1] : numbers[0] - numbers[1]);
			return numbers[0] + " " + symbol + " " + numbers[1];
		}
		firstHalf = numbers[0] + numbers[1];
		correctAnswer = (symbol.equals("+") ? firstHalf + numbers[2] : firstHalf - numbers[2]);
		/*
		 * System.out.println("The answer is " + answer + ". (" + (symbol.equals("+") ?
		 * "" + firstHalf + " + "
		 * + numbers[2] + ")" : "" + firstHalf + " - " + numbers[2] + ")"));
		 */
		return numbers[0] + " + " + numbers[1] + " " + symbol + " " + numbers[2];
	}

	/**
	 * Prints out the final calculated statistics of the quiz.
	 */
	private void printResults() {
		System.out.println("Difficulty: " + getDifficultyName(getDifficulty()) + "(" + getDifficulty() + ")");
		System.out.println("Correct: " + getCorrect() + ".");
		System.out.println("Incorrect: " + getIncorrect() + ".");
		System.out.println("Accuracy: " + getAccuracy() + "%.");
		System.out.println("Average answer time: " + (TIME_LIMIT / getCorrect()) + " ms.");
		System.out.println();
	}

	/**
	 * Starts the timer for the quiz.
	 */
	private void startTimer() {
		timeStart = System.currentTimeMillis();
	}

	/**
	 * Starts the time for tracking how long the equation takes to solve.
	 */
	private void startEquationTime() {
		equationStart = System.currentTimeMillis();
	}

	/**
	 * Calculates how long the user took to correctly answer the equation in
	 * seconds.
	 * 
	 * @return The amount of time the user took to correctly answer the equation.
	 */
	private long getEquationTime() {
		return ((System.currentTimeMillis() - equationStart) / 1000);
	}

	/**
	 * Gets the amount of time left for the quiz.
	 * 
	 * @return The amount of time left in milliseconds.
	 */
	private long getTimeLeft() {
		return TIME_LIMIT - (System.currentTimeMillis() - timeStart);
	}

	/**
	 * Gets the amount of attempts by the user for the current equation.
	 * 
	 * @return The amount of attempts made by the user for the current equation.
	 */
	private int getEquationAttempts() {
		return equationAttempts;
	}

	/**
	 * Resets the amount of attempts for the current equation to 0.
	 */
	private void resetEquationAttempts() {
		equationAttempts = 0;
	}

	/**
	 * Gets the current amount of attempts made by the user for the entire quiz.
	 * 
	 * @return The amount of attempts made by the user for the entire quiz.
	 */
	private int getTotalAttempts() {
		return totalAttempts;
	}

	/**
	 * Gets the current amount of equations answered correctly by the user.
	 * 
	 * @return The amount of equations answered correctly.
	 */
	private int getCorrect() {
		return correct;
	}

	/**
	 * Gets the current amount of equations answered incorrectly by the user.
	 * 
	 * @return The amount of equations answered incorrectly.
	 */
	private int getIncorrect() {
		return incorrect;
	}

	/**
	 * Gets the accuracy of correct equations as a percentage.
	 * 
	 * @return The calculated accuracy as a percentage.
	 */
	private double getAccuracy() {
		NumberFormat formatter;
		formatter = DecimalFormat.getInstance();
		formatter.setMaximumFractionDigits(2);
		formatter.setRoundingMode(RoundingMode.HALF_UP);

		if (getTotalAttempts() <= 0) {
			return 0;
		}
		return Double.parseDouble(formatter.format(((double) getCorrect() / getTotalAttempts()) * 100));
	}

	private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

	private static final long TIME_LIMIT = 60000;
	private int correctAnswer = 0, numberGuessed = 0;
	private int equationAttempts = 0, totalAttempts = 0;
	private int difficulty = 0;
	private int correct = 0, incorrect = 0;
	private long equationStart;
	private long timeStart;

}