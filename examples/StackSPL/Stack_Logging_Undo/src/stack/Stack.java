package stack;

class Stack {
	int size; Object[] data;
	void push (Object o) {	
		rememberValue();
		data[size++] = o;
}
	void log(String msg) {/*..*/}

	boolean undo() {
		restoreValue();
		log("undone.");
 	return true;
	}
	void rememberValue() {/*..*/}
	void restoreValue() {/*..*/}
	void clean () {/*..*/}
}
