package stack;

import sys.win.Lock;
class Stack {
	int size; Object[] data;
	void push (Object o) {	
		Lock lock = lock();
		if(lock == null) {	
		}
		rememberValue();
		data[size++] = o;
}

	boolean undo() {
		Lock lock = lock();
		if(lock == null) {
			return false;
		}	
		restoreValue();
 	return true;
	}
	void rememberValue() {/*..*/}
	void restoreValue() {/*..*/}
	void clean () {/*..*/}
}
