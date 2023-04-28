package stack;


class Stack {
	int size; 
	Object[] data;
	void push (Object o) {	
		Lock lock = lock();
		if(lock == null) {	
		}		
		data[size++] = o;
}

	void clean () {/*..*/}
}
