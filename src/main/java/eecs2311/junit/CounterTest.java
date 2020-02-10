package eecs2311.junit;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class CounterTest {

    private Counter c;
    
    @BeforeEach
    public void setUp() throws Exception {
        c = new Counter();
    }

    @Test
    public void testIncrement() {
        assertEquals(1,c.increment());
        assertEquals(2,c.increment());
    }

    @Test
    public void testDecrement() {
        assertEquals(-1,c.decrement());
        assertEquals(-2,c.decrement());
    }
    
    @Test
    public void reset() {
        c.incrementMore(5);
        assertEquals(0,c.reset());
        c.incrementMore(5);
        c.decrement();
        assertEquals(0,c.reset());
    }
    
    @Test
    public void startZero() { 
        assertEquals(0,c.get());
    }
    
    @Test
    public void incrementMore() {
        assertEquals(2,c.incrementMore(2));
        assertEquals(123894,c.incrementMore(123892));
    }

}
