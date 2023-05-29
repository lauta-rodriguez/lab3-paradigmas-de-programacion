package topic.Politics;

public class Politics extends topic.Topic {
  private static int totalFrequency = 0;

  public Politics(String name) {
    super(name);
    this.setCategory("Politics");
    totalFrequency++;
  }

  public static int getFrequency() {
    return totalFrequency;
  }

  public void incrementFrequency() {
    super.incrementFrequency();
    totalFrequency++;
  }

  public String StringifyObject() {
    return "(" + this.getCategory() + ", " + getFrequency() + ")]";
  }
}
