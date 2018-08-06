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
  * @version 0.1.1
  * @author PoppyFanboy
  */
  
import javafx.scene.shape.*;
  
class SnakeBlock {
	SnakeBlock next = null;          // I know that it is better to declare class fields as "private"
	                                 // Nevertheless, in my opinion, there is no need in such sophistication
	int x = 0;                       // in so primitive class. Fields' bounds checks will be implemented in
	int y = 0;                       // other classes
	
	Rectangle rect;
	
	SnakeBlock(SnakeBlock next, int x, int y) {
		this(x, y);
		this.next = next;		
	}
	
	SnakeBlock(int x, int y) {
		this.x = x;
		this.y = y;
		this.rect = new Rectangle(10 * x, 10 * y, 10, 10);
	}
	
	void setX(int x) {
		this.x = x;
		this.rect.setX(10 * x);
	}
	
	void setY(int y) {
		this.y = y;
		this.rect.setY(10 * y);
	}
	
	int getX() {
		return this.x;
	}
	
	int getY() {
		return this.y;
	}
}