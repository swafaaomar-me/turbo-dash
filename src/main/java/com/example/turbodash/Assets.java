package com.example.turbodash;

import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Loads images and sounds bundled with the game and applies the shared window title and icon.
 */
public final class Assets {

    public static final String TITLE = "TURBO DASH";

    private Assets() {
    }

    /** Returns the URL of a file in src/main/resources, e.g. "porsche.png" or "audio/crash.mp3". */
    public static String url(String name) {
        URL resource = Assets.class.getResource("/" + name);
        if (resource == null) {
            throw new IllegalArgumentException("Missing game resource: " + name);
        }
        return resource.toExternalForm();
    }

    /**
     * Returns a URL that JavaFX's MediaPlayer can open. In the packaged app, resources live
     * inside the Java runtime image ("jrt:" URLs), which MediaPlayer can't read, so the
     * sound is copied to a temporary file first.
     */
    public static String mediaUrl(String name) {
        String url = url(name);
        if (url.startsWith("file:") || url.startsWith("jar:") || url.startsWith("http")) {
            return url;
        }
        try (InputStream in = Assets.class.getResourceAsStream("/" + name)) {
            String fileName = name.substring(name.lastIndexOf('/') + 1);
            Path copy = Files.createTempFile("turbodash-", "-" + fileName);
            copy.toFile().deleteOnExit();
            Files.copy(in, copy, StandardCopyOption.REPLACE_EXISTING);
            return copy.toUri().toString();
        } catch (IOException | NullPointerException e) {
            throw new IllegalStateException("Could not prepare sound " + name, e);
        }
    }

    public static Image image(String name) {
        return new Image(url(name));
    }

    /** Sets the window title and icon used on every screen. */
    public static void decorate(Stage stage) {
        stage.setTitle(TITLE);
        if (stage.getIcons().isEmpty()) {
            stage.getIcons().add(image("porsche.png"));
        }
    }
}
