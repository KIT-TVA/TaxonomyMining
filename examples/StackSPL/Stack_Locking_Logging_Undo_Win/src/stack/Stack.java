package stack;

import sys.win.Lock;
class Stack {
	int size; Object[] data;
	void push (Object o) {	
		Lock lock = lock();
		if(lock == null) {	
			log("lock failed: " + o);	
		}
		rememberValue();
		data[size++] = o;
}
	void log(String msg) {/*..*/}

	boolean undo() {
		Lock lock = lock();
		if(lock == null) {
			log("undo-lock failed");
			return false;
		}	
		restoreValue();
		log("undone.");
 	return true;
	}
	void rememberValue() {/*..*/}
	void restoreValue() {/*..*/}
	void clean () {/*..*/}
}
