package main_classes;

import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

class Transition {
    private static Rectangle transitionScreen;

    static void screenChange(Pane pane1, Pane pane2, Group group, int width, int height, int color) {
        int animationLength = 1000;

        transitionScreen = new Rectangle(0, 0, width, height);
        transitionScreen.toFront();
        switch (color) {
            case 0:
                transitionScreen.setFill(Color.BLACK);
                break;
            case 1:
                transitionScreen.setFill(Color.WHITE);
                break;
        }
        group.getChildren().add(transitionScreen);
        FadeTransition ft = new FadeTransition((Duration.millis(animationLength)), transitionScreen);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setCycleCount(2);
        ft.setAutoReverse(true);
        ft.play();
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(animationLength * 2);
                } catch (InterruptedException ignored) {
                }
                return null;
            }
        };
        Task<Void> changeScene = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(animationLength);
                } catch (InterruptedException ignored) {
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(event2 -> group.getChildren().remove(transitionScreen));
        changeScene.setOnSucceeded((event1 -> swapScreens(pane1, pane2, group)));
        new Thread(sleeper).start();
        new Thread(changeScene).start();
    }

    private static void swapScreens(Pane pane1, Pane pane2, Group group) {
        group.getChildren().remove(pane1);
        group.getChildren().add(pane2);
       // System.out.println("Swapped!");
        pane2.toBack();
    }

}