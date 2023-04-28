package stack;


class Stack {
	int size; 
	Object[] data;
	void push (Object o) {	
		Lock lock = lock();
		if(lock == null) {	
			log("lock failed: " + o);	
		}		
		data[size++] = o;
}
	void log(String msg) {/*..*/}

	void clean () {/*..*/}
}
