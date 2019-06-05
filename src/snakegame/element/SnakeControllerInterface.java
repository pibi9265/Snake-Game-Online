package snakegame.element;

import java.rmi.Remote;
import java.rmi.RemoteException;

import java.util.ArrayList;

import snakegame.element.Snake;

public interface SnakeControllerInterface extends Remote {
    public Snake getSnake(int index) throws RemoteException;
    public ArrayList<Snake> getSnakes() throws RemoteException;
    public Apple getApple() throws RemoteException;
    public int getSize() throws RemoteException;
    public void setDir(int index, char dir) throws RemoteException;
    public int addPlayer(String playerName) throws RemoteException;
    public void removePlayer(int index) throws RemoteException;
    public ArrayList<String> getPlayerNames() throws RemoteException;
}
