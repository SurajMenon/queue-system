package queue;

public class Producer {
  private static Producer ourInstance = new Producer();
  private Producer() {}
  public static Producer getInstance() {
    return ourInstance;
  }

  public CustomQueue customQueue = CustomQueue.getInstance();

  public void insert(String msg, ConsumerList c) {
    customQueue.insert(msg, c);
  }
}
