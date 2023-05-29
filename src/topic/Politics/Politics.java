package topic.Politics;

public class Politics extends topic.Topic {

  private static int totalFrequency = 0;

  public Politics(String name, int frequency) {
    super(name, frequency);

    this.setCategory("Politics");
    totalFrequency += frequency;
  }

  public static int getTotalFrequency() {
    return totalFrequency;
  }

  public String StringifyObject() {
    return "(" + this.getCategory() + ", " + getTotalFrequency() + ")]";
  }
}
