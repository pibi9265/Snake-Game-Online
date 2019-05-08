import java.io.Serializable;

public class Apple implements Serializable{
	private static final long serialVersionUID = 1L;
	public int x = 0;
	public int y = 0;
	public Apple() {}
	public Apple(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Apple clone() throws CloneNotSupportedException
	{
		Apple myApple = (Apple)super.clone();
		return myApple;
	}
}
