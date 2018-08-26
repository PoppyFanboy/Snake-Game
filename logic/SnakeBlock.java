package poppyfanboy.snakegame.logic;

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
  
class SnakeBlock extends Block{
	SnakeBlock next = null;
	
	SnakeBlock(SnakeBlock next, IntVector coords) {
		super(coords);
		this.next = next;		
	}

	SnakeBlock(IntVector coords) {
		super(coords);
	}
}