package application;

import javafx.scene.control.Button;
import javafx.stage.Screen;

public class ReviewBtn extends Button{

	// Parameters to define the size relative to screen size
	private final Double height = Screen.getPrimary().getVisualBounds().getHeight();
	private final Double width = Screen.getPrimary().getVisualBounds().getWidth();
	
	/**
	 * Constructor with styling
	 * @param str : String
	 */ 
	public ReviewBtn(String str) {
		super(str);
		
		setPrefWidth((width * .1)*.8);
		setPrefHeight((height * .1)*.46);
		getStyleClass().add("CarouselBtn");
	}
}
