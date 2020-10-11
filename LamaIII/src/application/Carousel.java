package application;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Carousel {
	
	private int curr = 0;
	private int stepIdx = 0;
	
	private FileUtil fu;
	private ImageView iv;
	private MyLabel stepDesc;
	private CheckMark checked;
	private MyTextArea comment;
	private List<File> imgFiles;
	private ArrayList<Step> steps;
	
	private Image nssr  = new Image("/nssr.jpg");
	private Image red   = new Image("/Red_Check.png");
	private Image green = new Image("/Green_Check.png");
		
	private final String IDLE_BUTTON_STYLE = "-fx-background-color: transparent;";
	private final String HOVER_REVIEW_STYLE = "-fx-border-color: #00FF00; -fx-text-fill: #00FF00; -fx-background-color: #022F06";
	private final String HOVER_MARKED_STYLE = "-fx-border-color: #ff4d4d; -fx-text-fill: #ff4d4d; -fx-background-color: #3f0000";
	
    /**
     * Build the Image Carousel
     * @return Stage
     */
	public Stage buildCarousel() {
		// TODO need to handle closing the window by x
	 	Stage stage = new Stage();
	 	Image i = null;

		try {		

			Rectangle2D sb = Screen.getPrimary().getVisualBounds();

		    checked = new CheckMark();		  	    	    

		    i = steps.get(0).getImages().get(0);	
		    
		    iv = new ImageView(i);
		    
		    Pane pane = new Pane(); 
		    pane.getChildren().addAll(iv, checked);		    
		   
		    CarouselBtn nextImg  = new CarouselBtn("NEXT\n>>");
		    nextImg.setOnMouseEntered(e -> nextImg.setStyle(HOVER_REVIEW_STYLE));
		    nextImg.setOnMouseExited(e -> nextImg.setStyle(IDLE_BUTTON_STYLE));
		    nextImg.setOnAction( e -> nextImage());		    
		    
		    CarouselBtn prevBtn  = new CarouselBtn("PREV\n<<");
		    prevBtn.setOnAction( e-> prevImage());		    
		    
		    MyVBox vb1 = new MyVBox();
		    		    
		    ReviewBtn review = new ReviewBtn("PASS"); 
		    review.setOnMouseEntered(e -> review.setStyle(HOVER_REVIEW_STYLE));
		    review.setOnMouseExited(e -> review.setStyle(IDLE_BUTTON_STYLE));
		    review.setOnAction( e -> reviewCheck());

		    ReviewBtn marked = new ReviewBtn("FAIL"); 
		    marked.setOnMouseEntered(e -> marked.setStyle(HOVER_MARKED_STYLE));
		    marked.setOnMouseExited(e -> marked.setStyle(IDLE_BUTTON_STYLE));
		    marked.setOnAction( e -> markedCheck());

		    vb1.getChildren().addAll(review, marked);
		    
		    MyVBox vb2 = new MyVBox();
		    
		    ReviewBtn closeBtn = new ReviewBtn("Close"); 
		    ReviewBtn saveBtn = new ReviewBtn("Save"); 

		    vb2.getChildren().addAll(closeBtn, saveBtn);

		    stepDesc = new MyLabel(steps.get(stepIdx).getStepDesc());
		    
		    comment = new MyTextArea();	
		    
			MyHBox topBox = new MyHBox();
			MyHBox botBox = new MyHBox();
			MyVBox allBox = new MyVBox();
			
			botBox.getChildren().addAll(prevBtn, stepDesc, nextImg, vb1,  comment, vb2 );
			topBox.getChildren().add(pane);
			allBox.getChildren().addAll(topBox, botBox);			
		   
			Scene scene = new Scene(allBox, sb.getWidth()*.8 , sb.getHeight()*.91); //
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			iv.setPreserveRatio(true); 
		    iv.fitWidthProperty().bind(scene.widthProperty());
		    iv.fitHeightProperty().bind(scene.heightProperty());
		    	    
		    stage.setOnCloseRequest(e->closeProgram());
		    stage.setScene(scene);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return stage;
	}
	
	/**
	 * Sets the pass imageview
	 */
	private void reviewCheck() {

		// stepIdx and curr are maintained in next and prev
		// Set the review for this image green
		steps.get(stepIdx).getReviews().set(curr, green);
		checked.setImage(steps.get(stepIdx).getReviews().get(curr));
	}
	
	/**
	 * Sets the fail imageview
	 */
	private void markedCheck() {

		// stepIdx and curr are maintained in next and prev
		// Set the review for this image red
		steps.get(stepIdx).getReviews().set(curr, red);
		checked.setImage(steps.get(stepIdx).getReviews().get(curr));
	}
	
	/**
	 * This is a list of methods to get images from hd
	 * @param s : Stage
	 */
	public void getImages(Stage s) {
		
		fu = new FileUtil();
		imgFiles = fu.uploadDirectory(s);
		steps = fu.getSteps();
		noScreenshotReqd();
		testFile();
	}
	
	/**
	 * If no screenshot is required use image from res folder as place holder
	 */
	private void noScreenshotReqd() {
		
		
		for(Step s : steps) {
			if(s.getScreenshotReqd().equals("N")) {
	        	s.getImages().add(nssr);
	        	s.getReviews().add(null);  
			}
		}
	}
	
	/**
	 * Loops through all images and places them in the appropriate step
	 */
	private void testFile() {
		
		for(int i = 0; i < imgFiles.size(); i++) {
			
			Image image = null;
			
			// Extract image name from file path
			String fStr = imgFiles.get(i).toString();
			String str = fStr.substring(fStr.lastIndexOf("\\")+1, fStr.length());
			
			// Extract image number from image name
	        Pattern p = Pattern.compile("\\d+");
	        Matcher m = p.matcher(str);
	        int stepIndex = 0;
	        
	        // Extracted number is the step index - 1
	        while(m.find()) {
	        	stepIndex = Integer.valueOf(m.group(0));
	        }
	        
	        try {
				String localUrl = imgFiles.get(i).toURI().toURL().toString();
				image = new Image(localUrl); 
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

	        // Add image to image array and add null image to pass/fail image array
	        // Each expected result image has a pass/fail image associated with it
	        steps.get(stepIndex-1).getImages().add(image);
	        steps.get(stepIndex-1).getReviews().add(null);  
		}
	}
	
	/**
	 * Maintain imgIdx
	 * Maintain stepDesc
	 */
	private void nextImage() {

		// Clicking next on an unreviewed image counts as passing
		if(checked.getImage() == null) {
			steps.get(stepIdx).getReviews().set(curr, green);	
		}
		
		// Three cases to handle. 1. curr can be incremented
		// 2. stepIdx cannot be incremented (flip to beginning of step array)
		// 3. curr cannot be incremented (flip to next step)
		if(curr < steps.get(stepIdx).getImages().size()-1) {
			++curr;
		}
		else if(stepIdx+1 > steps.size()-1) {
			stepIdx = 0;
			curr = 0;
		}
		else if(curr+1 > steps.get(stepIdx).getImages().size()-1) {
			++stepIdx;
			curr = 0;
		}
		
		// Verify review not null before displaying
		if(steps.get(stepIdx).getReviews().get(curr) != null) {
			checked.setImage(steps.get(stepIdx).getReviews().get(curr));
		}

		// Update fields based on index maintenance
		stepDesc.setText(steps.get(stepIdx).getStepDesc());
		iv.setImage(steps.get(stepIdx).getImages().get(curr));
		checked.setImage(steps.get(stepIdx).getReviews().get(curr));
	}
	
	private void prevImage() {
		
		// Three cases to handle. 1. curr can be decremented
		// 2. stepIdx cannot be decremented (flip to end of step array)
		// 3. curr cannot be decremented (flip to prev step)
		if(curr > 0) {
			--curr;
		}
		else if(stepIdx-1 < 0) {
			stepIdx = steps.size()-1;
			curr = steps.get(stepIdx).getImages().size() - 1;
		}
		else if (curr-1 < 0) {
			--stepIdx;
			curr = steps.get(stepIdx).getImages().size() - 1;
		}
		
		// Update fields based on index maintenance
		checked.setImage(steps.get(stepIdx).getReviews().get(curr));
		stepDesc.setText(steps.get(stepIdx).getStepDesc());
		iv.setImage(steps.get(stepIdx).getImages().get(curr));
	}
	
	private void closeProgram() {
		// TODO write to file
	//	fu.writeToFile(myImages);
	}
}
