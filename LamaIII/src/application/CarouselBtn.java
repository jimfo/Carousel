package application;

import javafx.scene.control.Button;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;

public class CarouselBtn extends Button{
	
	private final Double height = Screen.getPrimary().getVisualBounds().getHeight();
	private final Double width = Screen.getPrimary().getVisualBounds().getWidth();
	
	public CarouselBtn(String str) {
		super(str);
		
		setFocused(false);
		setPrefWidth(width * .09);
		setPrefHeight(height * .1);
		getStyleClass().add("CarouselBtn");
		textAlignmentProperty().set(TextAlignment.CENTER);
	}
}
