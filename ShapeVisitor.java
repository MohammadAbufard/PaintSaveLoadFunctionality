package ca.utoronto.utm.paint;

import java.io.PrintWriter;

/**
 * @author Mohammad Abufardeh
 * This class implements the visitor interface and contains all the methods responsible
 * for adding text into the text file.
 * IS-A: inheritance 	(The ShapeVisitor class implements the visitor interface).
 * HAS-A: attributes 	(PrintWriter p).
 * RESPONDS-TO: methods (visit).
 * CONSTRUCTORS: 		(ShapeVisitor).
 */
public class ShapeVisitor implements Visitor {

	PrintWriter printWriter;


	public ShapeVisitor(PrintWriter p) {
		printWriter = p;
	}


	/**
	 * This method overrides the visit method in the Visitor interface.
	 * It is responsible for adding all the user's Circle commands into the text file.
	 */
	@Override
	public void visit(CircleCommand circle) {
		printWriter.print(circle.toText());
	}

	/**
	 * This method overrides the visit method in the Visitor interface.
	 * It is responsible for adding all the user's Rectangle commands into the text file.
	 */
	@Override
	public void visit(RectangleCommand rectangle) {
		printWriter.print(rectangle.toText());
	}


	/**
	 * This method overrides the visit method in the Visitor interface.
	 * It is responsible for adding all the user's Squiggle commands into the text file.
	 */
	@Override
	public void visit(SquiggleCommand squiggle) {
		printWriter.print(squiggle.toText());
	}

}
