package tictactoe;

import java.util.Scanner;

public class Main {
	
	

	public static void main(String[] args) 
	{
		Scanner in = new Scanner(System.in);
		System.out.print("Enter Player X name: ");
		String playerX = in.nextLine().trim();
		System.out.print("Enter Player O name: ");
		String playerO = in.nextLine().trim();
		System.out.println("==================================");
		System.out.println("      WELCOME TO TIC TAC TOE      ");
		System.out.println("==================================");

		if (currentPlayer == 'X') {
		    SoundPlayer.play("assets/sounds/star.wav");
		} else {
		    SoundPlayer.play("assets/sounds/fart.wav");
		}

	}

}
