package Data;
import java.util.Random;


public class Company {
	
	// constants for Headquarter class
	public static final int MIN_EMPLOYEES_NO = 8;                       // minimum number of employees, gives by specifications
	public static final int MAX_EMPLOYEES_NO = 16;                      // maximum number of employees, choose not given by specifications
	public static final int MAX_EMPLOYEES_CAN_READ = 10;                // maximum number of employees cad read system, gives by specifications
    public static final int MIN_FARMS_NO = 3;                           // minimum number of farms (gives by specifications)
    public static final int MAX_FARMS_NO = 5;                           // maximum number of farms (gives by specifications)
    public static final int MIN_FARM_SIZE = 100;                        // minimum of farm size (gives by specifications)
    public static final int MAX_FARM_SIZE = 500;                        // maximum of farm size (gives by specifications)
    public static final int MONITORING_TIME_OUT = 2000;                 // time out between monitoring tasks (choose, not given by specifications)
	// constants for Employee class
    public static final int MIN_PAUSE_MONITORING_TIME = 10;             // minimum time between two tasks, choose not given by specification
    public static final int MAX_PAUSE_MONITORING_TIME = 20;             // maximum time between two tasks, choose not given by specifications
	// constants for Hen class
    public static final int MIN_SPAWNING_TIME = 500;                    // minimum time between two spawning the egg, gives by specifications
    public static final int MAX_SPAWNING_TIME = 1000;                   // maximum time between two spawning the egg, gives by specifications
    public static final int MIN_CAN_NOT_MOVE_TIME = 10;                 // minimum time waiting if the hen is blocked, gives by specifications
    public static final int MAX_CAN_NOT_MOVE_TIME = 50;                 // maximum time waiting if the hen is blocked, gives by specifications
    public static final int AFTER_SPAWNING_RESTING_TIME = 30;           // resting time after the hen spawn an egg, gives by specifications
    
    
	public static Random random = new Random();                   // initialises a random  sequence, used throughout the program
	
	
	
	public static void main(String[] args) {
		
		Headquarter headquarter = new Headquarter();              // instantiate an object from Headquarter class
		headquarter.start();                                      // call the start() method of the instantiated object which start the application
	}

}
