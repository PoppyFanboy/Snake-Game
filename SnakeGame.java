/**
  * Классическая консольная игра "Змейка"
  * Основной класс "SnakeGame"
  *
  * @version 0.1
  * @author PoppyFanboy
  */

public class SnakeGame {
	private static final int FIELD_SIZE = 20;
	
	private static SnakeBlock head;
	private static SnakeBlock tail;
	
	private static void initNewGame() {
		int x = FIELD_SIZE / 2;
		int y = FIELD_SIZE / 2;
		head = new SnakeBlock(FIELD_SIZE / 2, FIELD_SIZE / 2);
		
		SnakeBlock previous = head;
		for (int i = x + 1; i < x + FIELD_SIZE / 4; i++) {
			 previous = new SnakeBlock(previous, i, y);
		}
		tail = previous;
	}
	
	public static void main(String args[]) {
		
	}
	
}