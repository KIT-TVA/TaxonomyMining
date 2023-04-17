/**
 * DPL Main.java
 * @author Roberto E. Lopez-Herrejon
 * Main class of Draw Product Line
 * SEP SPL Course July 2010
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
@SuppressWarnings("serial")  

public class Main extends JFrame {
	
	// *** Initialize constants
	
	// Window size
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	

	// Button names
	// Define string constants here
				private static final String rectText = "Rectangle";
		
	// Color handling
	// Use a vector to hold the names of the options
		
	// Declare string constants for the colors 
		
	// *** Declares atomic elements

	// Declare bottons
				JButton rectButton;
		
	// Declaration for colors combo box 
	
	// Pane declaration. No need to use more panels or canvas.
	protected JPanel toolPanel = new JPanel();
		protected Canvas canvas = new Canvas();
	
	// *** Initialization of atomic elements
	public void initAtoms() {
		
		// Initilize the buttons
								rectButton = new JButton(rectText);
				
				
	} // initAtoms
	
	// Layout components declaration
	Container contentPane;
	
	/** Initializes layout . No need to change */
	public void initLayout() {
		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		toolPanel.setLayout(new BoxLayout(toolPanel, BoxLayout.Y_AXIS));
	}
	
	/** Initializes the content pane */
	public void initContentPane() {
		// Add buttons to tool panel
		// Note: order of addition determines the order of appearance
								toolPanel.add(rectButton);
						
		// Adds the tool and canvas panels to the content pane
		// Note: No need to change the following two lines
		contentPane.add(toolPanel, BorderLayout.WEST);
		contentPane.add(canvas, BorderLayout.CENTER);
		
	} // initContentPane
	
	/** Initializes the listeners for the buttons and the combo box */
	public void initListeners() {
						rectButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				canvas.selectedFigure(Canvas.FigureTypes.RECT);
			}
		});
								
	} // of initListeners
	
	// Initializes entire containment hierarchy
	public void init() {
		initAtoms();
		initLayout();
		initContentPane();
		initListeners();
	}
	
	/* Constructor. No need to modify */
	public Main(String appTitle) {
		super(appTitle);
		init();
		addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setVisible(true);
		setSize(WIDTH, HEIGHT);
		setResizable(true);
		validate();
	} // Main constructor
	
	/** main method */
	public static void main(String[] args) {
		new Main("Draw Product Line");
	}
	
}
