package com.example.turbodash;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Runs a race: moves obstacles down the three lanes, handles steering, detects crashes,
 * counts the score and makes the game harder over time.
 */
public class GameController {

    private static final int[] LANES = {90, 210, 330};
    private static final int MAX_OBSTACLES = 4;
    private static final int OBSTACLE_GAP = 300;
    private static final int STARTING_SPEED = 4;
    private static final long ONE_SECOND = 1_000_000_000L;
    private static final long SPEED_UP_INTERVAL = 10 * ONE_SECOND;
    private static final int POINTS_PER_THEME = 10;

    @FXML
    private Pane gamePane;

    private final Random rand = new Random();
    private final List<ImageView> obstacles = new ArrayList<>();

    private Stage stage;
    private ImageView playerCarView;
    private ImageView background;
    private ImageView track;
    private Label scoreLabel;
    private Label gameOverLabel;

    private int speed = STARTING_SPEED;
    private int score = 0;
    private boolean gameOver = false;
    private long lastScoreUpdateTime = 0;
    private long lastSpeedUpTime = 0;

    private AnimationTimer gameLoop;
    private MediaPlayer backgroundMusicPlayer;
    private MediaPlayer collisionSoundPlayer;

    private final Image[] backgrounds = {
            Assets.image("background1.jpeg"),
            Assets.image("background2.jpeg"),
            Assets.image("background3.jpeg"),
            Assets.image("background4.jpeg")
    };
    private final Image[] tracks = {
            Assets.image("track1.png"),
            Assets.image("track2.png"),
            Assets.image("track3.png"),
            Assets.image("track4.png")
    };
    private final Image[] obstacleImages = {
            Assets.image("obstacle1.png"),
            Assets.image("obstacle2.png"),
            Assets.image("obstacle3.png"),
            Assets.image("obstacle4.png")
    };
    private final Image explosionImage = Assets.image("explosion.png");

    public void startGame(Stage stage, SelectVehicle.Vehicle vehicle) {
        this.stage = stage;

        backgroundMusicPlayer = createPlayer("audio/background-music.mp3");
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            backgroundMusicPlayer.play();
        }
        collisionSoundPlayer = createPlayer("audio/crash.mp3");

        background = new ImageView(backgrounds[0]);
        background.setFitWidth(GameScreen.WIDTH);
        background.setFitHeight(GameScreen.HEIGHT);

        track = new ImageView(tracks[0]);
        track.setX(100);
        track.setFitWidth(300);
        track.setFitHeight(GameScreen.HEIGHT);

        playerCarView = new ImageView(Assets.image(vehicle.imageFile()));
        playerCarView.setFitWidth(80);
        playerCarView.setFitHeight(70);
        playerCarView.setX(LANES[0]);
        playerCarView.setY(GameScreen.HEIGHT - 120);

        scoreLabel = new Label("SCORE: 0");
        scoreLabel.setLayoutX(360);
        scoreLabel.setLayoutY(22);
        scoreLabel.setMinWidth(130);
        scoreLabel.setPadding(new Insets(2, 8, 2, 8));
        scoreLabel.setStyle("-fx-background-color: green;");
        scoreLabel.setFont(Font.font(Ui.FONT, 18));
        scoreLabel.setTextFill(Color.WHITE);

        gameOverLabel = new Label();
        gameOverLabel.setLayoutX(110);
        gameOverLabel.setLayoutY(280);
        gameOverLabel.setStyle("-fx-background-color: #00AAEE;");
        gameOverLabel.setFont(Font.font(Ui.FONT, 30));
        gameOverLabel.setTextFill(Color.WHITE);
        gameOverLabel.setPadding(new Insets(10));
        gameOverLabel.setVisible(false);

        gamePane.getChildren().addAll(background, track, playerCarView, scoreLabel, gameOverLabel);

        gamePane.setOnKeyPressed(this::handleKeyPressed);
        gamePane.setFocusTraversable(true);

        for (int i = 0; i < MAX_OBSTACLES; i++) {
            addObstacle();
        }
        scoreLabel.toFront();

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateGame(now);
            }
        };
        gameLoop.start();
    }

    private MediaPlayer createPlayer(String file) {
        try {
            return new MediaPlayer(new Media(Assets.mediaUrl(file)));
        } catch (RuntimeException e) {
            // The game still works without sound (e.g. missing codecs on some Linux setups)
            System.err.println("Sound unavailable (" + file + "): " + e.getMessage());
            return null;
        }
    }

    /** Places a new obstacle in a random lane, above the screen and behind the previous one. */
    private void addObstacle() {
        int x = LANES[rand.nextInt(LANES.length)];
        double y = obstacles.isEmpty()
                ? -100
                : Math.min(-100, obstacles.get(obstacles.size() - 1).getY() - OBSTACLE_GAP);

        ImageView obstacle = new ImageView(obstacleImages[rand.nextInt(obstacleImages.length)]);
        obstacle.setFitWidth(playerCarView.getFitWidth());
        obstacle.setFitHeight(playerCarView.getFitHeight());
        obstacle.setX(x);
        obstacle.setY(y);

        obstacles.add(obstacle);
        gamePane.getChildren().add(obstacle);
    }

    private void updateGame(long now) {
        if (gameOver) {
            return;
        }
        if (lastScoreUpdateTime == 0) {
            lastScoreUpdateTime = now;
            lastSpeedUpTime = now;
        }

        for (ImageView obstacle : obstacles) {
            obstacle.setY(obstacle.getY() + speed);
        }

        for (ImageView obstacle : obstacles) {
            if (obstacle.getBoundsInParent().intersects(playerCarView.getBoundsInParent())) {
                crash(obstacle);
                return;
            }
        }

        // Remove obstacles that have left the screen (from the list and from the scene)
        obstacles.removeIf(obstacle -> {
            boolean gone = obstacle.getY() > GameScreen.HEIGHT;
            if (gone) {
                gamePane.getChildren().remove(obstacle);
            }
            return gone;
        });
        while (obstacles.size() < MAX_OBSTACLES) {
            addObstacle();
        }
        scoreLabel.toFront();

        // One point per second survived
        if (now - lastScoreUpdateTime >= ONE_SECOND) {
            score++;
            scoreLabel.setText("SCORE: " + score);
            lastScoreUpdateTime = now;
            if (score % POINTS_PER_THEME == 0) {
                changeBackgroundTrack(score / POINTS_PER_THEME);
            }
        }

        // Obstacles get faster every 10 seconds
        if (now - lastSpeedUpTime >= SPEED_UP_INTERVAL) {
            speed++;
            lastSpeedUpTime = now;
        }
    }

    private void crash(ImageView obstacle) {
        gameOver = true;
        gameLoop.stop();
        showExplosion(obstacle.getX(), obstacle.getY());

        boolean newHighScore = new ScoreManager().submitScore(Session.currentUsername(), score);
        gameOverLabel.setText("GAME OVER\nSCORE: " + score + (newHighScore && score > 0 ? "\nNEW HIGH SCORE!" : ""));
        gameOverLabel.setVisible(true);
        gameOverLabel.toFront();

        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
        }
        if (collisionSoundPlayer != null) {
            collisionSoundPlayer.play();
        }

        // Back to the main menu after 4 seconds
        PauseTransition delay = new PauseTransition(Duration.seconds(4));
        delay.setOnFinished(e -> leaveGame());
        delay.play();
    }

    /** Stops the race and its sounds, then returns to the main menu. Safe to call more than once. */
    public void leaveGame() {
        if (stage == null) {
            return;
        }
        gameOver = true;
        if (gameLoop != null) {
            gameLoop.stop();
        }
        disposePlayer(backgroundMusicPlayer);
        disposePlayer(collisionSoundPlayer);
        Stage current = stage;
        stage = null;
        current.setOnCloseRequest(null);
        MainMenu.show(current);
    }

    private static void disposePlayer(MediaPlayer player) {
        if (player != null) {
            player.stop();
            player.dispose();
        }
    }

    private void showExplosion(double x, double y) {
        ImageView explosion = new ImageView(explosionImage);
        explosion.setFitWidth(100);
        explosion.setFitHeight(100);
        explosion.setX(x - 10);
        explosion.setY(y);
        gamePane.getChildren().add(explosion);
    }

    /** Switches to the next background and track theme every 10 points. */
    private void changeBackgroundTrack(int index) {
        background.setImage(backgrounds[index % backgrounds.length]);
        track.setImage(tracks[index % tracks.length]);
    }

    private void handleKeyPressed(KeyEvent event) {
        if (gameOver) {
            return;
        }
        switch (event.getCode()) {
            case LEFT, A -> moveToLane(currentLane() - 1);
            case RIGHT, D -> moveToLane(currentLane() + 1);
            default -> { }
        }
    }

    private void moveToLane(int lane) {
        if (lane >= 0 && lane < LANES.length) {
            playerCarView.setX(LANES[lane]);
        }
    }

    private int currentLane() {
        for (int i = 0; i < LANES.length; i++) {
            if (playerCarView.getX() == LANES[i]) {
                return i;
            }
        }
        return 0;
    }
}
