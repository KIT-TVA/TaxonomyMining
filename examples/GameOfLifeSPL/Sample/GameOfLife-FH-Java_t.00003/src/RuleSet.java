


import java.util.Arrays; 

public 

class  RuleSet {
	
	private final boolean[] causesBirth;

	
	private final boolean[] causesDeath;

	
	
	public RuleSet(boolean[] causesBirth, boolean[] causesDeath) {
		this.causesBirth = causesBirth;
		this.causesDeath = causesDeath;
	}

	
	
	int apply(LifeForm lf) {
		int type = lf.getType();
		int size = 0;
		for(int i = 0; i < lf.getNeighbourhood().length; i++) {
			if (lf.getNeighbourhood()[i] > 0) {
				size++;
			}
		}
		if( (type == 0 && causesBirth[size]) || (type==1 && !causesDeath[size]) ) {
			return 1;
		} else {
			return 0;
		}
	}

	
	public boolean equals(Object o) {
		if(o == null) {
			return false;
		} else if(o instanceof RuleSet) {
			RuleSet rso = (RuleSet) o;
			return Arrays.equals(rso.causesBirth, this.causesBirth) && Arrays.equals(rso.causesDeath, this.causesDeath);
		} else {
			return false;
		}
	}


}
