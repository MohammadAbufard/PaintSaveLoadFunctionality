package ca.utoronto.utm.paint;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Parse a file in Version 1.0 PaintSaveFile format. An instance of this class
 * understands the paint save file format, storing information about
 * its effort to parse a file. After a successful parse, an instance
 * will have an ArrayList of PaintCommand suitable for rendering.
 * If there is an error in the parse, the instance stores information
 * about the error. For more on the format of Version 1.0 of the paint
 * save file format, see the associated documentation.
 *
 * @author Mohammad Abufardeh (abufard1)
 *
 */
public class PaintFileParser {
	private int lineNumber = 0; // the current line being parsed
	private String errorMessage =""; // error encountered during parse
	private PaintModel paintModel;

	/**
	 * Below are Patterns used in parsing
	 */
	private Pattern pFileStart=Pattern.compile("^PaintSaveFileVersion1.0$");
	private Pattern pFileEnd=Pattern.compile("^EndPaintSaveFile$");

	private Pattern pCircleStart=Pattern.compile("^Circle$");

	private Pattern pCircleEnd=Pattern.compile("^EndCircle$");
	private Pattern fileCenter = Pattern.compile("^center:\\((\\d{2,3}),(\\d{2,3})\\)+$");
	private Pattern fileRadius = Pattern.compile("^radius:(\\d{1,3})$");


	private Pattern pRectangleStart=Pattern.compile("^Rectangle$");
	private Pattern pRectangleEnd=Pattern.compile("^EndRectangle$");
	private Pattern fileP1 = Pattern.compile("^p1:\\((\\d{1,3}),(\\d{1,3})\\)+$");
	private Pattern fileP2 = Pattern.compile("^p2:\\((\\d{1,3}),(\\d{1,3})\\)+$");


	private Pattern pSquiggleStart=Pattern.compile("^Squiggle$");
	private Pattern pSquiggleEnd=Pattern.compile("^EndSquiggle$");
	private Pattern filePoint = Pattern.compile("^([a-z]+)$");
	private Pattern fileEndPoint = Pattern.compile("^([a-z]+)$");
	private Pattern filePoints = Pattern.compile("^point:\\((\\d{1,3}),(\\d{1,3})\\)+$");

	private Pattern fileColor = Pattern.compile("^color:([0-9]{1,3}),([0-9]{1,3}),([0-9]{1,3})+$");
	private Pattern fileFilled = Pattern.compile("^filled:([a-z]+)$");
	private Pattern fileEmpty = Pattern.compile("");


	/**
	 * Store an appropriate error message in this, including
	 * lineNumber where the error occurred.
	 * @param mesg
	 */
	private void error(String mesg){
		this.errorMessage = "Error in line "+lineNumber+" "+mesg;
	}

	/**
	 *
	 * @return the error message resulting from an unsuccessful parse
	 */
	public String getErrorMessage(){
		return this.errorMessage;
	}

	/**
	 * Parse the inputStream as a Paint Save File Format file.
	 * The result of the parse is stored as an ArrayList of Paint command.
	 * If the parse was not successful, this.errorMessage is appropriately
	 * set, with a useful error message.
	 *
	 * @param inputStream the open file to parse
	 * @param paintModel the paint model to add the commands to
	 * @return whether the complete file was successfully parsed
	 */
	public boolean parse(BufferedReader inputStream, PaintModel paintModel) {
		this.paintModel = paintModel;
		this.errorMessage="";

		// During the parse, we will be building one of the
		// following commands. As we parse the file, we modify
		// the appropriate command.

		CircleCommand circleCommand = null;
		RectangleCommand rectangleCommand = null;
		SquiggleCommand squiggleCommand = null;

		try {
			int state=0; Matcher m; String l;
			this.lineNumber=0;
			while ((l = inputStream.readLine()) != null) {

				System.out.println("Line: " + l);

				this.lineNumber++;
				switch(state){
					case 0:
						m=pFileStart.matcher(l.replaceAll("\\s",""));

						if(m.matches()){
							state=1;
						}
						else {
							error("Expected Paint Save File Version 1.0");
							throw new Exception(this.getErrorMessage());
						}
						break;
					case 1: // Looking for the start of a new object or end of the save file
						m=pCircleStart.matcher(l.replaceAll("\\s",""));
						if(m.matches()){
							circleCommand = new CircleCommand(new Point(0,0), 0);
							state=2;
						}

						else if(pRectangleStart.matcher(l.replaceAll("\\s","")).matches()){
							rectangleCommand = new RectangleCommand(new Point(0,0), new Point(0,0));
							state=7;
						}

						else if(pSquiggleStart.matcher(l.replaceAll("\\s","")).matches()){
							squiggleCommand = new SquiggleCommand();
							state=12;
						}

						else if(pFileEnd.matcher(l.replaceAll("\\s","")).matches()){
							state=1;
						}
						else if(fileEmpty.matcher(l.replaceAll("\\s","")).matches()){
							state=1;
						}
						else {
							error("Expected Shape name");
							throw new Exception(this.getErrorMessage());
						}
						break;

					case 2:
						m=fileColor.matcher(l.replaceAll("\\s",""));
						if(m.matches()){
							if (Integer.parseInt(m.group(1)) <= 255 && Integer.parseInt(m.group(1)) >= 0 && Integer.parseInt(m.group(2)) <= 255 && Integer.parseInt(m.group(2)) >= 0 && Integer.parseInt(m.group(3)) <= 255 && Integer.parseInt(m.group(3)) >= 0) {
								circleCommand.setColor(Color.rgb(Integer.parseInt(m.group(1)),Integer.parseInt(m.group(2)),Integer.parseInt(m.group(3))));
								state=3;
							}
							else {
								error("Invalid color input");
								throw new Exception(this.getErrorMessage());
							}
						}

						else {
							error("Expected color");
							throw new Exception(this.getErrorMessage());
						}
						break;

					case 3:
						m=fileFilled.matcher(l.replaceAll("\\s",""));
						if(m.matches()){
							circleCommand.setFill(Boolean.parseBoolean(m.group(1)));
							state=4;
						}
						else {
							error("Expected filled");
							throw new Exception(this.getErrorMessage());
						}
						break;

					case 4:
						m=fileCenter.matcher(l.replaceAll("\\s",""));
						if(m.matches()){
							circleCommand.setCentre(new Point(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2))));
							state=5;
						}
						else {
							error("Expected center");
							throw new Exception(this.getErrorMessage());
						}
						break;

					case 5:
						m=fileRadius.matcher(l.replaceAll("\\s",""));
						if(m.matches()){
							circleCommand.setRadius(Integer.parseInt(m.group(1)));
							state=6;
						}
						else {
							error("Expected radius");
							throw new Exception(this.getErrorMessage());
						}
						break;

					case 6:
						m=pCircleEnd.matcher(l.replaceAll("\\s",""));
						if(m.matches()){
							paintModel.addCommand(circleCommand);
							state=1;
						}
						else {
							error("Expected End Circle");
							throw new Exception(this.getErrorMessage());
						}
						break;

					case 7:
						m=fileColor.matcher(l.replaceAll("\\s",""));
						if(m.matches()){
							if (Integer.parseInt(m.group(1)) <= 255 && Integer.parseInt(m.group(1)) >= 0 && Integer.parseInt(m.group(2)) <= 255 && Integer.parseInt(m.group(2)) >= 0 && Integer.parseInt(m.group(3)) <= 255 && Integer.parseInt(m.group(3)) >= 0) {
								rectangleCommand.setColor(Color.rgb(Integer.parseInt(m.group(1)),Integer.parseInt(m.group(2)),Integer.parseInt(m.group(3))));
								state=8;
							}
							else {
								error("Invalid color input");
								throw new Exception(this.getErrorMessage());
							}
						}
						else {
							error("Expected color");
							throw new Exception(this.getErrorMessage());
						}
						break;

					case 8:
						m=fileFilled.matcher(l.replaceAll("\\s",""));
						if(m.matches()){
							rectangleCommand.setFill(Boolean.parseBoolean(m.group(1)));
							state=9;
						}
						else {
							error("Expected filled");
							throw new Exception(this.getErrorMessage());
						}
						break;

					case 9:
						m=fileP1.matcher(l.replaceAll("\\s",""));
						if(m.matches()){
							rectangleCommand.setP1(new Point(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2))));
							state=10;
						}
						else {
							error("Expected p1");
							throw new Exception(this.getErrorMessage());
						}
						break;

					case 10:
						m=fileP2.matcher(l.replaceAll("\\s",""));
						if(m.matches()){
							rectangleCommand.setP2(new Point(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2))));
							state=11;
						}
						else {
							error("Expected p2");
							throw new Exception(this.getErrorMessage());
						}
						break;

					case 11:
						m=pRectangleEnd.matcher(l.replaceAll("\\s",""));
						if(m.matches()){
							paintModel.addCommand(rectangleCommand);
							state=1;
						}
						else {
							error("Expected End Rectangle");
							throw new Exception(this.getErrorMessage());
						}
						break;

					case 12:
						m=fileColor.matcher(l.replaceAll("\\s",""));
						if(m.matches()){
							if (Integer.parseInt(m.group(1)) <= 255 && Integer.parseInt(m.group(1)) >= 0 && Integer.parseInt(m.group(2)) <= 255 && Integer.parseInt(m.group(2)) >= 0 && Integer.parseInt(m.group(3)) <= 255 && Integer.parseInt(m.group(3)) >= 0) {
								squiggleCommand.setColor(Color.rgb(Integer.parseInt(m.group(1)),Integer.parseInt(m.group(2)),Integer.parseInt(m.group(3))));
								state=13;
							}
							else {
								error("Invalid color input");
								throw new Exception(this.getErrorMessage());
							}
						}
						else {
							error("Expected color");
							throw new Exception(this.getErrorMessage());
						}
						break;

					case 13:
						m=fileFilled.matcher(l.replaceAll("\\s",""));
						if(m.matches()){
							squiggleCommand.setFill(Boolean.parseBoolean(m.group(1)));
							state=14;
						}
						else {
							error("Expected filled");
							throw new Exception(this.getErrorMessage());
						}
						break;

					case 14:
						m=filePoint.matcher(l.replaceAll("\\s",""));
						if(m.matches()){
							state=15;
						}
						else {
							error("Expected points");
							throw new Exception(this.getErrorMessage());
						}
						break;

					case 15:
						m=filePoints.matcher(l.replaceAll("\\s",""));
						if(m.matches()){
							squiggleCommand.add(new Point(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2))));
							state=15;
						}

						else if (!m.matches()) {
							m=fileEndPoint.matcher(l.replaceAll("\\s",""));
							if(m.matches()){
								state=17;
							}
							else {
								error("Expected end points");
								throw new Exception(this.getErrorMessage());
						}
						}
						else {
							error("Expected point");
							throw new Exception(this.getErrorMessage());
						}
						break;

					case 17:
						m=pSquiggleEnd.matcher(l.replaceAll("\\s",""));
						if (m.matches()) {
							paintModel.addCommand(squiggleCommand);
							state=1;
						}
						else {
							error("Expected End Squiggle");
							throw new Exception(this.getErrorMessage());
						}
						break;
					}
				}

			// Checks at the end of reading
			if (state != 1) {
				error("Expected End Paint Save File");
				throw new Exception(this.getErrorMessage());
			}
		}  catch (Exception e){
			return false;

		}
		return true;
	}
}
