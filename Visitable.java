package ca.utoronto.utm.paint;

/**
 * @author Mohammad Abufardeh
 * Accept the visitor based off corresponding paintCommand (CircleCommand, RectangleCommand, and SquiggleCommand).
 * IS-A: inheritance 	(None).
 * HAS-A: attributes 	(None).
 * RESPONDS-TO: methods (accept).
 * CONSTRUCTORS: 		(None).
 */

public interface Visitable {
	public void accept(Visitor visitor);
}
