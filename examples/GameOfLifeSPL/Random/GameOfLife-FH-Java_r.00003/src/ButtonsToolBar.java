

import java.awt.FlowLayout; 
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import java.awt.event.MouseAdapter; 
import java.awt.event.MouseEvent; 
import java.io.File; 
import java.io.IOException; 
import java.net.URL; 
import javax.swing.AbstractAction; 
import javax.swing.Action; 
import javax.swing.ImageIcon; 
import javax.swing.JButton; 
import javax.swing.JFileChooser; 
import javax.swing.JPanel; 
import javax.swing.JToolBar; 

import java.util.List; 
import generator.GeneratorStrategy; 
import java.awt.Point; 

public 

class  ButtonsToolBar  extends JToolBar {
	
  private ModelObservable model;

	
  private final GenerationScheduler sched;

	
  private JButton play;

	
  private JButton pause;

	
  public ButtonsToolBar  (  ModelObservable mod,  final GenerationScheduler sched){
    this.model=mod;
    this.sched=sched;
    this.setLayout(new FlowLayout(FlowLayout.LEFT));
    this.setFloatable(false);
    this.setRollover(true);
    
    addGenerationButton();
    
    play=makeNavigationButton("Play24","Play","Automatisch abspielen","abspielen",new ActionListener(){
      public void actionPerformed(      ActionEvent e){
        sched.start();
        pause.setEnabled(true);
        play.setEnabled(false);
      }
    }
);
    add(play);
    pause=makeNavigationButton("Pause24","Pause","pausieren","pause",new ActionListener(){
      public void actionPerformed(      ActionEvent e){
        sched.stop();
        pause.setEnabled(false);
        play.setEnabled(true);
      }
    }
);
    pause.setEnabled(false);
    add(pause);
    add(makeNavigationButton("StepForward24","SingleStep","Einzelschritt","Einzelschritt",new ActionListener(){
      public void actionPerformed(      ActionEvent e){
        model.nextGeneration();
        sched.stop();
        pause.setEnabled(false);
        play.setEnabled(true);
        model.notifyObservers();
      }
    }
));
  
    add(makeNavigationButton("open24","Load","Laden","Laden",new ActionListener(){
      public void actionPerformed(ActionEvent e){
        JFileChooser fc=new JFileChooser();
        int resp=fc.showOpenDialog(ButtonsToolBar.this);
        if (resp == JFileChooser.APPROVE_OPTION) {
          	File selected=fc.getSelectedFile();
          	if (selected == null || !selected.exists())           
          		return;
          	try {
            	model.setPlayground(PlaygroundIO.loadFromFile(selected));
          	} catch (          IOException e1) {
            	e1.printStackTrace();
          	}
        }
      }
    }
	));
    add(makeNavigationButton("Save24","Save","Speichern","Speichern",new ActionListener(){
      public void actionPerformed(ActionEvent e){
        JFileChooser fc=new JFileChooser();
        int resp=fc.showSaveDialog(ButtonsToolBar.this);
        if (resp == JFileChooser.APPROVE_OPTION) {
          	File selected=fc.getSelectedFile();
          	if (selected == null)
          		return;
          	try {
            	PlaygroundIO.saveToFile(model.getPlayground(),selected);
          	} catch (IOException e1) {
            	e1.printStackTrace();
          	}
        }
      }
    }
	));
  
    undo=makeNavigationButton("Undo24","Rückgängig","Rückgängig","Undo",new ActionListener(){
      public void actionPerformed(ActionEvent e){
        if (model.undoAvailable()) {
          model.undo();
        }
      }
    }
	);
    undo.setEnabled(false);
    add(undo);
    redo=makeNavigationButton("Redo24","Wiederholen","Wiederholen","Redo",new ActionListener(){
      public void actionPerformed(ActionEvent e){
        if (model.redoAvailable()) {
          model.redo();
        }
      }
    }
	);
    redo.setEnabled(false);
    add(redo);
  }

	
  
    private void addGenerationButton  () {
/*		JMenu btn = new GenerationSelector(model);
		String imgLocation = "/ressources/images/" + "Stop24" + ".gif";
		URL imageURL = Main.class.getResource(imgLocation);
		btn.setActionCommand("Generate");
		btn.setToolTipText("neues Feld generieren");
		if (imageURL != null) {
			btn.setIcon(new ImageIcon(imageURL, "Generieren"));
		} else {
			btn.setText("Generieren");
			System.err.println("Resource not found: " + imgLocation);
		}
		
		add(btn);*/
    	
  	  final JButton btn = makeNavigationButton("Stop24","ClearField","Feld generieren","generieren",new ActionListener(){
	      public void actionPerformed(ActionEvent e){
	    	  List gens = model.getGeneratorStrategies();
	    	  if (gens.size() == 1) {
	    		  model.setGenerator((GeneratorStrategy) gens.get(0));
	    		  model.generate();
	    		  return;
	    	  } else if (gens.size() == 0) {
	    		  model.generate();
	    		  return;
	    	  }
	    	  sched.stop();
        	  pause.setEnabled(false);
        	  play.setEnabled(true);
	    	  GenerationSelector s = new GenerationSelector(model);
	    	  Point p = ((JButton)e.getSource()).getLocationOnScreen();
	    	  p.y = p.y + ((JButton)e.getSource()).getHeight();
	    	  s.setLocation(p);
	    	  //s.setLocation(((JButton)e.getSource()).getX(), ((JButton)e.getSource()).getY() + ((JButton)e.getSource()).getHeight());
	    	  s.setVisible(true);
	      }
	  }
	  );
	  add(btn);
	}

	
   private void  update__wrappee__GuiBase  (){
  }

	
  public void update(){
    setUndoRedoAvailable();
    update__wrappee__GuiBase();
  }

	
  /** 
 * @param imageNamename of the image in folder without ".gif"
 * @param actionCommandstring that identifies the button, does not matter
 * @param toolTipTexttool Tip
 * @param altTexttext, that will be displayed if the image does not work
 * @param actActionListener that will be called when the button is pressed
 * @return
 */
  protected JButton makeNavigationButton(  String imageName,  String actionCommand,  String toolTipText,  String altText,  ActionListener act){
    String imgLocation="/ressources/images/" + imageName + ".gif";
    URL imageURL=Main.class.getResource(imgLocation);
    JButton button=new JButton();
    button.setActionCommand(actionCommand);
    button.setToolTipText(toolTipText);
    button.addActionListener(act);
    if (imageURL != null) {
      button.setIcon(new ImageIcon(imageURL,altText));
    }
 else {
      button.setText(altText);
      System.err.println("Resource not found: " + imgLocation);
    }
    return button;
  }

	
  private JButton undo;

	
  private JButton redo;

	
  private void setUndoRedoAvailable(){
    redo.setEnabled(model.redoAvailable());
    undo.setEnabled(model.undoAvailable());
  }


}
