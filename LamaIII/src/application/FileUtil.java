package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FileUtil {

	private List<File> files;
	private ArrayList<String> expectedResult;	
	private ArrayList<String> stepDescription;
	private ArrayList<ImageView> images = new ArrayList<>();
	private static ArrayList<Step> steps = new ArrayList<>();

	/**
	 * No arg constructor
	 */
	public FileUtil(){
	}
	
	/**
	 * @return ArrayList<ImageView>
	 */
	public ArrayList<ImageView> getImages() {
		return images;
	}
	
	/**
	 * @param images : ArrayList<ImageView>
	 */
	public void setImages(ArrayList<ImageView> images) {
		this.images = images;
	}
	
	/**
	 * @return ArrayList<Step>
	 */
	public ArrayList<Step> getSteps() {
		return steps;
	}
	
	/**
	 * @param steps
	 */
	public void setSteps(ArrayList<Step> steps) {
		FileUtil.steps = steps;
	}

	/**
	 * Getter for ArrayList<String> desc
	 * @return ArrayList<String> : desc
	 */
	public ArrayList<String> getDesc() {
		return stepDescription;
	}

	/**
	 * Setter for ArrayList<String> desc
	 * @param desc : ArrayList<String>
	 */
	public void setDesc(ArrayList<String> desc) {
		this.stepDescription = desc;
	}
	
	/**
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getExpectedResult() {
		return expectedResult;
	}

	/**
	 * @param expectedResult : ArrayList<String>
	 */
	public void setExpectedResult(ArrayList<String> expectedResult) {
		this.expectedResult = expectedResult;
	}

	/**
	 * Boiler plate file uploader
	 * @param stage : Stage
	 */
	public void uploadFile(Stage stage){
		// TODO add a filter
		FileChooser fileChooser = new FileChooser();
		File selectedFile = fileChooser.showOpenDialog(stage);    	

		if (selectedFile != null) {
			validateFileType(selectedFile);
		}
	}
	
	/**
	 * Boiler plate directory uploader
	 * @param stage : Stage
	 */
	public List<File> uploadDirectory(Stage stage) {
		// TODO Validate files are png/jpg
		DirectoryChooser dc = new DirectoryChooser();
		File sd = dc.showDialog(stage);
		files = Arrays.asList(sd.listFiles());
		return files;		
	}

	/**
	 * Validate file type
	 * @param f : File
	 */
	private void validateFileType(File f){

		// Need to validate user has uploaded an excel file type
		// if not redirect back to upload screen
		String fileName = f.toString();

		if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
			if(fileName.substring(fileName.lastIndexOf(".") + 1).equals("xls")){
				getXLSFile(f);
			}
			else if(fileName.substring(fileName.lastIndexOf(".") + 1).equals("xlsx")){
				getXLSXFile(f);
			}
			else{
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Incorrect File Type");
				alert.setContentText("Press OK to display File Uploader");
				alert.showAndWait().ifPresent(rs -> {
					if (rs == ButtonType.OK) {
						uploadFile(new Stage());
					}
				});
			}
		}
	}

	/**
	 * Takes the excel sheet scans the top row for column name
	 * @param sheet
	 * @param colName
	 * @return int : the column number
	 */
	private int getColumnNumber(XSSFSheet sheet, String colName) {

		Row row = sheet.getRow(0);

		for(int columnNumber = 0; columnNumber < row.getLastCellNum(); columnNumber++) {
			XSSFCell cell = (XSSFCell) row.getCell(columnNumber);
			if(cell.getStringCellValue().equals(colName)) {
				return columnNumber;
			}
		}
		return 0;
	}
	
	/**
	 * Takes the excel sheet scans the top row for column name
	 * @param sheet
	 * @param colName
	 * @return int : the column number
	 */
	private int getColumnNumber(HSSFSheet sheet, String colName) {

		Row row = sheet.getRow(0);

		for(int columnNumber = 0; columnNumber < row.getLastCellNum(); columnNumber++) {
			HSSFCell cell = (HSSFCell) row.getCell(columnNumber);
			if(cell.getStringCellValue().equals(colName)) {
				return columnNumber;
			}
		}
		return 0;
	}

	/**
	 * Extract data from Excel spreadsheet using Apache POI
	 * @param f : File
	 */
	public void getXLSXFile(File f){

		FileInputStream fIP = null;
		XSSFWorkbook workbook = null;

		try {
			fIP = new FileInputStream(f);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			workbook = new XSSFWorkbook(fIP);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		assert workbook != null;
		XSSFSheet xlsxWorkbook = workbook.getSheetAt(0);

		// Get the column numbers
		int descCol   = getColumnNumber(xlsxWorkbook, "Step Description");
		int expCol    = getColumnNumber(xlsxWorkbook, "Expected Result");
		int commCol   = getColumnNumber(xlsxWorkbook, "Comments");
		int ssreqdCol = getColumnNumber(xlsxWorkbook, "Screenshot Required");
		
		// Extract data by row and column
		for (Row row : xlsxWorkbook) {

			if(row.getRowNum() == 0){
				continue;
			}

			Step step = new Step();
			
			Cell cellDesc = row.getCell(descCol);
			step.setStepDesc(cellDesc.getStringCellValue());

			Cell cellExp = row.getCell(expCol);
			step.setExpResult(cellExp.getStringCellValue());
			 
			Cell cellComm = row.getCell(commCol);
			if(cellComm == null)
				step.setComments("");
			else
				step.setComments(cellComm.getStringCellValue());
			
			Cell cellSsReqd = row.getCell(ssreqdCol);
			step.setScreenshotReqd(cellSsReqd.getStringCellValue());

			steps.add(step);
		}

		try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Extract data from Excel spreadsheet using Apache POI
	 * @param f : File
	 */
	public void getXLSFile(File f){

		FileInputStream fIP = null;
		HSSFWorkbook workbook = null;

		try {
			fIP = new FileInputStream(f);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			workbook = new HSSFWorkbook(fIP);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		assert workbook != null;
		HSSFSheet xlsWorkbook = workbook.getSheetAt(0);

		// Get the column numbers
		int descCol   = getColumnNumber(xlsWorkbook, "Step Description");
		int expCol    = getColumnNumber(xlsWorkbook, "Expected Result");
		int commCol   = getColumnNumber(xlsWorkbook, "Comments");
		int ssreqdCol = getColumnNumber(xlsWorkbook, "Screenshot Required");
		
		// Extract data by row and column
		for (Row row : xlsWorkbook) {
			
			if(row.getRowNum() == 0){
				continue;
			}
			
			Step step = new Step();
			
			Cell cellDesc = row.getCell(descCol);
			step.setStepDesc(cellDesc.getStringCellValue());

			Cell cellExp = row.getCell(expCol);
			step.setExpResult(cellExp.getStringCellValue());
			
			Cell cellComm = row.getCell(commCol);
			if(cellComm == null)
				step.setComments("");
			else
				step.setComments(cellComm.getStringCellValue());
			
			Cell cellSsReqd = row.getCell(ssreqdCol);
			step.setScreenshotReqd(cellSsReqd.getStringCellValue());

			steps.add(step);
		}

		try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
