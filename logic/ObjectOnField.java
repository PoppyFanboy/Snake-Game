package poppyfanboy.snakegame.logic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public interface ObjectOnField {
    boolean collision(IntVector point);
    void paint(GraphicsContext gc);
    void paint(GraphicsContext gc, Color color);
}
