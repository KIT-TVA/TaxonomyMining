package stack;


class Stack {
	int size; 
	Object[] data;
	void push (Object o) {	
		data[size++] = o;
}
	void log(String msg) {/*..*/}

}
