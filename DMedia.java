//package com.sthembiso;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.stage.FileChooser;
import javafx.scene.media.Media;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.InvalidationListener;
import javafx.scene.control.Slider;
import javafx.util.Duration;
import javafx.event.EventHandler;
//import javafx.event.MouseEvent;

import java.io.File;
import java.util.ResourceBundle;

public class DMedia extends Application
{
	private MediaView mediaView = new MediaView();;
	private MediaPlayer mediaPlayer;
	private String filePath;
	private Slider slider = new Slider();
	private Slider seekSlider = new Slider();

	@Override
	public void start(Stage primaryStage)
	{
		StackPane screen = new StackPane();
		screen.getChildren().add(mediaView);
//		screen.getChildren().add(seekSlider);
		screen.setPrefHeight(450);
//		seekSlider.setAlignment(Pos.BOTTOM_CENTER);

		HBox hBox = new HBox();
		hBox.setPrefHeight(30);
		hBox.setAlignment(Pos.CENTER);
		hBox.setSpacing(3);

		VBox vBox = new VBox();
		vBox.setPrefHeight(40);
		vBox.getChildren().addAll(seekSlider, hBox);
		Button openFile = new Button("openFile");
		openFile.setOnAction(e -> {
			FileChooser fileChooser	= new FileChooser();
			FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select a File(*.mp4)", "*.mp4");
			fileChooser.getExtensionFilters().add(filter);
			File file = fileChooser.showOpenDialog(null);
			filePath = file.toURI().toString();

			if (filePath != null)
			{
				Media media = new Media(filePath);
				mediaPlayer = new MediaPlayer(media);
				mediaView.setMediaPlayer(mediaPlayer);
				DoubleProperty width = mediaView.fitWidthProperty();
				DoubleProperty height = mediaView.fitHeightProperty();

				width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
				height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));

				slider.setValue(mediaPlayer.getVolume() * 100);
				slider.valueProperty().addListener(o -> mediaPlayer.setVolume(slider.getValue() / 100));

				mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> seekSlider.setValue(newValue.toSeconds()));
				seekSlider.setOnMouseClicked(event -> mediaPlayer.seek(Duration.seconds(seekSlider.getValue())));


				mediaPlayer.play();
			}
		});

		Button play = new Button("Play");
		play.setOnAction(e -> 
		{
			mediaPlayer.play();
			mediaPlayer.setRate(1);
		});

		Button pause = new Button("Pause");
		pause.setOnAction(e -> mediaPlayer.pause());

		Button stop = new Button("stop");
		stop.setOnAction(e -> mediaPlayer.stop()); 

		Button slower = new Button("<<<");
		slower.setOnAction(e -> mediaPlayer.setRate(.5));

		Button slow = new Button("<<");
		slow.setOnAction(e -> mediaPlayer.setRate(.75));

		Button fast = new Button(">>");
		fast.setOnAction(e -> mediaPlayer.setRate(1.5));

		Button faster = new Button(">>>");
		faster.setOnAction(e -> mediaPlayer.setRate(2));

		Button exit = new Button("Exit");
		exit.setOnAction(e -> System.exit(0));

		Slider volume = new Slider();
		volume.setPadding(new Insets(0,0,0,20));
		volume.setPrefWidth(220);
		hBox.getChildren().addAll(openFile, play, pause, stop, slower, slow, fast, faster, exit, volume);

		BorderPane root = new BorderPane();
		root.setOpacity(50);
		root.setCenter(screen);
		root.setBottom(vBox);

		Scene scene = new Scene(root, 800, 500);
		scene.setOnMouseClicked(eh -> 
				{
					if (eh.getClickCount() == 2)
						primaryStage.setFullScreen(true);
				});
		scene.getStylesheets().add("Styles.css");
		primaryStage.setScene(scene);
		primaryStage.setTitle("DezignPhi");
		primaryStage.show();
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
