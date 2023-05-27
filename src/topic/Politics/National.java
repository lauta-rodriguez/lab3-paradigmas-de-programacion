package topic.Politics;

public class National extends Politics {
  private static int frequency = 0;

  public National(String name) {
    super(name);
    this.setCategory("National");
    this.setParentCategory("Politics");
    frequency++;
  }

  public static int getFrequency() {
    return frequency;
  }

  @Override
  public void incrementFrequency() {
    super.incrementFrequency();
    frequency++;
  }

  public String StringifyObject() {
    return "(" + this.getCategory() + ", " + getFrequency() + ")]";
  }
}
