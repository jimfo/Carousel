package application;

import java.util.ArrayList;
import javafx.scene.image.Image;

public class Step {
	
	private Image review;
	private String comments;
	private String stepDesc;
	private String expResult;
	private String screenshotReqd;
	private ArrayList<Image> images;
	private ArrayList<Image> reviews;
	
	public Step() {
		reviews = new ArrayList<>();		 
		images  = new ArrayList<>();
	}

	/**
	 * @return String
	 */
	public String getStepDesc() {
		return stepDesc;
	}
	
	/**
	 * @param stepDesc : String
	 */
	public void setStepDesc(String stepDesc) {
		this.stepDesc = stepDesc;
	}

	/**
	 * @return Image
	 */
	public Image getReview() {
		return review;
	}

	/**
	 * @param review : Image
	 */
	public void setReview(Image review) {
		this.review = review;
	}
	
	/**
	 * @return ArrayList<Image>
	 */
	public ArrayList<Image> getReviews() {
		return reviews;
	}

	/**
	 * @param reviews : ArrayList<Image>
	 */
	public void setReviews(ArrayList<Image> reviews) {
		this.reviews = reviews;
	}

	/**
	 * @return String
	 */
	public String getExpResult() {
		return expResult;
	}

	/**
	 * @param expResult : String
	 */
	public void setExpResult(String expResult) {
		this.expResult = expResult;
	}

	/**
	 * @return String
	 */
	public String getScreenshotReqd() {
		return screenshotReqd;
	}

	/**
	 * @param screenshotReqd : String
	 */
	public void setScreenshotReqd(String screenshotReqd) {
		this.screenshotReqd = screenshotReqd;
	}

	/**
	 * @return ArrayList<ImageView>
	 */
	public ArrayList<Image> getImages() {
		return images;
	}

	/**
	 * @param images : ArrayList<ImageView>
	 */
	public void setImages(ArrayList<Image> images) {
		this.images = images;
	}

	/**
	 * @return String
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments : String
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}		
}
