package poppyfanboy.snakegame.logic;

/**
 * Class "Snake"
 * The class is used to represent a whole
 * snake from a classic game
 *
 * @author PoppyFanboy
 */

import javafx.scene.canvas.*;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import poppyfanboy.snakegame.data.IntVector;

import java.util.ArrayList;

import static poppyfanboy.snakegame.data.IntVector.vector;

class Snake extends ObjectOnField {
	private Direction dir;

	private GraphicsContext gc;

	private ArrayList<SnakeBlock> blocks = new ArrayList<>();

	private double thickness = 0.6;
	private double gameWidth;
	
	Snake(GraphicsContext gc, double gameWidth) {
		this.gc = gc;
		this.gameWidth = gameWidth;
		this.blockSize = gameWidth / Field.FIELD_WIDTH;

		IntVector prev = vector(Field.FIELD_WIDTH / 2, Field.FIELD_HEIGHT / 2);
		int initLength = 5;

		for (int i = 1; i <= initLength; i++) {
			blocks.add(new SnakeBlock(prev, blockSize, SnakeGame.SNAKE_COLOR, thickness, Direction.LEFT));
			prev = prev.incX(1);
		}
		
		dir = Direction.LEFT;
	}

	int move(Field field) {
		int earnedPoints = 0;

		IntVector newCoordsMod = blocks.get(0).getCoords().add(dir.getOffset()).mod(Field.FIELD_WIDTH);
		Direction newDir = dir;

		boolean grown = false;

		if (field.getFood().getCoords().equals(newCoordsMod)) {
			earnedPoints = 1;

			blocks.add(1, new SnakeBlock(blocks.get(0).getCoords(), blockSize, SnakeGame.SNAKE_COLOR, thickness, dir));
			grown = true;
		}

		for (int i = 0; i < (grown ? 2 : blocks.size()); i++) {
			IntVector prevCoords = blocks.get(i).getCoords();
			Direction prevDir = blocks.get(i).getDir();
			blocks.set(i, new SnakeBlock(newCoordsMod, blockSize, SnakeGame.SNAKE_COLOR, thickness, newDir));
			newCoordsMod = prevCoords;
			newDir = prevDir;
		}

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
		if (code == KeyCode.UP || code == KeyCode.DOWN || code == KeyCode.RIGHT ||
				code == KeyCode.LEFT) {
			Direction newDir = null;
			if (code == KeyCode.UP) {
				newDir = Direction.UP;
			} else if (code == KeyCode.RIGHT) {
				newDir = Direction.RIGHT;
			} else if (code == KeyCode.DOWN) {
				newDir = Direction.DOWN;
			} else {
				newDir = Direction.LEFT;
			}

			if (dir.ableToChange(newDir)) {
				dir = newDir;
			}
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
		for (int i = blocks.size() - 2; i >= 0 ; i--) {
			SnakeBlock block = blocks.get(i);
			SnakeBlock.fillLine(gc, gameWidth, block.getCoords().add(block.getDir().getOffset().invert()), block.getCoords(),
					blockSize, thickness, SnakeGame.SNAKE_COLOR, 1.0, false);
		}
	}

	public void paint(GraphicsContext gc) {
		this.paint(gc, SnakeGame.SNAKE_COLOR);
	}


	public void movePaint(GraphicsContext gc, double proportion, boolean removeTail) {
		if (proportion >= 1.0) {
			proportion = 1.0;
		}

		if (removeTail) {
			SnakeBlock tail = blocks.get(blocks.size() - 1);
			SnakeBlock.fillLine(gc, gameWidth, tail.getCoords(), tail.getCoords().add(tail.getDir().getOffset().invert()),
					blockSize, thickness, Color.WHITE, proportion, true);
		}

		SnakeBlock head = blocks.get(0);
		SnakeBlock.fillLine(gc, gameWidth, head.getCoords().add(head.getDir().getOffset().invert()), head.getCoords(),
				blockSize, thickness, SnakeGame.SNAKE_COLOR, proportion, false);
	}

	void setGameWidth(double gameWidth, GraphicsContext gc) {
		this.gameWidth = gameWidth;
		this.blockSize = gameWidth / Field.FIELD_WIDTH;
		this.gc = gc;
	}

	@Override
	void setBlockSize(double blockSize) {
		this.blockSize = blockSize;
		for (Block block : blocks) {
			block.setBlockSize(blockSize);
		}
	}
}
