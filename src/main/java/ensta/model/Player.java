package ensta.model;

import java.io.*;
import java.util.List;

import ensta.model.ship.AbstractShip;
import ensta.util.Orientation;
import ensta.view.InputHelper;

public class Player implements Serializable {
	/*
	 * ** Attributs
	 */
	protected Board board;
	protected Board opponentBoard;
	private int destroyedCount;
	protected AbstractShip[] ships;
	private boolean lose;
	private String name;
	private static final long serialVersionUID = 233128818881815240L;

	/*
	 * ** Constructeur
	 */
	public Player(String name, Board board, Board opponentBoard, List<AbstractShip> ships) {
		this.name = name;
		this.setBoard(board);
		this.ships = ships.toArray(new AbstractShip[0]);
		this.opponentBoard = opponentBoard;
	}

	public Player() {}

	/*
	 * ** Méthodes
	 */

	/**
	 * Read keyboard input to get ships coordinates. Place ships on given
	 * coodrinates.
	 */
	public void putShips() {
		boolean done;
		int i = 0;
		board.print();
		do {
			AbstractShip ship = ships[i];
			String msg = String.format("%s placer %d : %s(%d)", name, i + 1, ship.getName(), ship.getLength());
			System.out.println(msg);
			InputHelper.ShipInput res = InputHelper.readShipInput();

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
			boolean success = this.getBoard().putShip(ship, new Coords(res.x, res.y));

			if (success)
				++i;
			else
				System.out.println("Position non valide");

			done = i == 5;

			board.print();
		} while (!done);
	}

	public Hit sendHit(Coords coords) {
		boolean done = false;
		Hit hit = null;
		do {
			try {
				System.out.println(name + " où frapper?");
				InputHelper.CoordInput hitInput = InputHelper.readCoordInput();
				Coords c = new Coords(hitInput.x, hitInput.y);
				hit = this.opponentBoard.sendHit(c);
				coords.setCoords(c);
				done = true;
			}
			catch(Exception ignored) {}
		} while (!done);

		return hit;
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

	public void copyBoard(Board board) {
		this.board.copy(board);
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

	public void printBoard() {
		board.print();
	}

	/**
	 * Externalizable methods implementation, this interface works with serializable exposing serializable methods
	 */

//	@Override
//	public void writeExternal(ObjectOutput out) throws IOException {
//		out.writeObject(board);
//		out.writeObject(opponentBoard);
//		out.writeUTF(name);
//		out.writeBoolean(lose);
//		out.writeInt(destroyedCount);
//	}
//
//	@Override
//	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
//		board = (Board) in.readObject();
//		opponentBoard = (Board) in.readObject();
//		name = in.readUTF();
//		lose = in.readBoolean();
//		destroyedCount = in.readInt();
//	}
}
