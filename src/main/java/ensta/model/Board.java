package ensta.model;

import ensta.model.ship.AbstractShip;
import ensta.model.ship.ShipState;
import ensta.util.ColorUtil;
import ensta.util.Orientation;

import java.io.Serializable;

public class Board implements IBoard, Serializable {

	private static final int DEFAULT_SIZE = 10;
	private int size;
	private Tile[][] grid;
	private String name;

	public Board(String name, int size) {
		this.name = name;
		this.size = size;
		this.grid = new Tile[size+1][size];

		for (int i = 1; i < size+1; i++)
			for (int j = 0; j < size; j++)
				grid[i][j] = new Tile();
	}

	public Board(String name) {
		this(name, DEFAULT_SIZE);
	}

	public void print() {
		int numberOfSpaces = ((int) Math.ceil(Math.log10(size)) + 2) +
							 2*size + 3 - "Navires :".length();
		System.out.format("Navires :%" + numberOfSpaces + "sFrappes:\n", "");

		numberOfSpaces = (int) Math.ceil(Math.log10(size)) + 2;

		System.out.format("%" + numberOfSpaces + "s", "");
		for (char i = 'A'; i < 'A' + size; i++) {
			System.out.print(i + " ");
		}

		System.out.format("%" + (3 + numberOfSpaces) + "s", "");
		for (char i = 'A'; i < 'A' + size; i++) {
			System.out.print(i + " ");
		}
		System.out.print("\n");

		for (int i = 1; i < this.size+1; i++) {
			System.out.format("%-" + numberOfSpaces + "d", i);
			for (int j = 0; j < this.size; j++) {
				if (grid[i][j].hasShip()) {
					System.out.print(grid[i][j].getShipState().toString() + " ");
				}
				else {
					System.out.print(". ");
				}
			}

			System.out.format("   %-" + numberOfSpaces + "d", i);
			for (int j = 0; j < this.size; j++) {
				System.out.print(grid[i][j].isHit() == null ? ". " :
						grid[i][j].isHit() ? ColorUtil.colorize("X ", ColorUtil.Color.RED) : "X ");
			}
			System.out.print("\n");
		}

	}

	public Tile getTile(Coords coords) {
		return grid[coords.getY()][coords.getX()];
	}
	@Override
	public int getSize() {
		return size;
	}

	@Override
	public boolean putShip(AbstractShip ship, Coords coords) {
		if (canPutShip(ship, coords)) {
			for (int i = 0; i < ship.getLength(); i++) {
				getTile(coords).putShip(ship);

				switch(ship.getOrientation()) {
					case EAST:
						coords.setX(coords.getX() + 1);
						break;
					case SOUTH:
						coords.setY(coords.getY() + 1);
						break;
					case WEST:
						coords.setX(coords.getX() - 1);
						break;
					case NORTH:
						coords.setY(coords.getY() - 1);
						break;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean hasShip(Coords coords) {
		return getTile(coords).hasShip();
	}

	@Override
	public void setHit(boolean hit, Coords coords) {
		getTile(coords).setHit(hit);
	}

	@Override
	public Boolean getHit(Coords coords) {
		return getTile(coords).isHit();
	}

	@Override
	public Hit sendHit(Coords coords) {
		Tile t = getTile(coords);
		if (!t.hasShip()) {
			return Hit.MISS;
		}
		else {
			ShipState s = t.getShipState();
			s.addStrike();
			if (s.isSunk())
				return Hit.fromInt(s.getShip().getLength());
			else
				return Hit.STRIKE;
		}
	}

	public boolean canPutShip(AbstractShip ship, Coords coords) {
		Orientation o = ship.getOrientation();
		int dx = 0, dy = 0;
		if (coords.getY() < 1 || coords.getX() < 0)
			return false;
		else if (o == Orientation.EAST) {
			if (coords.getX() + ship.getLength() - 1 >= this.size) {
				return false;
			}
			dx = 1;
		} else if (o == Orientation.SOUTH) {
			if (coords.getY() + ship.getLength() - 1 >= this.size+1) {
				return false;
			}
			dy = 1;
		} else if (o == Orientation.NORTH) {
			if (coords.getY() + 1 - ship.getLength() < 1) {
				return false;
			}
			dy = -1;
		} else if (o == Orientation.WEST) {
			if (coords.getX() + 1 - ship.getLength() < 0) {
				return false;
			}
			dx = -1;
		}

		Coords iCoords = new Coords(coords);

		for (int i = 0; i < ship.getLength(); ++i) {
			if (this.hasShip(iCoords)) {
				return false;
			}
			iCoords.setX(iCoords.getX() + dx);
			iCoords.setY(iCoords.getY() + dy);
		}

		return true;
	}

	public Tile[][] getGrid() {
		return grid;
	}

	public void setGrid(Tile[][] grid) {
		this.grid = grid;
	}

	public void copy(Board board) {
		this.grid = board.grid;
		this.name = board.name;
		this.size = board.size;
	}

}
