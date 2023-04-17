

import java.util.LinkedList; 
import java.util.List; 
import java.util.Iterator; 

import generator.GeneratorStrategy; 


public abstract 


class  ModelObservable {
	
	private List observers=new LinkedList();

	
	  
	public void attach(ModelObserver o){
		if (o == null) {
      		throw new IllegalArgumentException("Parameter is null");
    	}
    	observers.add(o);
  	}

	
  
  	public void detach(  ModelObserver o){
    	if (o == null) {
      		throw new IllegalArgumentException("Parameter is null");
    	}
    	observers.remove(o);
  	}

	
  	
  	public void notifyObservers(){
    	Iterator it = observers.iterator();
    	while(it.hasNext()) {
    		ModelObserver x;
    		x = (ModelObserver) it.next();
      		x.update();
    	}
  	}

	
  	public abstract void nextGeneration();

	
  
  	public abstract int[][] getPlayground();

	
  	
  	public abstract void setLifeform(  int coord,  int coord2,  int i);

	
  	
  	public abstract void setPlayground(  int[][] pg);

	
	public abstract void generate();

	
	public abstract void setGenerator(GeneratorStrategy generator);

	
	public abstract List getGeneratorStrategies();

	
	 public abstract void undo();

	
 	 public abstract boolean undoAvailable();

	
  	 public abstract void redo();

	
  	 public abstract boolean redoAvailable();


}
