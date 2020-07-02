package project2;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Customer implements Runnable{
   private static long time = System.currentTimeMillis();
   
   private static final int storeCapacity =  Marcus_Seth_CS340_p2.storeCapacity;
   private static final int numCustomers = Marcus_Seth_CS340_p2.numCustomers;
   public static int done = 0;
   
   private Thread thread;
   private Random rand;
   private String name;
   
   private boolean elderly;
   private int id;
   
   private static Semaphore mutex;
   public static Semaphore groupIn; //group waiting to go in
   
   public Customer(String n, int i) {
      thread = new Thread(this, n);
      id = i;
      rand = new Random();
      name = n;
      if(rand.nextInt() % 4 == 0) {
         elderly = true;
      }else {
         elderly = false;
      }
      mutex = new Semaphore(1, true);
      groupIn = new Semaphore(storeCapacity, true);
   }
   
   @Override
   public void run() {
      gotoSleep(rand.nextInt(10000));
      
      msg("Has arrived to the store and is waiting outside on line");
      
      try {
         groupIn.acquire();
      }catch(InterruptedException ie) {
         System.out.println(ie);
      }
      
      //System.out.println(groupIn.availablePermits()+"\t"+storeCapacity); 
      //Appears that the semaphore is getting a random number of permits.
      
      try {
         Manager.arrived.acquire();
         Manager.arrived.release(); //domino/cascading effect
      }catch(InterruptedException ie) {
         System.out.println(ie);
      }
      
      msg("In the store, begginning to shop");
      
      gotoSleep(rand.nextInt(10000));
      msg("Done shopping, headed to checkout");
      
      if(this.elderly) {
         try {
            Employee.oldRegister.acquire();
            msg("Elderly. paying and on line 1");
            gotoSleep(rand.nextInt(10000));
            msg("Elderly and done paying");
            Employee.oldRegister.release();
         }catch(InterruptedException ie) {
            System.out.println(ie);
         }
      }else {
         try {
            Employee.nonOldRegisters.acquire();
            mutex.acquire();
            int i = 0;
            while(!Employee.registers[++i]);//Finding the empty register
            Employee.registers[i] = false;//Register is not available
            msg("Non-elderly. Paying on line " + (i + 1));
            mutex.release();
            gotoSleep(rand.nextInt(10000));
            msg("Done paying");
            Employee.nonOldRegisters.release();
            Employee.registers[i] = true;//Register is now available
         }catch(InterruptedException ie) {
            System.out.println(ie);
         }
      }
      
      try {
         mutex.acquire();
      }catch(InterruptedException ie) {
         System.out.println(ie);
      }
      done++;
      mutex.release();
   
      if(done == numCustomers) {
         Employee.sendHome.release();
      }
      
      try {
         Employee.allCustomers[this.id].acquire();
      }catch(InterruptedException ie){
         System.out.println(ie);
      }
      
      msg("Leaves parking lot");
      try {
         Thread.sleep(10); //to show the customers leaving in correct order. 
      } catch (InterruptedException ie) {
         System.out.println(ie);
      }
      Employee.sendHome.release();
      
   }
   
   public void start() {
      thread.start();
   }
   
   public void gotoSleep(int time) {
      try {
         Thread.sleep(time);
      } catch (InterruptedException ie) {
         System.out.println(ie);
      }
   }
   
   public void msg(String m) {
      System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
   }
   private String getName() {
      return name;
   }

}
