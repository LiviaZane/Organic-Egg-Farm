package Data;

import java.util.Vector;
import java.util.concurrent.Semaphore;

public class Headquarter {
	
	// data members
    private boolean monitoring_sensor;                          // true when the headquarter monitoring the system
    private Vector<Farm> farms;                                 // list of farms (gives by specifications)
    private Vector<Egg> collected_eggs;                         // list of the eggs delivered by employees from farms
    private Semaphore semaphore_employees;                      // semaphore for employees to move eggs into headquarter (10 employees at a time)
    
    
    //constructor
    public Headquarter() {
    	this.semaphore_employees= new Semaphore(Company.MAX_EMPLOYEES_CAN_READ);
    	this.monitoring_sensor = false;
    	this.collected_eggs = new Vector<>();
    	this.farms = new Vector<>();
    }
   
    
    // getters
    public boolean GetMonitoring_sensor() {                    // used in Emplyee class to MONITOR execution of the synchronized run method
    	return this.monitoring_sensor;
    }
    
    
    public Semaphore GetSemaphore_employees() {
        return this.semaphore_employees;
    }
    
        
    
    // methods
    public void start() {                                       // start the system
        int farms_no = Company.random.nextInt(Company.MAX_FARMS_NO - Company.MIN_FARMS_NO + 1) + Company.MIN_FARMS_NO;
        int farm_dim;                                          // dimension of farm, random number between MIN_FARM_SIZE and MAX_FARM_SIZE
        for (int i = 0; i < farms_no; i++) {                   // create a random number of farms (between MIN_FARMS and MAX_FARMS)
        	farm_dim = Company.random.nextInt(Company.MAX_FARM_SIZE - Company.MIN_FARM_SIZE + 1) + Company.MIN_FARM_SIZE;
            farms.add(new Farm(i, farm_dim));                  // add the farm to farms list
        }
        for (Farm farm : farms) {                              //add a random number of  hens (maximum Farm_dim / 2) for each farm
            int hens_no = Company.random.nextInt((int)(farm.GetFarm_dim() / 2) - (int)(farm.GetFarm_dim() / 2.5) + 1) + (int)(farm.GetFarm_dim() / 2.5);
            for (int j = 0; j < hens_no; j++) {
                Hen hen = new Hen(farm, this);                 // create a new hen
                farm.Register_the_hen(hen);                    // call the function who will registered the hen to his farm
                new Thread(hen).start();                       // each hen run in his own thread
            }
        } 
        for (Farm farm : farms) {             // add a random number of employees to each farm (between MIN_EMPLOYEES and MAX_EMPLOYEES)
        	int employees_no = Company.random.nextInt(Company.MAX_EMPLOYEES_NO - Company.MIN_EMPLOYEES_NO + 1) + Company.MIN_EMPLOYEES_NO;
        	for (int i = 0; i < employees_no; i++) {
        		Employee employee = new Employee(i, farm, this);            // create a new employee
        		farm.Add_Employee(employee);                                // add the employee to the farm list witch belong
        		new Thread(employee).start();                               // each employee run in his own thread
        	}
        }
        
        system_monitoring();                                                // call the system monitoring method

    }
    
    
    private void system_monitoring() {
    	while (true) {
   			this.monitoring_sensor = true;                                  // employee and hen threads pass in wait state
   			System.out.println("===============================  RAPORT MONITORIZARE ========================================");
   			for(Farm farm : this.farms) {            // for each farm, locks insertion in eggs list and read the eggs number (queue size)
   				farm.SetLock_lock_hens_movement();   // lock/unlock the hens movement to read the hens position
   					for(Hen hen : farm.GetHens()) {
   						int X, Y;
   						X = hen.GetX();                         // read position for each hen
   						Y = hen.GetY();
   					}
   				farm.SetUnLock_lock_hens_movement();           // unlock the lock_hens_movement
   				System.out.println("Am citit pozitia gainilor din Ferma cu ID = " + farm.GetFarm_ID());
   				System.out.println("     Ferma are dimensiunea = " + farm.GetFarm_dim() 
				+ " si " + farm.GetEmployees().size() + " angajati, are " + farm.GetHens().size() + " gaini, cu " + farm.GetFarm_eggs_in() 
				+ " oua inregistrate si " + farm.GetFarm_eggs_out() + " livrate.");
   			}
   			System.out.println("---------------------------------------------------------------------------------------------");
   			System.out.println("           La sediul firmei (headquarter) au fost receptionate " + this.collected_eggs.size() + " oua");
			System.out.println("=============================================================================================");
    		this.monitoring_sensor = false;                                     // employee and hen threads continue
    		try {
    			Thread.sleep(Company.MONITORING_TIME_OUT);                      // thread takes a time out
    		} catch (InterruptedException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
    	}	
    }
    
    
    public void Receive_an_egg(Egg egg) {
    	collected_eggs.add(egg);
    }
    
}