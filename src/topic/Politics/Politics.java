package topic.Politics;

public class Politics extends topic.Topic {
  private static int frequency = 0;

  public Politics(String name) {
    super(name);
    this.setCategory("Politics");
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
