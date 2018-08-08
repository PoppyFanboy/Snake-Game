package poppyfanboy.snakegame;

/**
 * Class "SnakeBlock"
 * The class is used to represent a single segment of a snake
 * from a classic game
 *
 * Every instance of the class stores:
 *  - its coordinates as a pair of integers x and y
 *  - a reference to the next segment of a snake counting from tail to head
 *
 * @author PoppyFanboy
 */
  
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
  
class SnakeBlock {
	SnakeBlock next = null;
  
	private int x = 0;
	private int y = 0;
	
	SnakeBlock(SnakeBlock next, int x, int y) {
		this(x, y);
		this.next = next;		
	}
	
	SnakeBlock(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	void paint(GraphicsContext gc, int blockSize, Color color) {
		gc.setFill(color);
		gc.fillRect(x * blockSize, y * blockSize, blockSize, blockSize);
	}
	
	int getX() {
		return x;
	}
	
	int getY() {
		return y;
	}
}
