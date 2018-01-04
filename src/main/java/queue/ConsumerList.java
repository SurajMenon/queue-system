package queue;

import java.util.ArrayList;
import java.util.List;

public class ConsumerList {
  
  private static ConsumerList ourInstance = new ConsumerList();
  private ConsumerList() {}
  public static ConsumerList getInstance() {
    return ourInstance;
  }

  private List<Consumer> consumerList = new ArrayList<Consumer>();

  // Register a consumer
  public void register(Consumer c) {
    consumerList.add(c);
  }

  public List<Consumer> getConsumers() {
    return consumerList;
  }
}
