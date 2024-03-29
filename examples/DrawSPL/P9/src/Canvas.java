/**
 * DPL Canvas.java
 * @author Roberto E. Lopez-Herrejon
 * SEP SPL Course July 2010
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.*;
import java.awt.event.*;
import javax.swing.JComponent;


import java.awt.Point;


@SuppressWarnings("serial")
public class Canvas extends JComponent implements MouseListener, MouseMotionListener {

	// Lists of figures objects to display
			protected List<Rectangle> rects = new LinkedList<Rectangle>();
		
	// Auxiliary point
	Point start, end;
	
	// Objects for creating figures to add to the canvas
			protected Rectangle newRect = null;
	
	// Enum for types of figures
	public enum FigureTypes { NONE 
							,RECT 
			};
	
	// The selected default is none. Do not change.
	public FigureTypes figureSelected = FigureTypes.NONE;

		
	/** Sets up the canvas. Do not change */
	public Canvas() { 
		this.setDoubleBuffered(true); // for display efficiency
		this.addMouseListener(this);  // registers the mouse listener
		this.addMouseMotionListener(this); // registers the mouse motion listener
	}

	/** Sets the selected figure. Do not change. */
	public void selectedFigure(FigureTypes fig) {
		figureSelected = fig;
	}
	
		public void wipe() {
						this.rects.clear();
				this.repaint();
	}
		
		
	/** Paints the component in turn. Call whenever repaint is called. */
	public void paintComponent(Graphics g) 	{
		super.paintComponent(g);
		
		// refreshes the canvas
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		// Paints the drawn figures
						for (Rectangle r: rects) { r.paint(g);	}
			
	}
		
	// **************** Mouse Handling
	
	/* Invoked when the mouse button has been clicked (pressed and released) on a component.
	 * Empty implementation. Do not change.
	 */
	public void mouseClicked(MouseEvent e)  { }// mouseClicked
	
    /* Invoked when the mouse enters a component. Empty implementation.
     * Do not change. */     
	public void mouseEntered(MouseEvent e) { }
          
	/** Invoked when the mouse exits a component. Empty implementation. 
	 * Do not change. */
	public void mouseExited(MouseEvent e) {	}
          
	/** Invoked when a mouse button has been pressed on a component. */
	public void mousePressed(MouseEvent e) {
		switch(figureSelected) {
						case RECT: mousePressedRect(e); break;
				}
	}
     
	/** Invoked when a mouse button has been released on a component. */
	public void mouseReleased(MouseEvent e) {
		switch(figureSelected) {
						case RECT: mouseReleasedRect(e); break;
				}
	}
	
	/** Invoked when the mouse is dragged over a component */
	public void mouseDragged(MouseEvent e)	{
		switch(figureSelected) {
						case RECT: mouseDraggedRect(e); break;
				}
	}

	/* Empty implementation. Do not change. */
	public void mouseMoved(MouseEvent e)	{ }

		// **************** Manage Rect

	public void mousePressedRect(MouseEvent e) {
		// If there is no line being created
		if (newRect == null) {
			newRect = new Rectangle (
										e.getX(), e.getY());
			rects.add(newRect);
		}
	}
	
	/** Updates the end point coordinates and repaints figure */
	public void mouseDraggedRect(MouseEvent e) {
		newRect.setEnd(e.getX(), e.getY());
		repaint();	
	}
	
	/** Clears the reference to the new line */
	public void mouseReleasedRect(MouseEvent e) {
		newRect = null;
	}
			
			
} // Canvas
