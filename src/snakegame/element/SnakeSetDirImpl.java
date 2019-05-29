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
        snakes.get(id-1).setDir(dir);
    }
}
