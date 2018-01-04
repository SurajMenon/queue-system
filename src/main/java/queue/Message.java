package queue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Message {

    String data;
    List<Consumer> consumerList = new ArrayList<Consumer>();//List of all consumers who need to read the message
    HashMap<Consumer,Integer> failureCount = new HashMap<Consumer,Integer>();//Failed consumers & their count

    public Message(String msg,ConsumerList toConsume){
        this.data=msg;
        this.consumerList=new ArrayList<Consumer>(toConsume.getConsumers());//adding current consumers
    }

    @Override
    public String toString() {
      return "Message [data=" + data + ", consumerList=" + consumerList + ", failureCount="
          + failureCount + "]";
    }
}
