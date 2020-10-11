package application;
	
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;


public class Main extends Application {
	
    private Stage cStage;
    private FileUtil fu = new FileUtil();
    private Carousel car = new Carousel();
	
	/**
	 * Builds the upload screen
	 */
    @Override	
	public void start(Stage stage) {
		try {
			
			// Size and position
			stage.setHeight(300); 
			stage.setWidth(450);
			MyVBox container  = new MyVBox();
			container.setAlignment(Pos.CENTER);
			
			// Uploads excel script
			UploadBtn scriptBtn = new UploadBtn("Script");
			scriptBtn.setOnAction( e -> {
				fu.uploadFile(stage);
				scriptBtn.setDisable(true);
			});
			
			// Uploads test evidence
			UploadBtn imageBtn  = new UploadBtn("Images");
			imageBtn.setOnAction( e ->{						
				car.getImages(stage);
				imageBtn.setDisable(true);
			});
			
			// Uploads state
			// TODO need to establish work flow for this functionality
			// test evidence and state are specific to the credential
			// 1. Save state in credential folder
			// 2. Look for state file when uploading test evidence
			// 3. If state file exists, set flag for new workflow
			UploadBtn stateBtn  = new UploadBtn("State");
			
			// Closes the upload window and builds the carousel
			UploadBtn closeBtn  = new UploadBtn("Close");
			closeBtn.setOnAction( e ->{	
				stage.close();
				cStage = car.buildCarousel();
				cStage.show();
				});

			container.getChildren().addAll(scriptBtn, imageBtn, stateBtn, closeBtn);
		    Scene scene = new Scene(container, 300, 450);
		  
		    stage.setScene(scene);
		    stage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
