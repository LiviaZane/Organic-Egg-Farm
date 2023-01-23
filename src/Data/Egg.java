package Data;

public class Egg {
	
	// data members
    private int x;                                                     // position of the egg on x axe in the farm matrix
    private int y;                                                     // position of the egg on y axe in the farm matrix

    
    // constructor
    public Egg(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    
    // getters
    public int GetX() {
    	return x;
    }
    
    
    public int GetY() {
    	return y;
    }
    
}

