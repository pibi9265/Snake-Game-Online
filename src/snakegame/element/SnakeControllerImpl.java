package snakegame.element;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import java.util.ArrayList;

import snakegame.element.SnakeControllerInterface;

import snakegame.rmisslsocketfactory.RMISSLServerSocketFactory;
import snakegame.rmisslsocketfactory.RMISSLClientSocketFactory;

import snakegame.element.Snake;
import snakegame.element.Apple;
import snakegame.element.Board;

public class SnakeControllerImpl extends UnicastRemoteObject implements SnakeControllerInterface {
	private static final long serialVersionUID = 1L;

	private ArrayList<Snake> snakes;
	private ArrayList<String> playerNames;
	private Apple apple;
	
	private int size;

	private int[] idList = new int[Board.maxPlayer];

	public SnakeControllerImpl(ArrayList<Snake> snakes, Apple apple) throws Exception {
		super(Board.DEFAULT_PORT, new RMISSLClientSocketFactory(), new RMISSLServerSocketFactory());

		this.snakes = snakes;
		this.apple = apple;
		playerNames = new ArrayList<String>();
		
		size = 0;

		for(int i = 0; i < Board.maxPlayer; i++){
			idList[i] = 0;
			playerNames.add(null);
		}
	}

	@Override
	public Snake getSnake(int index) {
		return snakes.get(index);
	}
	
	@Override
	public ArrayList<Snake> getSnakes() throws RemoteException {
		return snakes;
	}

	@Override
	public Apple getApple() {
		return apple;
	}

	@Override
	public int getSize() {
		return size;
	}

	public ArrayList<String> getPlayerNames() throws RemoteException {
		return playerNames;
	}
	
	@Override
	public void setDir(int id, char dir) {
		int index = -1;
		for(int i = 0; i < size; i++){
			if(snakes.get(i).id == id){
				index = i;
			}
		}
		if(index != -1){
			if (dir == 'R' && (!snakes.get(index).body.get(0).left)) {
				snakes.get(index).body.get(0).dx = 1;
				snakes.get(index).body.get(0).dy = 0;
				snakes.get(index).body.get(0).right = true;
				snakes.get(index).body.get(0).left = false;
				snakes.get(index).body.get(0).down = false;
				snakes.get(index).body.get(0).up = false;
			} else if (dir == 'L' && (!snakes.get(index).body.get(0).right)) {
				snakes.get(index).body.get(0).dx = -1;
				snakes.get(index).body.get(0).dy = 0;
				snakes.get(index).body.get(0).right = false;
				snakes.get(index).body.get(0).left = true;
				snakes.get(index).body.get(0).down = false;
				snakes.get(index).body.get(0).up = false;
			} else if (dir == 'D' && (!snakes.get(index).body.get(0).up)) {
				snakes.get(index).body.get(0).dx = 0;
				snakes.get(index).body.get(0).dy = 1;
				snakes.get(index).body.get(0).right = false;
				snakes.get(index).body.get(0).left = false;
				snakes.get(index).body.get(0).down = true;
				snakes.get(index).body.get(0).up = false;
			} else if (dir == 'U' && (!snakes.get(index).body.get(0).down)) {
				snakes.get(index).body.get(0).dx = 0;
				snakes.get(index).body.get(0).dy = -1;
				snakes.get(index).body.get(0).right = false;
				snakes.get(index).body.get(0).left = false;
				snakes.get(index).body.get(0).down = false;
				snakes.get(index).body.get(0).up = true;
			}
		}
	}

	@Override
	public int addPlayer(String playerName) throws RemoteException {
		if(size < Board.maxPlayer){
			for(int i = 0; i < Board.maxPlayer; i++){
				if(idList[i] == 0){
					idList[i] = 1;
					size++;
					snakes.add(new Snake(1, 1));
					snakes.get(snakes.size()-1).id = i;
					playerNames.set(i, playerName);
					
					if(i==0){
						snakes.get(snakes.size()-1).r = 255;
						snakes.get(snakes.size()-1).g = 0;
						snakes.get(snakes.size()-1).b = 0;
					}
					else if(i==1){
						snakes.get(snakes.size()-1).r = 0;
						snakes.get(snakes.size()-1).g = 255;
						snakes.get(snakes.size()-1).b = 0;
					}
					else if(i==2){
						snakes.get(snakes.size()-1).r = 0;
						snakes.get(snakes.size()-1).g = 0;
						snakes.get(snakes.size()-1).b = 255;
					}
					else if(i==3){
						snakes.get(snakes.size()-1).r = 255;
						snakes.get(snakes.size()-1).g = 0;
						snakes.get(snakes.size()-1).b = 255;
					}
					else if(i==4){
						snakes.get(snakes.size()-1).r = 255;
						snakes.get(snakes.size()-1).g = 255;
						snakes.get(snakes.size()-1).b = 0;
					}

					return i;
				}
			}
		}
		return -1;
	}

	@Override
	public void removePlayer(int id) throws RemoteException {
		for(int i = 0; i < size; i++){
			if(snakes.get(i).id == id){
				snakes.remove(i);
				playerNames.set(id, null);
				size--;
				idList[id] = 0;
			}
		}
	}
}
