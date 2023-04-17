
import java.awt.event.MouseAdapter; 
import java.awt.event.MouseEvent; 

import java.awt.Color; 
import java.awt.Dimension; 
import java.awt.Graphics; 
import javax.swing.JPanel; 
import javax.swing.JPopupMenu; 

public 

class  PlaygroundMouseAdapter  extends MouseAdapter {
	

		private final PlaygroundPanel playgroundPanel;

	
		private final GenerationScheduler sched;

	

		public PlaygroundMouseAdapter(PlaygroundPanel playgroundPanel, GenerationScheduler sched) {
			this.playgroundPanel = playgroundPanel;
			this.sched = sched;
			// sched is not used in this feature: TODO: Fix this constructor;
		}

	

		public void mousePressed(MouseEvent e){
			super.mousePressed(e);
			int xCoord=(e.getX() - playgroundPanel.getXOffset()) / playgroundPanel.getXBoxWidth();
			int yCoord=(e.getY() - playgroundPanel.getYOffset()) / playgroundPanel.getYBoxWidth();
			if(!hook(e, xCoord, yCoord)) {
				if (e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3) {
					if (playgroundPanel.getFieldOnPlayground(xCoord, yCoord) == 0) {
						playgroundPanel.setFieldOnPlayground(xCoord, yCoord, 1);
					}
				} else {
					playgroundPanel.setFieldOnPlayground(xCoord, yCoord, 0);
				}				
			}
		}

	
		
		public boolean hook  (MouseEvent e, int x, int y) {
			if (e.isPopupTrigger() || e.getButton()==MouseEvent.BUTTON3) {
	            JPopupMenu popup = new PopUpMenu(playgroundPanel.getModel(), x, y);
	            popup.show(playgroundPanel,
	                       e.getX(), e.getY());
	            return true;
	        } else {
	        	return false;
	        }
		}


}
