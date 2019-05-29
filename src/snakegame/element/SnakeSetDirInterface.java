package snakegame.element;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SnakeSetDirInterface extends Remote{
    public void setDir(int id, char dir) throws RemoteException;
}
