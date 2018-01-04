package app;
import queue.Consumer;
import queue.ConsumerList;
import queue.Producer;

public class AppOrder {

    public static void main(String args[]){

        Producer P = Producer.getInstance();

        Consumer A= new Consumer("A","test");
        Consumer B= new Consumer("B","test");

        ConsumerList l = ConsumerList.getInstance();
        l.register(A);l.register(B);

        String json1= "{\"test\":\"json\"}";
        String json2= "{\"name\":\"john\"}";
        String json3= "{\"lname\":\"doe\"}";

        P.insert(json1,l);
        P.insert(json2,l);
        P.insert(json3,l);

        System.out.println("\nQueue Size:"+P.customQueue.queue.size()+"\n");
        B.read();System.out.println();//Should give an error message
        A.read();System.out.println();//Should be able to read
        
        A.read();System.out.println();//Should not be able to read as it has already read the msg
        B.read();//Should be able to read
    }
}
