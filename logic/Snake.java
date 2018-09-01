package poppyfanboy.snakegame.logic;

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
 * @author PoppyFanboy
 */
  
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.input.KeyCode;
import poppyfanboy.snakegame.Main;

import java.util.ArrayList;

import static poppyfanboy.snakegame.logic.IntVector.vector;

class Snake implements ObjectOnField {
	private Direction dir;

	private GraphicsContext gc;
	private int blockSize = Main.GAME_WIDTH / Field.FIELD_WIDTH;

	ArrayList<Block> blocks = new ArrayList<>();

	// костыль: если значение == true, то нажатия любых клавиш не обрабатываются
	private boolean keyPressed = false;

	private double thickness = 0.7;
	
	Snake(GraphicsContext gc) {
		this.gc = gc;

		IntVector prev = vector(Field.FIELD_WIDTH / 2, Field.FIELD_HEIGHT / 2);
		int initLength = 5;

		for (int i = 1; i <= initLength; i++) {
			blocks.add(new Block(prev, blockSize, Color.BLACK, thickness));
			prev = prev.incX(1);
		}
		
		dir = Direction.LEFT;
	}

	int move(Field field) {
		keyPressed = false;
		int earnedPoints = 0;

		IntVector newCoordsMod = blocks.get(0).getCoords().add(dir.getOffset()).mod(Field.FIELD_WIDTH);
		IntVector newCoords = blocks.get(0).getCoords().add(dir.getOffset());

		boolean grown = false;
		Block oldTail = blocks.get(blocks.size() - 1);

		if (field.getFood().getCoords().equals(newCoordsMod)) {
			earnedPoints = 10;

			blocks.add(1, new Block(blocks.get(0).getCoords(), blockSize, Color.BLACK, thickness));
			grown = true;
		}

		for (int i = 0; i < (grown ? 2 : blocks.size()); i++) {
			IntVector prevCoords = blocks.get(i).getCoords();
			blocks.set(i, new Block(newCoordsMod, blockSize, Color.BLACK, thickness));
			newCoordsMod = prevCoords;
		}

		if (!grown) {
			oldTail.paint(gc, Color.WHITE, 1.0);
		} else {
			field.generateFood();
		}

		Block.fillLine(gc, newCoords, blocks.get(1).getCoords(), blockSize, thickness);

		return earnedPoints;
	}

	boolean isDead(Field field) {
		IntVector head = blocks.get(0).getCoords();
		for (int i = 1; i < blocks.size(); i++) {
			if (head.equals(blocks.get(i).getCoords())) {
				return true;
			}
		}

		return field.checkCollision(head, Snake.class);
	}

	// changes the direction of the snake
	void controlInp(KeyCode code) {
		if ((code == KeyCode.UP || code == KeyCode.DOWN || code == KeyCode.RIGHT ||
				code == KeyCode.LEFT) && !keyPressed) {
			Direction newDir = null;
			if (code == KeyCode.UP) {
				newDir = Direction.UP;
			} else if (code == KeyCode.RIGHT) {
				newDir = Direction.RIGHT;
			} else if (code == KeyCode.DOWN) {
				newDir = Direction.DOWN;
			} else if (code == KeyCode.LEFT) {
				newDir = Direction.LEFT;
			}

			if (dir.ableToChange(newDir)) {
				dir = newDir;
			}
			keyPressed = true;
		}
	}

	public boolean collision(IntVector point) {
		for (Block block : blocks) {
			if (block.getCoords().equals(point)) {
				return true;
			}
		}
		return false;
	}

	public void paint(GraphicsContext gc, Color color) {
		for (int i = 0; i < blocks.size(); i++) {
			if (i > 0) {
				Block.fillLine(gc, blocks.get(i - 1).getCoords(),
						blocks.get(i).getCoords(), blockSize, thickness);
			}
		}
	}

	public void paint(GraphicsContext gc) {
		this.paint(gc, Color.BLACK);
	}
}
