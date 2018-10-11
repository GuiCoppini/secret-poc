package client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;
import system.Connection;
import system.Message;

/**
 * Created by lbertoni on 10/9/17
 */
public class Interface extends Application implements Runnable {

    private static final int TILE_SIZE = 80;
    private static final int COLUMNS = 7;
    private static final int ROWS = 6;
    private static Connection connection;
    private boolean redMove = true;
    private static boolean waitingOponent = true;

    private static boolean watching = false;

    private static Pane discRoot = new Pane();

    private static Disc[][] grid = new Disc[COLUMNS][ROWS];

    public Interface() {
    }

    public Interface(boolean watching, Connection connection) {
        if (watching) this.setWaitingOponent(false);
        Client.setWatching(watching);
        this.watching = watching;
        this.connection = connection;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    private Parent createContent() {
        Pane root = new Pane();
        root.getChildren().add(discRoot);

        Shape gridShape = makeGrid();
        root.getChildren().add(gridShape);
        root.getChildren().addAll(makeColumns());

        return root;
    }

    private Shape makeGrid() {
        Shape shape = new Rectangle((COLUMNS + 1) * TILE_SIZE, (ROWS + 1) * TILE_SIZE);

        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                Circle circle = new Circle(TILE_SIZE / 2);
                circle.setCenterX(TILE_SIZE / 2);
                circle.setCenterY(TILE_SIZE / 2);
                circle.setTranslateX(x * (TILE_SIZE + 5) + TILE_SIZE / 4);
                circle.setTranslateY(y * (TILE_SIZE + 5) + TILE_SIZE / 4);

                shape = Shape.subtract(shape, circle);
            }
        }

        Light.Distant light = new Light.Distant();
        light.setAzimuth(45.0);
        light.setElevation(30.0);

        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(5.0);

        shape.setFill(Color.BLUE);
        shape.setEffect(lighting);

        return shape;
    }

    private List<Rectangle> makeColumns() {
        List<Rectangle> list = new ArrayList<>();

        for (int x = 0; x < COLUMNS; x++) {
            Rectangle rect = new Rectangle(TILE_SIZE, (ROWS + 1) * TILE_SIZE);
            rect.setTranslateX(x * (TILE_SIZE + 5) + TILE_SIZE / 4);
            rect.setFill(Color.TRANSPARENT);

            rect.setOnMouseEntered(e -> rect.setFill(Color.rgb(200, 200, 50, 0.3)));
            rect.setOnMouseExited(e -> rect.setFill(Color.TRANSPARENT));

            final int column = x;
            rect.setOnMouseClicked(e -> placeDisc(new Disc(Client.getId() == 1), column, true));
            list.add(rect);
        }

        return list;
    }

    private static Optional<Disc> getDisc(int column, int row) {
        if (column < 0 || column >= COLUMNS
                || row < 0 || row >= ROWS)
            return Optional.empty();

        return Optional.ofNullable(grid[column][row]);
    }

    public void setRedMove(boolean red) {
        this.redMove = red;
    }


    public static void placeDisc(Disc disc, int column, boolean colocouPeca) {
        if (!waitingOponent || !colocouPeca) {

            int row = ROWS - 1;
            do {
                if (!getDisc(column, row).isPresent())
                    break;

                row--;
            } while (row >= 0);

            if (row < 0)
                return;

            grid[column][row] = disc;
            discRoot.getChildren().add(disc);
            disc.setTranslateX(column * (TILE_SIZE + 5) + TILE_SIZE / 4);
            TranslateTransition animation = new TranslateTransition(Duration.seconds(0.5), disc);
            animation.setToY(row * (TILE_SIZE + 5) + TILE_SIZE / 4);
            animation.play();
            if (colocouPeca) {
                connection.sendMessage(new Message("add", column));
                waitingOponent = true;
            }
        }
    }

    @Override
    public void run() {
        launch();
    }

    public static class Disc extends Circle {

        private final boolean red;

        public Disc(boolean red) {
            super(TILE_SIZE / 2, red ? Color.RED : Color.YELLOW);
            this.red = red;

            setCenterX(TILE_SIZE / 2);
            setCenterY(TILE_SIZE / 2);
        }
    }

    public void setWaitingOponent(boolean waitingOponent) {
        this.waitingOponent = waitingOponent;
    }
}

