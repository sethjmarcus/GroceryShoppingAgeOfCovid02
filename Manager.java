package project2;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Manager implements Runnable{
   private static long time = System.currentTimeMillis();
   
   private static final int storeCapacity = Marcus_Seth_CS340_p2.storeCapacity;
   private static final int numCustomers = Marcus_Seth_CS340_p2.numCustomers;
   
   public static Semaphore isActive;
   public static Semaphore arrived;
   
   public static Thread thread;
   private Random rand;
   
   public Manager() {
      rand = new Random();
      thread = new Thread(this, "manager");
      isActive = new Semaphore(0, false);
      arrived = new Semaphore(0, false);
   }
   
   public void run() {
      
      try {
         Thread.sleep(rand.nextInt(10000)); 
      } catch (InterruptedException ie) {
         System.out.println(ie);
      }
      
      arrived.release();
      
      msg("Manager has arrived to the store");
      
      while(Customer.done != numCustomers) {
         if(Customer.done%storeCapacity == 0){
            for(int i = 0; i < storeCapacity; i++) {
               Customer.groupIn.release();
            }
         }
      }
    
      msg("going home");
      
   }
   
   public void start() {
      thread.start();
   }
   
   public void msg(String m) {
      System.out.println("["+(System.currentTimeMillis()-time)+"] "+Thread.currentThread().getName()+": "+m);
   }
}
