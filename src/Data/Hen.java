package Data;

public class Hen implements Runnable {
	
	// data members
    private int x;                                                   // position of the hen on x axe in the farm matrix
    private int y;                                                   // position of the hen on x axe in the farm matrix
    private boolean hen_alive;                                       //  variable for the hen witch is alive, gives by specifications
    private Farm farm;                                               // farm which the hen belongs to
    private Headquarter headquarter;                                 // used to access headquarter.GetMonitoring_sensor()

    
    // constructor
    public Hen(Farm farm, Headquarter headquarter) {
    	this.farm = farm;
    	int temp_x = 0, temp_y = 0; 
    	boolean hen_not_placed = true;                        // start with this boolean variable to true and switch false if the place was changed
    	while(hen_not_placed) {
    		temp_x = Company.random.nextInt(farm.GetFarm_dim());     // chose a random place on x axe of the farm matrix where hen will be placed
            temp_y = Company.random.nextInt(farm.GetFarm_dim());     // chose a random place on y axe of the farm matrix where hen will be placed
            if (Can_be_placed(temp_x, temp_y)) {                     // boolean Can_be_placed verify if the hen can be placed in the position x,y
	            hen_not_placed = false;                              // and if it is true, switch hen_not_placet to true
	        }
    	}
        this.x = temp_x;                                             // after set x and y of the object hen
        this.y = temp_y;
        this.hen_alive = true;                                       // and hen_alive to true, because rise hen is alive
        this.headquarter = headquarter;
        System.out.println("O noua gaina a crescut in ferma cu ID = " + farm.GetFarm_ID());
    }

    
    // getters  
    public int GetX(){
    	return this.x;
    }
    
    public int GetY(){
    	return this.y;
    }
    
    
    @Override
    public void run() {                                                    // override the run method from Runnable interface
    	while(headquarter.GetMonitoring_sensor()) {                        // while monitoring the system this threads waiting
    	}	
    	while (this.hen_alive) {
        	try {
            	Move_hen();          // call the method Move_hen which verify if it is possible to move the hen
                Thread.sleep(Company.random.nextInt(Company.MAX_SPAWNING_TIME - Company.MIN_SPAWNING_TIME + 1) + Company.MIN_SPAWNING_TIME);                        
            } catch (InterruptedException e) {
            	// TODO Auto-generated catch block
				e.printStackTrace();
            }
        }
    }

    
    // methods
	private synchronized void Move_hen() {
    	while(farm.GetEgg_lay_sensor() == true) {                          // waiting while sensor is false (hen reports egg lay) 
    		try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
		String message_moved= "";
        int direction = Company.random.nextInt(4); // 0 - left, 1 - right, 2 - up, 3 - down
        int newX = x;
        int newY = y;
        switch (direction) {
            case 0:
                newX--;
                message_moved = "la stanga";
                break;
            case 1:
            	message_moved = "la dreapta";
                newX++;
                break;
            case 2:
            	message_moved = "in sus";
                newY--;
                break;
            default:
            	message_moved = "in jos";
                newY++;
                break;
        }
        boolean hen_can_move = Can_be_placed(newX, newY);
        if (hen_can_move) {                                                   // if hen has blocked next position, then will wait
        	Egg egg = new Egg(newX, newY);                                        // Lay an egg at the new position
            System.out.println("O gaina din ferma cu ID = " + this.farm.GetFarm_ID() + " s-a deplasat " + message_moved + 
            		" la pozitia " + egg.GetX() + " " + egg.GetY());
            x = newX;                                                             // and update the hen's position
            y = newY;
            try {
            	farm.lock_egg_lay_sensor.lock();                                  // lock access to the egg_lay_sensor
                farm.SetEgg_lay_sensor(true);                                     // set the sensor
                farm.lock_egg_lay_sensor.unlock();                                // lock access to the egg_lay_sensor
            	notify();                                                         // notify the Report_eggs_to_farm
            	Report_eggs_to_farm(egg);                                         // report the egg to the farm list
                System.out.println("Un ou la pozitia : " + egg.GetX() + " " + egg.GetY() +" a fost raportat la ferma cu ID = " + this.farm.GetFarm_ID());
				Thread.sleep(Company.AFTER_SPAWNING_RESTING_TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}                    // sleep for resting time after spawn the egg
        } else {
        	farm.lock_egg_lay_sensor.lock();                                     // lock access to the egg_lay_sensor
        	farm.SetEgg_lay_sensor(true);                                        // set the sensor
        	farm.lock_egg_lay_sensor.unlock();                                   // lock access to the egg_lay_sensor
        	notify();                                                            // notify the Add_egg_to_queue 
        	int waiting_time = Company.random.nextInt(Company.MAX_CAN_NOT_MOVE_TIME - Company.MIN_CAN_NOT_MOVE_TIME + 1) + Company.MIN_CAN_NOT_MOVE_TIME;
            try {
				Thread.sleep(waiting_time);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    
        }
    }

	
    private boolean Can_be_placed(int temp_x, int temp_y) {                       // return true if a hen can be placed on the position x,y
    	boolean hen_can_be_placed = true;
    	if(temp_x < 0 && temp_y < 0 && temp_x >= this.farm.GetFarm_dim() && temp_y >= this.farm.GetFarm_dim()) {
    		return false;
    	}                              // block the hens moving
    	for (Hen hen : farm.GetHens()) {
    		if (hen.x == temp_x && hen.y == temp_y) {                         // verify it is other hen in the x,y position
    				hen_can_be_placed = false;
    		}
    	}
        return hen_can_be_placed;
    }
    
    
    private synchronized void Report_eggs_to_farm(Egg egg) {
    	while(farm.GetEgg_lay_sensor() == false) {                                // waiting while sensor is false and Hen_moving
    		try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	farm.Add_egg(egg);                                                       // report/add the eggs to Farm list
    	farm.lock_egg_lay_sensor.lock();                                         // lock access to the egg_lay_sensor
        farm.SetEgg_lay_sensor(false);                                           // set the sensor
        farm.lock_egg_lay_sensor.unlock();                                       // lock access to the egg_lay_sensor
    	notify();                                                                // notify the Move_hen
    }
    
}