package ca.utoronto.utm.paint;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;

/**
 * @author Mohammad Abufardeh
 * This class creates SquiggleCommand objects.
 * Each SquiggleCommand has point p.
 * IS-A: inheritance 	(The SquiggleCommand inherits from the PaintCommand class).
 * HAS-A: attributes 	(Point p).
 * RESPONDS-TO: methods (add, getPoints, execute, toText and accept).
 * CONSTRUCTORS: 		(None).
 */
public class SquiggleCommand extends PaintCommand{

	//Array list in which all the squiggle points are stored.
	private ArrayList<Point> points=new ArrayList<Point>();

	/**
	 * Add the new given point to the points array list.
	 * @param p		The new point to be added to the points array list.
	 */
	public void add(Point p){
		this.points.add(p);
		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * Return the array list of squiggle points.
	 */
	public ArrayList<Point> getPoints(){ return this.points; }


	/**
	 * Draws the circle object.
	 */
	@Override
	public void execute(GraphicsContext g) {
		ArrayList<Point> points = this.getPoints();
		g.setStroke(this.getColor());
		for(int i=0;i<points.size()-1;i++){
			Point p1 = points.get(i);
			Point p2 = points.get(i+1);
			g.strokeLine(p1.x, p1.y, p2.x, p2.y);
		}

	}

	/**
	 * It is responsible for converting all the user's squiggle commands to a String.
	 * @return	Returns a string of the squiggle command.
	 */
	public String toText() {
		int r = (int)(this.getColor().getRed() * 255);
		int g = (int)(this.getColor().getGreen() * 255);
		int b = (int)(this.getColor().getBlue() * 255);

		String finalString = "";
		finalString += "Squiggle\n";
		finalString += "\tcolor:" + r + "," + g + "," + b + "\n";
		finalString += "\tfilled:" + this.isFill() + "\n";
		finalString += "\tpoints\n";

		for (Point point: this.getPoints()) {
			finalString += "\t\tpoint:(" + point.x + "," + point.y + ")\n";
		}

		finalString += "\tend points\n";
		finalString += "End Squiggle\n";
		return finalString;
	}

	/**
	 * Overrides the accept method in PaintCommand. Sends the current squiggle instance to the Visitor.
	 */
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
