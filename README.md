# ThreadSafeContainer
Design and develop a thread-safe container class called ThreadSafeContainer.
//	1. The class shall conform to the following:
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

//	  1. Using synchronized keyword makes sure only one thread can access the function or block;
//	     By setup lock to make sure when the shared queue is been occupied,no other thread can access the queue.
//	     It's safte for n read threads and mwriter threads.
