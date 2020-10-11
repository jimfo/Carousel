package application;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;

public class MyLabel extends Label{

	// Parameters to define the size relative to screen size
	private final Double height = Screen.getPrimary().getVisualBounds().getHeight();
	private final Double width = Screen.getPrimary().getVisualBounds().getWidth();
	
	/**
	 * Constructor with styling
	 * @param str : String
	 */
	public MyLabel(String str) {
		super(str);
		
		setWrapText(true);
	    setPrefWidth(width * .3);
	    setPrefHeight(height * .1);
	    getStyleClass().add("MyLabel");
	    setTextAlignment(TextAlignment.LEFT);
	    setContentDisplay(ContentDisplay.TOP);
	}
	
}
