package Data;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Farm {
	
	// data members
    private int farm_dim;                                              // farm dimension
    private int farm_id;                                               // farm identification number                                    
    private int farm_eggs_in;                                          // counter for eggs from the hen 
    private int farm_eggs_out;                                         // counter for eggs left for the headquarter
    private Vector<Hen> hens;                                          // hens list within the farm
    private Queue<Egg> eggs;                                           // eggs list of the farm (queue because its stay just a little)
    private final Vector<Employee> employees;                          // list of the employees from the farm
    private boolean can_report_eggs;                                   // used to MONITOR method synchronized Add_egg_to_queue(Egg egg) from Hen class
    private Lock lock_hens_movement;                                   // lock/unlock hens movement
    public Lock lock_eggs;                                             // lock/unlock access to eggs for employees
    private boolean egg_lay_sensor;                                    // true when hen report a new egg
    public Lock lock_egg_lay_sensor;                                   // locker for more threads to access at egg_lay_sensor

    
    // constructor
    public Farm(int farm_id, int farm_dim) {
    	this.can_report_eggs = true;
    	this.farm_dim = farm_dim;
    	this.farm_eggs_in = 0;
    	this.farm_eggs_out = 0;
    	this.farm_id = farm_id;
    	this.hens = new Vector<>();
    	this.eggs = new LinkedList<>();
    	this.employees = new Vector<>();
        this.lock_hens_movement = new ReentrantLock();
        this.lock_eggs = new ReentrantLock();
    	this.egg_lay_sensor = false;
        this.lock_egg_lay_sensor = new ReentrantLock();
        System.out.println("Ferma cu ID = " + this.farm_id  + " a fost creata");
    }
    
    
 // setters
    public void Set_can_report_eggs(boolean var) {
    	this.can_report_eggs = var;
    }
    
    public void SetLock_lock_hens_movement() {
    	this.lock_hens_movement.lock();
    }
    
    public void SetUnLock_lock_hens_movement() {
    	this.lock_hens_movement.unlock();
    }
    
    public void SetEgg_lay_sensor(boolean var) {
    	this.egg_lay_sensor = var;
    }
    
    
    // getters
    public boolean Get_can_report_eggs() {
    	return this.can_report_eggs;
    }
    
    public boolean GetEgg_lay_sensor() {
    	return this.egg_lay_sensor;
    }
    
    public Vector<Hen> GetHens() {
        return this.hens;
    }
    
    
    public Queue<Egg> GetEggs() {
        return this.eggs;
    }

    
    public Vector<Employee> GetEmployees(){
    	return this.employees;
    }
    
   
    public Lock GetLock_hens_movement() {
    	return this.lock_hens_movement;
    }
    
    
    public Lock GetLock_eggs() {
    	return this.lock_eggs;
    }

    
    public int GetFarm_dim() {
        return farm_dim;
    }
  
    
    public int GetFarm_ID() {
        return farm_id;
    }
    
    
    public int GetFarm_eggs_in() {
        return farm_eggs_in;
    }
    
    
    public int GetFarm_eggs_out() {
        return farm_eggs_out;
    }
    
    
    
    // methods
    public void Register_the_hen(Hen hen) {                                     // each hen is registered in the farm
        this.hens.add(hen);
    }
  
    
    public void Add_Employee(Employee employee) {
      	this.employees.add(employee);                                                 // add an employee to the farm list of employees
        System.out.println("Un angajat cu ID = " + employee.GetId() + " a fost adaugat in ferma cu ID = " + this.farm_id);
    }

    
    public synchronized void Add_egg(Egg egg) {                           // add an egg in eggs queue
    	while(Get_can_report_eggs() == false) {                           // wait while Take_First_egg thread work
    		try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
        eggs.add(egg);                                                    // add an egg in eggs queue
        this.farm_eggs_in++;                                              // and increments the counter
        Set_can_report_eggs(false);                                       // switch the monitor
    	notify();                                                         // inform the waiting threads about finish the job
    }
    
    public synchronized Egg Take_First_egg() {
    	while(Get_can_report_eggs() == true) {                            // wait wille add eggs
    		try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	if(eggs.peek() != null) {                                         // if the first record is not empty                    
        	this.farm_eggs_out++;                                         // increment the counter 
        	Set_can_report_eggs(true);                                    // switch the monitor 
        	notify();                                                     // inform the waiting threads about finish the job
        	return eggs.poll();                                           // return and delete the head of the queue
    	}
    	Set_can_report_eggs(true);
    	notify();
    	return null;                                                      // else, return null
    }
    
}