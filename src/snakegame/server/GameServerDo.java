package snakegame.server;

import java.util.Random;

import snakegame.element.snake.*;
import snakegame.element.Apple;
import snakegame.element.Board;

public class GameServerDo implements Runnable{
    private Snake snake = new Snake(10, 10);
	private Apple apple = new Apple(20, 20);
    private Random random = new Random();

    public static void main(String[] args) {
        GameServerDo gameServerDo = new GameServerDo();
        new Thread(gameServerDo).start();
    }

    public void run(){
        try {
			while(true) {
				Thread.sleep(50);
				snake.keysPressed = false;
				move(snake);
				shiftDir(snake);
				collisionHB(snake, snake);
				collisionHA(snake, apple);
			}
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
    }

    private void move(Snake snake) {
		for(int i = 0;i < snake.maxLength;i++) {
			snake.body.get(i).x += snake.body.get(i).dx;
			if(snake.body.get(i).x > (Board.width/Board.grid) - 1) {
				snake.body.get(i).x = 0;
			}
			else if(snake.body.get(i).x < 0) {
				snake.body.get(i).x = (Board.width/Board.grid) - 1;
			}
			snake.body.get(i).y += snake.body.get(i).dy;
			if(snake.body.get(i).y > (Board.height/Board.grid) - 1) {
				snake.body.get(i).y = 0;
			}
			else if(snake.body.get(i).y < 0) {
				snake.body.get(i).y = (Board.height/Board.grid) - 1;
			}
		}
	}

    private void shiftDir(Snake snake) {
		for(int i = (snake.maxLength-1);i > 0;i--) {
			if(snake.body.get(i).dx != snake.body.get(i-1).dx) {
				snake.body.get(i).dx = snake.body.get(i-1).dx;
			}
			if(snake.body.get(i).dy != snake.body.get(i-1).dy) {
				snake.body.get(i).dy = snake.body.get(i-1).dy;
			}
		}
    }

    private void collisionHB(Snake h, Snake b) {
		if(b.maxLength!=1) {
			for(int i = 1;i < b.maxLength;i++) {
				if((h.body.get(0).x==b.body.get(i).x)&&(h.body.get(0).y==b.body.get(i).y)) {
					while(b.maxLength!=i) {
						b.body.remove(b.maxLength-1);
						b.maxLength--;
					}
					return;
				}
			}
		}
	}
	
	private void collisionHH(Snake h1, Snake h2) {
		if((h1.body.get(0).x==h2.body.get(0).x)&&(h1.body.get(0).y==h2.body.get(0).y)) {
			while(h1.maxLength!=1) {
				h1.body.remove(h1.maxLength-1);
				h1.maxLength--;
			}
			while(h2.maxLength!=1) {
				h2.body.remove(h2.maxLength-1);
				h2.maxLength--;
			}
		}
	}
	
	private boolean collisionSA(Snake s, Apple a){
		for(int i = 0;i < s.maxLength;i++) {
			if((a.x==s.body.get(i).x)&&(a.y==s.body.get(i).y)) {
				return true;
			}
		}
		return false;
	}

	private void collisionHA(Snake s, Apple a) {
		if((s.body.get(0).x==a.x)&&(s.body.get(0).y==a.y)) {
			s.body.add(new Part());
			s.maxLength++;
			s.body.get(s.maxLength-1).x = s.body.get(s.maxLength-2).x;
			s.body.get(s.maxLength-1).y = s.body.get(s.maxLength-2).y;
			s.body.get(s.maxLength-1).dx = s.body.get(s.maxLength-2).dx;
			s.body.get(s.maxLength-1).dy = s.body.get(s.maxLength-2).dy;
			s.body.get(s.maxLength-1).right = s.body.get(s.maxLength-2).right;
			s.body.get(s.maxLength-1).left = s.body.get(s.maxLength-2).left;
			s.body.get(s.maxLength-1).down = s.body.get(s.maxLength-2).down;
			s.body.get(s.maxLength-1).up = s.body.get(s.maxLength-2).up;
			s.body.get(s.maxLength-1).x -= s.body.get(s.maxLength-1).dx;
			s.body.get(s.maxLength-1).y -= s.body.get(s.maxLength-1).dy;
			while(true){
				a.x = random.nextInt(49);
				a.y = random.nextInt(49);
				if(!collisionSA(snake, apple)){
					break;
				}
			}
		}
	}
}
