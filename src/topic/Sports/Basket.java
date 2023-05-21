package topic.Sports;

public class Basket extends Sports {
  private static int frequency = 0;

  public Basket(String name) {
    super(name);
    this.setCategory("Basket");
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
