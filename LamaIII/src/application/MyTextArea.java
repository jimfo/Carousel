package application;

import javafx.scene.control.TextArea;
import javafx.stage.Screen;

public class MyTextArea extends TextArea{

	// Parameters to define the size relative to screen size
	private final Double height = Screen.getPrimary().getVisualBounds().getHeight();
	private final Double width = Screen.getPrimary().getVisualBounds().getWidth();

	/**
	 * No-Arg constructor with styling
	 */
	public MyTextArea() {
		super();
		
		setPromptText("Comment");		
	    setPrefWidth(width * .3);
	    setPrefHeight(height * .09);
	    getStyleClass().add("MyTextArea");
	}
}
