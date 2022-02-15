package ensta.model;

import ensta.model.ship.AbstractShip;

import java.io.*;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class PlayerOnline extends Player implements Serializable {
    boolean local;
    boolean server;
    private transient static Socket socket = null;
    private transient ServerSocket serverSocket = null;
    private transient static ObjectOutputStream oos = null;
    private transient static ObjectInputStream ois = null;
    private transient int port;
    private static final long serialVersionUID = 6329323126807895L;

    public PlayerOnline(String name, Board board, Board opponentBoard, List<AbstractShip> ships, boolean isLocal,
                        boolean isServer) {
        super(name, board, opponentBoard, ships);
        this.local = isLocal;
        this.server = isServer;

        if (isLocal) {
            if (isServer) {
                port = 80;
                do{
                    try {
                        serverSocket = new ServerSocket(port);
                    }
                    catch (IOException i) {
                        port++;
                    }
                } while(serverSocket == null);

                try {
                    System.out.println("Use IP: " + Inet4Address.getLocalHost().getHostAddress());
                    System.out.println("  Port: " + port);
                    System.out.println("To connect");
                }
                catch (Exception e) {
                    System.out.println(e.getStackTrace());
                }

                try {
                    socket = serverSocket.accept();
                }
                catch(Exception e) {
                    e.printStackTrace();
                }

                System.out.println("Connected");

                try {
                    oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                    oos.flush();
                    ois = new ObjectInputStream(socket.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                Scanner scan = new Scanner(System.in);
                System.out.print("Enter the IP of the server: ");
                String ip = scan.next();

                System.out.print("Enter the port of the server: ");
                int port = scan.nextInt();

                try {
                    socket = new Socket(ip, port);
                    System.out.println("Connected");

                    oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                    oos.flush();
                    ois = new ObjectInputStream(socket.getInputStream());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public PlayerOnline() {}

    @Override
    public void putShips() {
        if (local) {
            super.putShips();
            try {
                oos.writeObject(getBoard());
                oos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {

            try {
                board.copy((Board) ois.readObject());

                // Maybe inefficient but it's needed to preserve the references
                // Other option, send each ship, coordinate and orientation over network
                // This option make necessary copy of the code putShip of the super class
                // Duplication of the code vs efficiency loss ?
                Set<AbstractShip> shipSet = new HashSet<AbstractShip>();

                Coords c = new Coords();
                for (int y = 1; y <= board.getSize(); y++) {
                    for (int x = 0; x < board.getSize(); x++) {
                        c.setX(x);
                        c.setY(y);
                        if (board.hasShip(c))
                            shipSet.add(board.getTile(c).getShip());
                    }
                }

                ships = shipSet.toArray(ships);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public void setLocal(boolean isLocal) {
        local = isLocal;
    }

    public boolean isLocal() {
        return local;
    }

    @Override
    public Hit sendHit(Coords coords) {
        Hit h = Hit.MISS;
        if (local) {
            h = super.sendHit(coords);
            try {
                oos.writeObject(h);
                oos.writeObject(coords);
                oos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else {
            try {
                h = (Hit) ois.readObject();
                coords.setCoords((Coords) ois.readObject());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            h = this.opponentBoard.sendHit(coords);
        }

        return h;
    }

    @Override
    public void printBoard() {
        if (local)
            board.print();
    }

    public void setConnection(boolean isServer) {
        server = isServer;
        if (isServer) {
            port = 80;
            do{
                try {
                    serverSocket = new ServerSocket(port);
                }
                catch (IOException i) {
                    port++;
                }
            } while(serverSocket == null);

            try {
                System.out.println("Use IP: " + Inet4Address.getLocalHost().getHostAddress());
                System.out.println("  Port: " + port);
                System.out.println("To connect");
            }
            catch (Exception e) {
                System.out.println(e.getStackTrace());
            }

            try {
                socket = serverSocket.accept();
            }
            catch(Exception e) {
                e.printStackTrace();
            }

            System.out.println("Connected");

            try {
                oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                oos.flush();
                ois = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Scanner scan = new Scanner(System.in);
            System.out.print("Enter the IP of the server: ");
            String ip = scan.next();

            System.out.print("Enter the port of the server: ");
            int port = scan.nextInt();

            try {
                socket = new Socket(ip, port);
                System.out.println("Connected");

                oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                oos.flush();
                ois = new ObjectInputStream(socket.getInputStream());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    @Override
//    public void writeExternal(ObjectOutput out) throws IOException {
//        super.writeExternal(out);
//        out.writeBoolean(local);
//    }
//
//    @Override
//    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
//        super.readExternal(in);
//        server = in.readBoolean();
//        local = in.readBoolean();
//
//        // Now need to initialize the network connection
//        if (local) {
//            if (server) {
//                port = 80;
//                do{
//                    try {
//                        serverSocket = new ServerSocket(port);
//                    }
//                    catch (IOException i) {
//                        port++;
//                    }
//                } while(serverSocket == null);
//
//                try {
//                    System.out.println("Use IP: " + Inet4Address.getLocalHost().getHostAddress());
//                    System.out.println("  Port: " + port);
//                    System.out.println("To connect");
//                }
//                catch (Exception e) {
//                    System.out.println(e.getStackTrace());
//                }
//
//                try {
//                    socket = serverSocket.accept();
//                }
//                catch(Exception e) {
//                    e.printStackTrace();
//                }
//
//                System.out.println("Connected");
//
//                try {
//                    oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
//                    oos.flush();
//                    ois = new ObjectInputStream(socket.getInputStream());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            else {
//                Scanner scan = new Scanner(System.in);
//                System.out.print("Enter the IP of the server: ");
//                String ip = scan.next();
//
//                System.out.print("Enter the port of the server: ");
//                int port = scan.nextInt();
//
//                try {
//                    socket = new Socket(ip, port);
//                    System.out.println("Connected");
//
//                    oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
//                    oos.flush();
//                    ois = new ObjectInputStream(socket.getInputStream());
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }


}
