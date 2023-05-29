package topic.Culture;

public class Culture extends topic.Topic {
  private static int totalFrequency = 0;

  public Culture(String name, int frequency) {
    super(name, frequency);
    this.setCategory("Culture");
    totalFrequency += frequency;
  }

  public static int getTotalFrequency() {
    return totalFrequency;
  }

  public String StringifyObject() {
    return "(" + this.getCategory() + ", " + getTotalFrequency() + ")]";
  }

}
