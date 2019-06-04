package snakegame.element;

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
    private Apple apple;

    private int size;

    public SnakeControllerImpl(ArrayList<Snake> snakes, Apple apple) throws Exception {
        super(Board.DEFAULT_PORT, new RMISSLClientSocketFactory(), new RMISSLServerSocketFactory());

        this.snakes = snakes;
        this.apple = apple;

        size = 0;
    }

    @Override
    public Snake getSnake(int index) {
        return snakes.get(index);
    }

    @Override
    public Apple getApple() {
        return apple;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setDir(int index, char dir) {
		if(dir == 'R' && (!snakes.get(index).body.get(0).left)) {
			snakes.get(index).body.get(0).dx = 1;
			snakes.get(index).body.get(0).dy = 0;
			snakes.get(index).body.get(0).right = true;
			snakes.get(index).body.get(0).left = false;
			snakes.get(index).body.get(0).down = false;
			snakes.get(index).body.get(0).up = false;
		}
		else if(dir == 'L' && (!snakes.get(index).body.get(0).right)) {
			snakes.get(index).body.get(0).dx = -1;
			snakes.get(index).body.get(0).dy = 0;
			snakes.get(index).body.get(0).right = false;
			snakes.get(index).body.get(0).left = true;
			snakes.get(index).body.get(0).down = false;
			snakes.get(index).body.get(0).up = false;
		}
		else if(dir == 'D' && (!snakes.get(index).body.get(0).up)) {
			snakes.get(index).body.get(0).dx = 0;
			snakes.get(index).body.get(0).dy = 1;
			snakes.get(index).body.get(0).right = false;
			snakes.get(index).body.get(0).left = false;
			snakes.get(index).body.get(0).down = true;
			snakes.get(index).body.get(0).up = false;
		}
		else if(dir == 'U' && (!snakes.get(index).body.get(0).down)) {
			snakes.get(index).body.get(0).dx = 0;
			snakes.get(index).body.get(0).dy = -1;
			snakes.get(index).body.get(0).right = false;
			snakes.get(index).body.get(0).left = false;
			snakes.get(index).body.get(0).down = false;
			snakes.get(index).body.get(0).up = true;
        }
    }
}
