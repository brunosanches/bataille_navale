package ensta;

import ensta.ai.BattleShipsAI;
import ensta.model.Board;
import ensta.model.Coords;
import ensta.model.Hit;
import ensta.model.ship.*;

import java.util.Arrays;
import java.util.List;

public class TestGame {

    public static void main(String[] args) {
        Board b1 = new Board("Board 1");
        Board b2 = new Board("Board 2");

        BattleShipsAI ai1 = new BattleShipsAI(b1, b2);
        BattleShipsAI ai2 = new BattleShipsAI(b2, b1);

        AbstractShip[] shipList = new AbstractShip[] { new Destroyer(), new Submarine(),
                new Submarine(), new BattleShip(), new Carrier() };
        ai1.putShips(shipList);
        ai2.putShips(shipList);

        int shipCount1 = shipList.length ,shipCount2 = shipList.length;

        while (shipCount1 == shipList.length && shipCount2 == shipList.length) {
            Hit h;
            Coords c = new Coords();
            do {
                h = ai1.sendHit(c);

                if (h != Hit.MISS && h != Hit.STRIKE)
                    shipCount2--;

            } while(h != Hit.MISS);
            b1.print();

            if (shipCount2 == 0)
                break;

            do {
                h = ai2.sendHit(c);

                if (h != Hit.MISS && h != Hit.STRIKE)
                    shipCount1--;


            } while(h != Hit.MISS);
            b2.print();
        }

        if (shipCount2 == 0)
            System.out.println("Joeur 1 a gagné");
        else
            System.out.println("Joeur 2 a gagné");
    }
}
