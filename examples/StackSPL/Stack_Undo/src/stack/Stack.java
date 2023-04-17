package stack;

class Stack {
	int size; Object[] data;
	void push (Object o) {	
		rememberValue();
		data[size++] = o;
}

	boolean undo() {
		restoreValue();
 	return true;
	}
	void rememberValue() {/*..*/}
	void restoreValue() {/*..*/}
}
