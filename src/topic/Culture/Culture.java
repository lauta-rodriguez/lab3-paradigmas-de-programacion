package topic.Culture;

public class Culture extends topic.Topic {
  private static int frequency = 0;

  public Culture(String name) {
    super(name);
    this.setCategory("Culture");
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
