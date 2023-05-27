package topic.Sports;

public class Sports extends topic.Topic {
  private static int frequency = 0;

  public Sports(String name) {
    super(name);
    this.setCategory("Sports");
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
