package topic.Sports;

public class F1 extends Sports {
  private static int frequency = 0;

  public F1(String name) {
    super(name);
    this.setCategory("F1");
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
