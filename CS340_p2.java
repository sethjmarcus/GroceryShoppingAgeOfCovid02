package project2;

public class Marcus_Seth_CS340_p2 {

public static long time = System.currentTimeMillis();
   
   public static int numCustomers = 20; 
   public static final int storeCapacity = 6;
   public static final int numRegisters = 3;
   
   public static void main(String[] args) {
      if(args.length > 0) {
         try {
            numCustomers = Integer.parseInt(args[0]);
         }catch(Exception e) {
            System.out.println("Error parsing args[0]. numCustomers = 20");
            e.getStackTrace();
         }
      }
      Customer[] customers = new Customer[numCustomers];
      Employee[] employees = new Employee[numRegisters];
      for(int i = 0; i < numCustomers; i++) {
         customers[i] = new Customer("Customer " + Integer.toString(i), i);
      }
      for(int i = 0; i < numRegisters; i++) {
         employees[i] = new Employee("Employee" + Integer.toString(i));
      }
      Manager manager = new Manager();
      
      //Start Threads
      for(int i = 0; i < numCustomers; i++) {
         customers[i].start();
      }
      manager.start();
      employees[0].start();
      /*
      for(int i = 0; i < numRegisters; i++) {
         employees[i].start();
      }
      */
   }

}
