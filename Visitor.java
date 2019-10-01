package ca.utoronto.utm.paint;

/**
 * @author Mohammad Abufardeh
 * Visitor design to save the object depending on the paintCommand (CircleCommand, RectangleCommand, and SquiggleCommand).
 * IS-A: inheritance 	(None).
 * HAS-A: attributes 	(None).
 * RESPONDS-TO: methods (visit).
 * CONSTRUCTORS: 		(None).
 */

public interface Visitor {
	public void visit(CircleCommand circle);
	public void visit(RectangleCommand rectangle);
	public void visit(SquiggleCommand squiggle);

}
