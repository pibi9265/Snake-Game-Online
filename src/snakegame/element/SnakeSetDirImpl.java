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
    	Snake curSnake = snakes.get(id-1);
    	
    	if(dir == 'R' && (!curSnake.body.get(0).left)) {
    		curSnake.body.get(0).dx = 1;
    		curSnake.body.get(0).dy = 0;
    		curSnake.body.get(0).right = true;
    		curSnake.body.get(0).left = false;
    		curSnake.body.get(0).down = false;
    		curSnake.body.get(0).up = false;
		}
		else if(dir == 'L' && (!curSnake.body.get(0).right)) {
			curSnake.body.get(0).dx = -1;
			curSnake.body.get(0).dy = 0;
			curSnake.body.get(0).right = false;
			curSnake.body.get(0).left = true;
			curSnake.body.get(0).down = false;
			curSnake.body.get(0).up = false;
		}
		else if(dir == 'D' && (!curSnake.body.get(0).up)) {
			curSnake.body.get(0).dx = 0;
			curSnake.body.get(0).dy = 1;
			curSnake.body.get(0).right = false;
			curSnake.body.get(0).left = false;
			curSnake.body.get(0).down = true;
			curSnake.body.get(0).up = false;
		}
		else if(dir == 'U' && (!curSnake.body.get(0).down)) {
			curSnake.body.get(0).dx = 0;
			curSnake.body.get(0).dy = -1;
			curSnake.body.get(0).right = false;
			curSnake.body.get(0).left = false;
			curSnake.body.get(0).down = false;
			curSnake.body.get(0).up = true;
		}
    }
}
