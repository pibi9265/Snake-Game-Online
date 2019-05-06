import java.util.ArrayList;

public class Snake{
	public ArrayList<Part> body = new ArrayList<Part>();
	public int maxLength = 4;
	public int level = 1;
	public int moveCount = 0;
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
}