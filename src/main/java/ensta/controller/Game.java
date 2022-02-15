package ensta.controller;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import ensta.ai.PlayerAI;
import ensta.model.*;
import ensta.model.ship.AbstractShip;
import ensta.model.ship.BattleShip;
import ensta.model.ship.Carrier;
import ensta.model.ship.Destroyer;
import ensta.model.ship.Submarine;
import ensta.util.ColorUtil;

public class Game {

	/*
	 * *** Constante
	 */
	public static final File SAVE_FILE = new File("savegame.dat");

	/*
	 * *** Attributs
	 */
	private Player player1;
	private Player player2;
	private Scanner sin;
	private boolean isMultiplayerOnline = false;

	/*
	 * *** Constructeurs
	 */
	public Game() {
		sin = new Scanner(System.in);
	}

	public Game init() {
		if (!loadSave()) {

			Board board1 = new Board("Board1");
			Board board2 = new Board("Board2");

			char r;
			do{
				System.out.println("2 players mode ? y/[n]");
				r = sin.next().charAt(0);
			} while(r != 'y' && r != 'n' && r != '\n');

			if (r == 'y') {
				char p;
				do{
					System.out.println("Is online ? y/[n]");
					p = sin.next().charAt(0);
				} while(p != 'y' && p != 'n' && p != '\n');

				if (p == 'y') {
					isMultiplayerOnline = true;

					char c;
					do {
						System.out.println("Are you the server ? y/[n]");
						c = sin.next().charAt(0);
					} while (c != 'y' && c != 'n' && c != '\n');

					if (c == 'y') {
						this.player1 = new PlayerOnline("Player1", board1, board2, createDefaultShips(),
								true, true);
						this.player2 = new PlayerOnline("Player2", board2, board1, createDefaultShips(),
								false, false);


					} else {
						this.player1 = new PlayerOnline("Player1", board1, board2, createDefaultShips(),
								false, true);
						this.player2 = new PlayerOnline("Player2", board2, board1, createDefaultShips(),
								true, false);

					}
				}
				else {
					this.player1 = new Player("Player 1", board1, board2, createDefaultShips());
					this.player2 = new Player("Player 2", board2, board1, createDefaultShips());
				}

			}
			else {
				this.player1 = new Player("Player 1", board1, board2, createDefaultShips());
				this.player2 = new PlayerAI(board2, board1, createDefaultShips());
			}

			this.player1.putShips();
			this.player2.putShips();
		}
		return this;
	}

	/*
	 * *** Méthodes
	 */
	public void run() {
		Hit hit;

		// main loop
		player1.printBoard();
		boolean done;
		do {
			Coords coords1 = new Coords();
			hit = player1.sendHit(coords1);
			boolean strike = hit != Hit.MISS;
			player1.getBoard().setHit(strike, coords1);

			if (strike)
				player2.printBoard();

			done = updateScore();
			player1.printBoard();
			System.out.println(makeHitMessage(false /* outgoing hit */, coords1, hit));

			save();

			if (!done && !strike) {
				player2.printBoard();
				do {
					Coords coords2 = new Coords();
					hit = player2.sendHit(coords2);

					strike = hit != Hit.MISS;

					player2.getBoard().setHit(strike, coords2);

					if (strike) {
						player1.printBoard();
					}
					player2.printBoard();

					System.out.println(makeHitMessage(true /* incoming hit */, coords2, hit));
					done = updateScore();

					if (!done) {
						save();
					}
				} while (strike && !done);
			}

		} while (!done);

		SAVE_FILE.delete();

		System.out.println(String.format("joueur %d gagne", player1.isLose() ? 2 : 1));
		sin.close();
	}

	private void save() {
		try {
			if (!SAVE_FILE.exists()) {
				SAVE_FILE.getAbsoluteFile().getParentFile().mkdirs();
			}

			FileOutputStream fos = new FileOutputStream(SAVE_FILE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeBoolean(isMultiplayerOnline);
			oos.writeObject(player1);
			oos.writeObject(player2);

			oos.close();
			fos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean loadSave() {
		if (SAVE_FILE.exists()) {
			try {
				FileInputStream fis = new FileInputStream(SAVE_FILE);
				ObjectInputStream ois = new ObjectInputStream(fis);

				isMultiplayerOnline = ois.readBoolean();
				player1 = (Player) ois.readObject();
				player2 = (Player) ois.readObject();

				if (isMultiplayerOnline) {
					char c;
					do{
						System.out.println("Are you the server ? y/[n]");
						c = sin.next().charAt(0);
					} while(c != 'y' && c != 'n' && c != '\n');

					if (c == 'y') {
						((PlayerOnline) player1).setLocal(true);
						((PlayerOnline) player1).setConnection(true);
						((PlayerOnline) player2).setLocal(false);
					}
					else {
						((PlayerOnline) player1).setLocal(false);
						((PlayerOnline) player2).setLocal(true);
						((PlayerOnline) player2).setConnection(false);
					}
				}

				ois.close();
				fis.close();
				return true;
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private boolean updateScore() {
		for (Player player : new Player[] { player1, player2 }) {
			int destroyed = 0;
			for (AbstractShip ship : player.getShips()) {
				if (ship.isSunk()) {
					destroyed++;
				}
			}

			player.setDestroyedCount(destroyed);
			player.setLose(destroyed == player.getShips().length);
			if (player.isLose()) {
				return true;
			}
		}
		return false;
	}

	private String makeHitMessage(boolean incoming, Coords coords, Hit hit) {
		String msg;
		ColorUtil.Color color = ColorUtil.Color.RESET;
		switch (hit) {
		case MISS:
			msg = hit.toString();
			break;
		case STRIKE:
			msg = hit.toString();
			color = ColorUtil.Color.RED;
			break;
		default:
			msg = hit.toString() + " coulé";
			color = ColorUtil.Color.RED;
		}
		msg = String.format("%s Frappe en %c%d : %s", incoming ? "<=" : "=>", ((char) ('A' + coords.getX())),
				(coords.getY()), msg);
		return ColorUtil.colorize(msg, color);
	}

	private static List<AbstractShip> createDefaultShips() {
		return Arrays.asList(new AbstractShip[] { new Destroyer(), new Submarine(), new Submarine(),
				new BattleShip(), new Carrier() });
	}
}
