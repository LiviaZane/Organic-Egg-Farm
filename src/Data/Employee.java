package Data;

import java.util.LinkedList;
import java.util.Queue;

public class Employee implements Runnable {
	
	// data members
    private int id;                                              // identification number of the employee, between 8 (conf specif) and 16 (choose)
    private Farm farm;                                           // farm which employee belongs to
    private Headquarter headquarter;                             // an instantiated object, needed to access colected_egss method from Headquarter class
    private Queue<Egg> waiting_eggs;                             // buffer for eggs from farm, waiting to deliver to headquarter
    
    
    // constructor
    public Employee(int id, Farm farm, Headquarter headquarter) {
        this.id = id;
        this.farm = farm;
        this.headquarter = headquarter;
        this.waiting_eggs = new LinkedList<>();
    }

    
    @Override
    public void run() {                                    // override the run method from Runnable interface
    	Egg temp = null;                                   // a temporary variable needed below to move an egg from farm to headquarter
        while (true) {
        	while(headquarter.GetMonitoring_sensor()) {    // while monitoring the system this thread loops
        	}
        	farm.lock_eggs.lock();                         // lock the eggs lock and after
			temp = farm.Take_First_egg();                  // take the first record from eggs list within farm is copied in temporary variable
			if (temp != null) {
				this.waiting_eggs.add(temp);               // add the egg in employee egg queue
        	}
        	if(headquarter.GetSemaphore_employees().tryAcquire()) {                  // if can aquire a permit from semaphore
        		deliver_the_waiting_eggs(farm);                                      // employee send the egg to the headquarter
        		headquarter.GetSemaphore_employees().release();                      // and release the semaphore
	               	System.out.println("Un ou a fost livrat la sediul firmei de angajatul cu ID = " + this.id + " din ferma cu ID = " + farm.GetFarm_ID());
				try {
					int waiting_time = Company.random.nextInt(Company.MAX_PAUSE_MONITORING_TIME - Company.MIN_PAUSE_MONITORING_TIME + 1) 
        				+ Company.MIN_PAUSE_MONITORING_TIME;
					Thread.sleep(waiting_time);                                  // after success task, the employee sleep for waiting time
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }
    }

    
    // getter
    public int GetId() {                                                     // used in system_monitoring method from Headqurter class
    	return this.id;
    }
    
    
    // method
    public void deliver_the_waiting_eggs(Farm farm) {                        // move the selected egg from farm to headquarter by colling
    	while(this.waiting_eggs.peek() != null) {                            // while the head of queue is not empty
    		headquarter.Receive_an_egg(this.waiting_eggs.poll());            // deliver the egg to headquarter and delete it from queue 
    	    System.out.println("La sediul firmei a fost receptionat un ou de la ferma cu ID = " + farm.GetFarm_ID());
    	}
    }
    
}