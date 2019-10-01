package ca.utoronto.utm.paint;
import javafx.scene.canvas.GraphicsContext;

/**
 * @author Mohammad Abufardeh
 * This class creates RectangleCommand objects.
 * Each RectangleCommand has p1 and p2.
 * IS-A: inheritance 	(The RectangleCommand inherits from the PaintCommand class).
 * HAS-A: attributes 	(Point p1 and Point p2).
 * RESPONDS-TO: methods (getP1, setP1, getP2, setP2, getTopLeft, getBottomRight, getDimensions, execute, toText and accept).
 * CONSTRUCTORS: 		(RectangleCommand).
 */
public class RectangleCommand extends PaintCommand{

	//RectangleCommand attributes.
	private Point p1,p2;

	/**
	 * The RectangleCommand constructor.
	 * @param p1	initial point of the current rectangle.
	 * @param p2	final point of the current rectangle.
	 */
	public RectangleCommand(Point p1, Point p2){
		this.p1 = p1; this.p2=p2;
		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * Return initial point of the current rectangle.
	 */
	public Point getP1() {
		return p1;
	}

	/**
	 * Set the initial point of the current rectangle to p1.
	 */
	public void setP1(Point p1) {
		this.p1 = p1;
		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * Return final point of the current rectangle.
	 */
	public Point getP2() {
		return p2;
	}

	/**
	 * Set the final point of the current rectangle to p1.
	 */
	public void setP2(Point p2) {
		this.p2 = p2;
		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * Return the top left point of the current rectangle.
	 */
	public Point getTopLeft(){
		return new Point(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y));
	}

	/**
	 * Return the bottom right point of the current rectangle.
	 */
	public Point getBottomRight(){
		return new Point(Math.max(p1.x, p2.x), Math.max(p1.y, p2.y));
	}

	/**
	 * Return the dimensions of the current rectangle.
	 */
	public Point getDimensions(){
		Point tl = this.getTopLeft();
		Point br = this.getBottomRight();
		return(new Point(br.x-tl.x, br.y-tl.y));
	}

	/**
	 * Draws the circle object.
	 */
	@Override
	public void execute(GraphicsContext g) {
		Point topLeft = this.getTopLeft();
		Point dimensions = this.getDimensions();
		if(this.isFill()){
			g.setFill(this.getColor());
			g.fillRect(topLeft.x, topLeft.y, dimensions.x, dimensions.y);
		} else {
			g.setStroke(this.getColor());
			g.strokeRect(topLeft.x, topLeft.y, dimensions.x, dimensions.y);
		}
	}

	/**
	 * It is responsible for converting all the user's rectangle commands to a String.
	 * @return	Returns a string of the rectangle command.
	 */
	public String toText() {
		int r = (int)(this.getColor().getRed() * 255);
		int g = (int)(this.getColor().getGreen() * 255);
		int b = (int)(this.getColor().getBlue() * 255);

		String finalString = "";
		finalString += "Rectangle\n";
		finalString += "\tcolor:" + r + "," + g + "," + b + "\n";
		finalString += "\tfilled:" + this.isFill() + "\n";
		finalString += "\tp1:(" + this.getP1().x + "," +  this.getP1().y + ")\n";
		finalString += "\tp2:(" + this.getP2().x + "," + this.getP2().y + ")\n";
		finalString += "End Rectangle\n";
		return finalString;
	}

	/**
	 * Overrides the accept method in PaintCommand. Sends the current rectangle instance to the Visitor.
	 */
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

}
