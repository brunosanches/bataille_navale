package ensta.model;

import java.io.Serializable;
import java.util.List;

import ensta.model.ship.AbstractShip;
import ensta.util.Orientation;
import ensta.view.InputHelper;
import javafx.util.Pair;

public class Player {
	/*
	 * ** Attributs
	 */
	private Board board;
	protected Board opponentBoard;
	private int destroyedCount;
	protected AbstractShip[] ships;
	private boolean lose;

	/*
	 * ** Constructeur
	 */
	public Player(Board board, Board opponentBoard, List<AbstractShip> ships) {
		this.setBoard(board);
		this.ships = ships.toArray(new AbstractShip[0]);
		this.opponentBoard = opponentBoard;
	}

	/*
	 * ** Méthodes
	 */

	/**
	 * Read keyboard input to get ships coordinates. Place ships on given
	 * coodrinates.
	 */
	public void putShips() {
		boolean done = false;
		int i = 0;

		do {
			AbstractShip ship = ships[i];
			String msg = String.format("placer %d : %s(%d)", i + 1, ship.getName(), ship.getLength());
			System.out.println(msg);
			InputHelper.ShipInput res = InputHelper.readShipInput();
			// TODO set ship orientation
			Orientation o;
			switch (res.orientation) {
				case "north":
					o = Orientation.NORTH;
					break;
				case "east":
					o = Orientation.EAST;
					break;
				case "south":
					o = Orientation.SOUTH;
					break;
				default:
					o = Orientation.WEST;
					break;
			}

			ship.setOrientation(o);

			// TODO put ship at given position
			boolean success = this.getBoard().putShip(ship, new Coords(res.x, res.y));
			// TODO when ship placement successful
			if (success)
				++i;
			else
				System.out.println("Position non valide");

			done = i == 5;

			board.print();
		} while (!done);
	}

	public Pair<Hit, Coords> sendHit() {
		boolean done = false;
		Hit hit = null;
		Pair<Hit, Coords> p;
		do {
			System.out.println("où frapper?");
			InputHelper.CoordInput hitInput = InputHelper.readCoordInput();
			Coords coords = new Coords(hitInput.x, hitInput.y);
			// TODO call sendHit on this.opponentBoard
			hit = this.opponentBoard.sendHit(coords);
			// TODO : Game expects sendHit to return BOTH hit result & hit coords.
			// return hit is obvious. But how to return coords at the same time ?
			p = new Pair<>(hit, coords);
			done = true;
		} while (!done);

		return p;
	}

	public AbstractShip[] getShips() {
		return ships;
	}

	public void setShips(AbstractShip[] ships) {
		this.ships = ships;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public int getDestroyedCount() {
		return destroyedCount;
	}

	public void setDestroyedCount(int destroyedCount) {
		this.destroyedCount = destroyedCount;
	}

	public boolean isLose() {
		return lose;
	}

	public void setLose(boolean lose) {
		this.lose = lose;
	}
}
