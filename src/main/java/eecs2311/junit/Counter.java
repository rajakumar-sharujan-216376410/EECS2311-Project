package eecs2311.junit;

public class Counter {

    private int data;
    
    public Counter() {
        data = 0;
    }
    
    public int get() {
    	return data;
    }
    
    public int increment() {
        data++;
        return data;
    }
    
    public int decrement() {
        data--;
        return data;
    }
    
    public int reset() {
    	data = 0;
    	return data;
    }
    
    public int incrementMore(int more) {
    	data = data + more;
    	return data;
    }
    
}