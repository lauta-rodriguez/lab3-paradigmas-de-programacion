package topic.Politics;

public class National extends Politics {
  private static int totalFrequency = 0;

  public National(String name) {
    super(name);
    this.setCategory("National");
    this.setParentCategory("Politics");
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
