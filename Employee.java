package project2;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class Employee implements Runnable{
   private static long time = System.currentTimeMillis();
   public static boolean[] registers;
   
   public static Semaphore oldRegister;
   public static Semaphore nonOldRegisters;
   public static Semaphore sendHome;
   
   public static Semaphore[] allCustomers;
   
   private static final int numRegisters = Marcus_Seth_CS340_p2.numRegisters;
   private static final int numCustomers = Marcus_Seth_CS340_p2.numCustomers;
   
   
   public Thread thread;
   
   public Employee(String n) {
      thread = new Thread(this, n);
      
      registers = new boolean[numRegisters];
      Arrays.fill(registers, Boolean.TRUE);
      
      nonOldRegisters = new Semaphore(numRegisters - 1, true);
      oldRegister = new Semaphore(1, true);
      sendHome = new Semaphore(0, false);
      allCustomers = new Semaphore[numCustomers];
      for(int i = 0; i < numCustomers; i++) {
         allCustomers[i] = new Semaphore(0, false);
      }
   }
   
   @Override
   public void run() {
      
      try {
         sendHome.acquire();
      }catch(InterruptedException ie) {
         System.out.println(ie);
      }
      
      msg("Starting to Send Customers Home");
      
      for(int i = 0; i < numCustomers; i++) {
         allCustomers[i].release();
         try {
            sendHome.acquire();
         }catch(InterruptedException ie){
            System.out.println(ie);
         }
      }
      
      msg("is going home now");
   }
   
   public void start() {
      thread.start();
   }
   
   public void msg(String m) {
      System.out.println("["+(System.currentTimeMillis()-time)+"] "+Thread.currentThread().getName()+": "+m);
   }

}
