package queue;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import com.fasterxml.jackson.databind.ObjectMapper;


public class CustomQueue {
  private static CustomQueue ourInstance = new CustomQueue();

  private CustomQueue() {}

  public static CustomQueue getInstance() {
    return ourInstance;
  }

  public LinkedList<Message> failedQueue = new LinkedList<Message>();
  public LinkedList<Message> queue = new LinkedList<Message>();
  HashMap<String, Message> lastSentMsg = new HashMap<String, Message>();
  ConsumerList c = ConsumerList.getInstance();

  public void insert(String msg, ConsumerList c) {
    // Check if Queue is full
    if (queue.size() >= Config.QUEUE_SIZE) {
      System.out.println("Queue is already full");
      return;
    }

    // Check if Message is Json
    if (!isJSONValid(msg)) {
      System.out.println(msg + ": not a json");
      return;
    }

    // Lock & add
    synchronized (queue) {
      System.out.println("Adding to Queue:" + msg);
      queue.add(new Message(msg, c));
    }

  }

  public String read(Consumer c) {

    Message msg = queue.peek();

    // Check is consumer has already consumed the message
    // A consumer can only consume next msg if all other consumers have consumed prev msg
    if (!msg.consumerList.contains(c)) {
      System.out.println("Message already consumed by consumer");
      return null;
    }

    // Consumers can only consume msg based on priority
    // Priority is decided based on the order of adding consumers
    if (!msg.consumerList.get(0).equals(c)) {
      System.out.println("Order of consumers violated");
      return null;
    }

    // Add message as last sent msg for client
    lastSentMsg.put(c.getName(), msg);

    // If pattern matches, return string else return null
    String pattern = c.getPattern();
    if (msg.data.contains(pattern)) {
      return msg.data;
    }

    return null;
  }

  public void ack(Consumer consumer) {
    // Get last sent msg for consumer
    Message msg = lastSentMsg.get(consumer.getName());

    // If all consumers have recieved msg, remove it from Queue
    if (msg != null) {
      msg.consumerList.remove(consumer);
      if (msg.consumerList.size() == 0) {
        queue.remove();
      }
    }
  }

  public void fail(Consumer consumer) {

    // Get last sent msg for consumer
    Message msg = lastSentMsg.get(consumer.getName());

    // Get/Set number of retires done
    if (msg.failureCount.get(consumer) == null) {
      msg.failureCount.put(consumer, 1);

    } else if (msg.failureCount.get(consumer) >= Config.retry) {
      // Fail request if max retries reached, add to failed queue
      System.out.println("Max retry limit reached");

      // Remove current consumer from list
      msg.consumerList.remove(consumer);
      if (msg.consumerList.size() == 0) {
        queue.remove(msg);
        failedQueue.add(msg);
      }
      return;

    } else {
      msg.failureCount.put(consumer, msg.failureCount.get(consumer) + 1);
    }

    // Add back msg to queue in case of rerty
    System.out.println("Retrying ...");
  }


  // Util to validate JSON payload
  public static boolean isJSONValid(String jsonInString) {
    try {
      final ObjectMapper mapper = new ObjectMapper();
      mapper.readTree(jsonInString);
      return true;
    } catch (IOException e) {
      return false;
    }
  }
}
