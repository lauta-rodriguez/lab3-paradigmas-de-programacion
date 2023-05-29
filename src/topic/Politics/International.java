package topic.Politics;

public class International extends Politics {
  private static int totalFrequency = 0;

  public International(String name, int frequency) {
    super(name, frequency);
    this.setCategory("International");
    this.setParentCategory("Politics");
    totalFrequency += frequency;
  }

  public static int getFrequency() {
    return totalFrequency;
  }

  public String StringifyObject() {
    return "(" + this.getCategory() + ", " + getFrequency() + ")]";
  }
}
