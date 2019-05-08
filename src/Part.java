import java.io.Serializable;

public class Part implements Serializable{
	private static final long serialVersionUID = 1L;
	public int x = 0;
	public int y = 0;
	public int dx = 1;
	public int dy = 0;
	public boolean right = true;
	public boolean left = false;
	public boolean down = false;
	public boolean up = false;
}