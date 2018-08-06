package poppyfanboy.snakegame;

/**
  * Class "Snake"
  * The class is used to represent a whole
  * snake from a classic game
  *
  * The snake stores:
  * - references to its "head" and "tail"
  *   represented as instances of "SnakeBlock" class
  * - its current direction (as a enumeration)
  *
  * "Snake" class provides methods for:
  * - moving the snake in the direction stated in "dir" field
  * - checking whether it is possible for the snake to move
  *   (if the snake meets its own body)
  *
  * @version 0.1.1
  * @author PoppyFanboy
  */
  
enum Direction {
	NORTH, EAST, SOUTH, WEST
}
  
class Snake {
	Direction dir;
	
	SnakeBlock head;
	SnakeBlock tail;
	
	Snake(int initX, int initY, int initLength) {
		head = new SnakeBlock(initX, initY);
		
		SnakeBlock previous = head;
		for (int i = initX + 1; i <= initX + initLength; i++) {
			previous = new SnakeBlock(previous, i, initY);
		}
		tail = previous;
		
		dir = Direction.WEST;
	}
	
	// returns the number of gained points
	int move() {		
		SnakeBlock block = this.tail;
		do {
			block.setX(block.next.getX());
			block.setY(block.next.getY());
			block = block.next;
		} while (block.next != null);
		
		int newX = head.x;
		int newY = head.y;
		
		switch (dir) {
			case NORTH:	newY--;
						break;
					
			case EAST:	newX++;
						break;
					
			case SOUTH:	newY++;
						break;
					
			case WEST:	newX--;
						break;
		}
		head.setX(newX);
		head.setY(newY);
		
		return 0;
	}
	
	// returns true if the snake is able to move further
	boolean isSafe() {
		return true;
	}
}