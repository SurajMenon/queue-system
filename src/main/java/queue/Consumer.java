package queue;
public class Consumer {

    private String name;
    private String pattern;//Send messages only if the pattern matches
    CustomQueue q=CustomQueue.getInstance();

    public Consumer(String name,String pattern){
        this.name=name;
        this.pattern=pattern;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    //Successful read scenario
    public void read(){
        String msg = q.read(this);
        if(msg==null){
          System.out.println("Empty message receieved by consumer "+this.getName());
        }else{
          System.out.println("Message:"+ msg+ " receieved by consumer "+this.getName());
        }
        q.ack(this);
    }
    
    //Failure scenario
    public void fail(){
      q.read(this);
      q.fail(this);
  }
}
