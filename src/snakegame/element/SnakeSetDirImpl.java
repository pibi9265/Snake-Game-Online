package snakegame.element;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class SnakeSetDirImpl extends UnicastRemoteObject implements SnakeSetDirInterface{
    private static final long serialVersionUID = 1L;

    private ArrayList<Snake> snakes;

    public SnakeSetDirImpl(ArrayList<Snake> snakes) throws RemoteException {
        super();
        this.snakes = snakes;
    }

    @Override
    public void setDir(int id, char dir) {
        if(dir == 'R' && (!snakes.get(id-1).body.get(0).left)) {
			snakes.get(id-1).body.get(0).dx = 1;
			snakes.get(id-1).body.get(0).dy = 0;
			snakes.get(id-1).body.get(0).right = true;
			snakes.get(id-1).body.get(0).left = false;
			snakes.get(id-1).body.get(0).down = false;
			snakes.get(id-1).body.get(0).up = false;
		}
		else if(dir == 'L' && (!snakes.get(id-1).body.get(0).right)) {
			snakes.get(id-1).body.get(0).dx = -1;
			snakes.get(id-1).body.get(0).dy = 0;
			snakes.get(id-1).body.get(0).right = false;
			snakes.get(id-1).body.get(0).left = true;
			snakes.get(id-1).body.get(0).down = false;
			snakes.get(id-1).body.get(0).up = false;
		}
		else if(dir == 'D' && (!snakes.get(id-1).body.get(0).up)) {
			snakes.get(id-1).body.get(0).dx = 0;
			snakes.get(id-1).body.get(0).dy = 1;
			snakes.get(id-1).body.get(0).right = false;
			snakes.get(id-1).body.get(0).left = false;
			snakes.get(id-1).body.get(0).down = true;
			snakes.get(id-1).body.get(0).up = false;
		}
		else if(dir == 'U' && (!snakes.get(id-1).body.get(0).down)) {
			snakes.get(id-1).body.get(0).dx = 0;
			snakes.get(id-1).body.get(0).dy = -1;
			snakes.get(id-1).body.get(0).right = false;
			snakes.get(id-1).body.get(0).left = false;
			snakes.get(id-1).body.get(0).down = false;
            snakes.get(id-1).body.get(0).up = true;
        }
    }
}
