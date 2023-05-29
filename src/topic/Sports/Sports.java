package topic.Sports;

public class Sports extends topic.Topic {
  private static int totalFrequency = 0;

  public Sports(String name, int frequency) {
    super(name, frequency);
    this.setCategory("Sports");
    totalFrequency += frequency;
  }

  public static int getTotalFrequency() {
    return totalFrequency;
  }

  public String StringifyObject() {
    return "(" + this.getCategory() + ", " + getTotalFrequency() + ")]";
  }
}
