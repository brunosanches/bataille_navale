package ensta.model;

import ensta.model.ship.AbstractShip;
import ensta.util.Orientation;

public class Board implements IBoard {

	private static final int DEFAULT_SIZE = 10;
	private int size;
	private Tile[][] grid;
	private String name;

	public Board(String name, int size) {
		this.name = name;
		this.size = size;
		this.grid = new Tile[size][size];

		for (int i = 0; i < size; i++)
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

		for (int i = 0; i < this.size; i++) {
			System.out.format("%-" + numberOfSpaces + "d", i+1);
			for (int j = 0; j < this.size; j++) {
				if (grid[i][j].hasShip()) {
					System.out.print(grid[i][j].getShip().getLabel() + " ");
				}
				else {
					System.out.print(". ");
				}
			}

			System.out.format("   %-" + numberOfSpaces + "d", i+1);
			for (int j = 0; j < this.size; j++) {
				System.out.print(grid[i][j].isHit() ? "x" : "." + " ");
			}
			System.out.print("\n");
		}

	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public boolean putShip(AbstractShip ship, Coords coords) {
		if (canPutShip(ship, coords)) {
			for (int i = 0; i < ship.getLength(); i++) {
				grid[coords.getY()][coords.getX()].putShip(ship);

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
		return grid[coords.getY()][coords.getX()].hasShip();
	}

	@Override
	public void setHit(boolean hit, Coords coords) {
		grid[coords.getY()][coords.getX()].setHit(hit);
	}

	@Override
	public Boolean getHit(Coords coords) {
		return grid[coords.getY()][coords.getX()].isHit();
	}

	@Override
	public Hit sendHit(Coords res) {
		return null;
	}

	public boolean canPutShip(AbstractShip ship, Coords coords) {
		Orientation o = ship.getOrientation();
		int dx = 0, dy = 0;
		if (o == Orientation.EAST) {
			if (coords.getX() + ship.getLength() >= this.size) {
				return false;
			}
			dx = 1;
		} else if (o == Orientation.SOUTH) {
			if (coords.getY() + ship.getLength() >= this.size) {
				return false;
			}
			dy = 1;
		} else if (o == Orientation.NORTH) {
			if (coords.getY() + 1 - ship.getLength() < 0) {
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
}
