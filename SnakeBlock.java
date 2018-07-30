/**
  * Класс SnakeBlock
  * Часть змейки, хранит в себе ссылку на следующий блок змейки
  * и свои координаты на поле
  *
  * @version 0.1
  * @author PoppyFanboy
  */
  
class SnakeBlock {
	int x;
	int y;
	
	SnakeBlock next;
	
	SnakeBlock(SnakeBlock next, int x, int y) {
		this.next = next;
		this.x = x;
		this.y = y;
	}
	
	SnakeBlock(int x, int y) {
		this.x = x;
		this.y = y;
	}
}