package topic.Sports;

public class Tennis extends Sports {
  private static int frequency = 0;

  public Tennis(String name) {
    super(name);
    this.setCategory("Tennis");
    this.setParentCategory("Sports");
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
