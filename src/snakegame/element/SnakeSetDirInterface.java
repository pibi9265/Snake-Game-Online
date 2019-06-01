package snakegame.element;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface SnakeSetDirInterface extends Remote{
    public void setDir(int roomNumber, int id, char dir) throws RemoteException;
}
