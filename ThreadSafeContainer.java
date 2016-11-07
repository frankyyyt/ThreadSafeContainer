import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadSafeContainer {  
	
	
//	1. Design and develop a thread-safe container class called ThreadSafeContainer.
//	a. The class shall conform to the following restrictions:
//	         i.  The data will be removed in a first in first out order.
//	         ii. The class with be a template/generic container class
//	         iii.The class will be initialized with a capacity at construction time.
//	         iv. The _add_ method will add one element to the FIFO and, if needed, block
//	                  the caller until space is available.
//	         v.  The _remove_ method will block the caller until an element is available for
//	              retrieval.
//	         vi. The _clear_ method will remove any objects which were added, but not yet
//	              removed.
//	         vii.The _shutdown_ method will cause any blocked or future calls to add or
//	              remove to throw a ShutdownException .
//	         viii.Please do not use any existing BlockingQueue classes. We prefer that
//	               you generate your own versions to see how you handle blocking and
//	               synchronization.
//	b. Explain how your design is thread-safe? Is it safe for n reader threads and m
//	              writer threads?
//	  1. Using synchronized keyword makes sure only one thread can access the function or block;
//	     By setup lock to make sure when the shared queue is been occupied,no other thread can access the queue.
//	     Yes, it's safte for n read threads and mwriter threads.
//	c. [Optional] Test your code using a unit testing framework. You could using
//	              something like JUnit for Java or Google Test for C++.
//	d. [Optional] Using some UML diagrams of your choosing, please illustrate your
//	              design.
	
	
	
		
	static Boolean addflag=false;
	static Boolean removeflag=false;
	
	public static Queue<Integer> queue=new LinkedList<Integer>(); 
	public static Integer capacity=0;
	public ThreadSafeContainer(int capacity){
		this.capacity=capacity;
		//https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ConcurrentLinkedQueue.html
	}
	
	public static synchronized void add( int values) throws InterruptedException {  
		//http://docs.oracle.com/javase/6/docs/api/java/lang/Thread.html#interrupted()
		//http://docs.oracle.com/javase/tutorial/essential/concurrency/interrupt.html
		String name =Thread.currentThread().getName();
		while(removeflag){
			System.out.println("add, Thread name: "+name+"; waiting... ");
			try {
				Thread.currentThread().wait();
			} catch (InterruptedException ShutdownException) {
				ShutdownException.printStackTrace();
			}
		}
		addflag=true;
		int i=1;
		
		while(queue.size()<capacity && i<values){
			System.out.println("Thread name: "+name +"; Add test: " + i);
			queue.add(i++);
		}	
		addflag=false;
//		Thread.currentThread().notifyAll();
		return ;
	}

	public static synchronized int remove( ) throws InterruptedException {
		String name =Thread.currentThread().getName();
		while(addflag ){
			System.out.println(" Waiting for removing; Thread name: "+name);
			try {
				Thread.currentThread().wait();
			} catch (InterruptedException ShutdownException) {
				ShutdownException.printStackTrace();
			}
		}
		removeflag=true;		
		Integer x=-1;  // if queue is empty return -1;
		if(queue.size()>0){		
			x=queue.poll();
			System.out.println(" Thread name: "+name+"; Remove test: "+ x+ ". Queue size is: "+queue.size());
			
		}else{
			System.out.println("Thread name: "+name+"; Remove test: queue is empty.");
		}
		removeflag=false;
		return x;
	}
	
	public static void clear(){
		
		while(addflag || removeflag ){
			try {
				System.out.println("clear waiting... ");
				Thread.currentThread().wait();
//				Thread.sleep(100);
		    } catch (InterruptedException ShutdownException) {
			    ShutdownException.printStackTrace();
		    }
    	}
		String name =Thread.currentThread().getName();
		System.out.println("Thread name: "+name+"; Clear test; "+"queue size is: " +queue.size());
		queue.clear();
		return ;
	}
	
	public static void shutdown() {
		
		String name =Thread.currentThread().getName();
		System.out.println("Thread name: "+name+"; Shutdown test;");
		try {
			if(addflag || removeflag) throw new InterruptedException();
			System.exit(0);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return ;
	}
	
	public static void main(String[] args) throws InterruptedException {
	// TODO Auto-generated method stub
		ThreadSafeContainer threadsafe=new ThreadSafeContainer(100);
	    Runnable addrun=new addTest();
	    Thread add=new Thread(addrun);
	    
	    Runnable removerun=new removeTest();
	    Thread remove=new Thread(removerun);
	
	    Runnable addrun2=new addTest();
	    Thread add2=new Thread(addrun2);
	
	    Runnable clearrun=new clearTest();
	    Thread clear=new Thread(clearrun);
	
	    Runnable shutdownrun=new shutdownTest();
	    Thread shutdown=new Thread(shutdownrun);
	
	    add.start();
	    remove.start();
	    add2.start();
	    clear.start();
	    shutdown.start();
	    // wait for threads to end
//        try{
//        	  add.join();
//            remove.join();
//            add2.start();
//            clear.join();
//        }catch( Exception e) {
//            System.out.println("Interrupted");
//        }	  	  
	
	}	
}



class addTest implements Runnable{
	
	@Override
	public void run(){
		int values=1000;	
		try {
			ThreadSafeContainer.add(values);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}	
	
}


class removeTest implements Runnable{
	@Override
	public void run(){
		try {
			String name =Thread.currentThread().getName();
			int remove=ThreadSafeContainer.remove();
			System.out.println("Thread name: "+name+"; Remove test: "+ remove);
				
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
		
	}	
}


class shutdownTest implements Runnable {
	
	@Override
	public void run(){	
		ThreadSafeContainer.shutdown();		
	}

}

class clearTest implements Runnable{
	@Override
	public void run(){	
		ThreadSafeContainer.clear();	
	}

}

