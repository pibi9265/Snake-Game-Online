package snakegame.element;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import snakegame.server.ServerWindow;

public class SnakeSetDirImpl extends UnicastRemoteObject implements SnakeSetDirInterface{
    private static final long serialVersionUID = 1L;

    public ArrayList<ServerWindow> rooms;

    public SnakeSetDirImpl(ArrayList<ServerWindow> rooms) throws RemoteException {
        super();
        this.rooms = rooms;
    }

    @Override
    synchronized public void setDir(int roomNumber, int id, char dir) {
    	ServerWindow room = rooms.get(roomNumber);
    	ArrayList<Snake> snakes = room.snakes;
    	
		if(dir == 'R' && (!snakes.get(id).body.get(0).left)) {
			snakes.get(id).body.get(0).dx = 1;
			snakes.get(id).body.get(0).dy = 0;
			snakes.get(id).body.get(0).right = true;
			snakes.get(id).body.get(0).left = false;
			snakes.get(id).body.get(0).down = false;
			snakes.get(id).body.get(0).up = false;
		}
		else if(dir == 'L' && (!snakes.get(id).body.get(0).right)) {
			snakes.get(id).body.get(0).dx = -1;
			snakes.get(id).body.get(0).dy = 0;
			snakes.get(id).body.get(0).right = false;
			snakes.get(id).body.get(0).left = true;
			snakes.get(id).body.get(0).down = false;
			snakes.get(id).body.get(0).up = false;
		}
		else if(dir == 'D' && (!snakes.get(id).body.get(0).up)) {
			snakes.get(id).body.get(0).dx = 0;
			snakes.get(id).body.get(0).dy = 1;
			snakes.get(id).body.get(0).right = false;
			snakes.get(id).body.get(0).left = false;
			snakes.get(id).body.get(0).down = true;
			snakes.get(id).body.get(0).up = false;
		}
		else if(dir == 'U' && (!snakes.get(id).body.get(0).down)) {
			snakes.get(id).body.get(0).dx = 0;
			snakes.get(id).body.get(0).dy = -1;
			snakes.get(id).body.get(0).right = false;
			snakes.get(id).body.get(0).left = false;
			snakes.get(id).body.get(0).down = false;
			snakes.get(id).body.get(0).up = true;
		}
    }
}
