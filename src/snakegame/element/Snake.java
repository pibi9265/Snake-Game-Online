package snakegame.element;

import java.io.Serializable;
import java.util.ArrayList;

import snakegame.element.Part;

public class Snake implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public ArrayList<Part> body = new ArrayList<Part>();
	public int maxLength = 4;
	public Snake(){
		for(int i = 0;i < maxLength;i++) {
			body.add(new Part());
			body.get(i).x = maxLength - i -1;
			body.get(i).y = 0;
		}
	}
	public Snake(int x, int y){
		for(int i = 0;i < maxLength;i++) {
			body.add(new Part());
			body.get(i).x = maxLength - i - 1 + x;
			body.get(i).y = y;
		}
	}
	
	public Snake(int player) {
		int x = 0, y = 0;
		if(player == 0)	{
			x = 10;
			y = 10;
		}
		else if(player == 1) {
			x = 18;
			y = 10;
		}
		
		for(int i = 0; i < maxLength;i++) {
			body.add(new Part());
			body.get(i).x = maxLength - i - 1 + x;
			body.get(i).y = y;
		}
	}

	public void setDir(char dir){
		if(dir == 'R' && (!body.get(0).left)) {
			body.get(0).dx = 1;
			body.get(0).dy = 0;
			body.get(0).right = true;
			body.get(0).left = false;
			body.get(0).down = false;
			body.get(0).up = false;
		}
		else if(dir == 'L' && (!body.get(0).right)) {
			body.get(0).dx = -1;
			body.get(0).dy = 0;
			body.get(0).right = false;
			body.get(0).left = true;
			body.get(0).down = false;
			body.get(0).up = false;
		}
		else if(dir == 'D' && (!body.get(0).up)) {
			body.get(0).dx = 0;
			body.get(0).dy = 1;
			body.get(0).right = false;
			body.get(0).left = false;
			body.get(0).down = true;
			body.get(0).up = false;
		}
		else if(dir == 'U' && (!body.get(0).down)) {
			body.get(0).dx = 0;
			body.get(0).dy = -1;
			body.get(0).right = false;
			body.get(0).left = false;
			body.get(0).down = false;
			body.get(0).up = true;
		}
	}
}
