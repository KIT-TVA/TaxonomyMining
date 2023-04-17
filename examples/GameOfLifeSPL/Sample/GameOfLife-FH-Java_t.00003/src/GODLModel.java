

import java.util.Iterator; 
import java.util.ArrayList; 
import java.util.Collections; 
import java.util.List; 


import generator.GeneratorStrategy; 
import generator.ClearGeneratorStrategy; 


import generator.RandomGeneratorStrategy; 

import java.util.LinkedList; 


public  


class  GODLModel  extends ModelObservable {
	

	private RuleSet rules;

	
	private Playground playground;

	
	private List generators;

	
	public GODLModel  (int xSize, int ySize, RuleSet rules) {
		this.rules = rules;
		this.playground = new Playground(xSize, ySize, 0);
		this.generators = new java.util.ArrayList();
	
		generators.add(new ClearGeneratorStrategy());
	
		RandomGeneratorStrategy rgs = new RandomGeneratorStrategy();
		generators.add(rgs);
	
    	this.undoList = new LinkedList();
        this.redoList = new LinkedList();
    
		for (int i = 0;  i < generators.size(); i++) {
			if (generators.get(i) instanceof RandomGeneratorStrategy) {
				generator = (GeneratorStrategy) generators.get(i);
				break;
			}
		}  
	}

	
	
	
	 private void  setLifeform__wrappee__ModelBase  (int x, int y, int value) {
		playground.set(x, y, value);
		notifyObservers();
	}

	
	
    public void setLifeform(int x, int y, int value) {
        undoList.push((Object) playground.clone());
        setLifeform__wrappee__ModelBase(x, y, value);
	}

	
		
	 private void  setPlayground__wrappee__ModelBase  (int[][] pg) {
		Playground newGround = new Playground(pg.length, pg[0].length, 0);
		for(int i = 0; i < pg.length; i++) {
			for(int j = 0; j < pg[i].length; j++) {
				newGround.set(i, j, pg[i][j]);
			}
		}
		this.playground = newGround;
		notifyObservers();
	}

	

	public void setPlayground(int[][] pg) {
	    undoList.push((Object) playground.clone());
        setPlayground__wrappee__ModelBase(pg);
	}

	
	
	 private void  nextGeneration__wrappee__ModelBase  () {
		Playground newGround = new Playground(playground.getXSize(), playground.getYSize(), playground.getGeneration() + 1);
		Iterator it = playground.iterator();
		while(it.hasNext()) {
			LifeForm current = (LifeForm) it.next();
            newGround.set(current.getX(), current.getY(),  rules.apply(current));
		}
		this.playground = newGround;
		notifyObservers();
	}

	

    public void nextGeneration() {
        undoList.push((Object) playground.clone());
        nextGeneration__wrappee__ModelBase();
    }

	
			
	public int[][] getPlayground() {
		return playground.getField();
	}

	
	
	private GeneratorStrategy generator = null;

	
	public void setGenerator(GeneratorStrategy generator) {
		this.generator = generator;
	}

	
	public List getGeneratorStrategies() {
		return java.util.Collections.unmodifiableList(this.generators);
	}

	
	
	 private void  generate__wrappee__AbstractGenerator  () {
		if (generator == null) {
			generator = new ClearGeneratorStrategy();
		}
		Playground newGround = new Playground(playground.getXSize(), playground.getYSize(), 0);
		Iterator it = playground.iterator();
		while(it.hasNext()) {
			LifeForm current = (LifeForm) it.next();
			newGround.set(current.getX(), current.getY(), generator.getNext(current.getX(), current.getY()));
		}
		this.playground = newGround;
		notifyObservers();
	}

	
	public void generate() {
		undoList.push((Object) playground.clone());
		generate__wrappee__AbstractGenerator();
	}

		
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(playground);
		return sb.toString();
	}

	
	
	public boolean equals(Object o) {
		if(o == null) {
			return false;
		} else if(o instanceof GODLModel) {
			GODLModel ogm = (GODLModel) o;
			return playground.equals(ogm.playground) && rules.equals(ogm.rules);
		} else {
			return false;
		}
	}

	
	private final LinkedList undoList;

	
	private final LinkedList redoList;

	

    public void undo() {
        redoList.push((Object) playground);
        playground = (Playground) undoList.pop();
        notifyObservers();
    }

	

    public void redo() {
        undoList.push((Object) playground);
        playground = (Playground) redoList.pop();
        notifyObservers();
    }

	
    
    public boolean undoAvailable() {
		return undoList != null && !undoList.isEmpty();
	}

	
	
	public boolean redoAvailable() {
		return redoList != null && !redoList.isEmpty();
	}


}
