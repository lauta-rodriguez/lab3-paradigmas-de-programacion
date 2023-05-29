package topic.Politics;

public class National extends Politics {
  private static int totalFrequency = 0;

  public National(String name, int frequency) {
    super(name, frequency);
    this.setCategory("National");
    this.setParentCategory("Politics");
    totalFrequency += frequency;
  }

  public static int getTotalFrequency() {
    return totalFrequency;
  }

  public String StringifyObject() {
    return "(" + this.getCategory() + ", " + getTotalFrequency() + ")]";
  }
}
