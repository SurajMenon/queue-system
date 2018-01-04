package app;

import queue.Consumer;
import queue.ConsumerList;
import queue.Producer;

public class AppFail {

  public static void main(String args[]) {

    Producer P = Producer.getInstance();
    Consumer A = new Consumer("A", "test");
    Consumer B = new Consumer("B", "test2");

    ConsumerList l = ConsumerList.getInstance();
    l.register(A);l.register(B);

    String json1 = "{\"test\":\"json\"}";
    String json2 = "{\"test2\":\"json\"}";
    P.insert(json1, l);
    P.insert(json2, l);

    System.out.println("\nInitial Queue Size:" + P.customQueue.queue.size());
    A.fail();//Should retry
    A.fail();//Should retry
    A.fail();//Should fail
    
    //Main Queue & failed queues should not be modified
    System.out.println("Failed Queue Size:" + P.customQueue.failedQueue.size());
    System.out.println("Main Queue Size:" + P.customQueue.queue.size());
    System.out.println();
    
    B.fail();//Should retry
    B.fail();//Should retry
    B.fail();//Should fail
    
    //Main Queue & failed queues should BE modified now
    System.out.println("Failed Queue Size:" + P.customQueue.failedQueue.size());
    System.out.println("Main Queue Size:" + P.customQueue.queue.size());
    
    //TODO : this test case can be modified to run in while loop
  }
}
