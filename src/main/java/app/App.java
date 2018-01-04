package app;
import queue.Consumer;
import queue.ConsumerList;
import queue.Producer;

public class App {

    public static void main(String args[]){

        //Creating the Single provider
        Producer P = Producer.getInstance();

        //Creating multiple Consumers & Registering
        Consumer A= new Consumer("A","test");
        Consumer B= new Consumer("B","john");
        Consumer C= new Consumer("C","doe");

        ConsumerList l = ConsumerList.getInstance();
        l.register(A);l.register(B);l.register(C);

        //Adding json msg to Queue
        String json1= "{\"test\":\"json\"}";
        String json2= "{\"name\":\"john\"}";
        String json3= "{\"lname\":\"doe\"}";


        P.insert(json1,l);
        P.insert(json2,l);
        P.insert(json3,l);
        P.insert("blah",l);//Should throw error

        //Getting Queue Size
        System.out.println("\nInitial Queue Size:"+P.customQueue.queue.size());

        //All clients reading from Queue
        while(P.customQueue.queue.size() > 0){
          System.out.println();
          A.read();
          B.read();
          C.read();
        }
    }
}
