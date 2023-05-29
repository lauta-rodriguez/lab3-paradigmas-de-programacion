package topic.Sports;

public class Basket extends Sports {
  private static int totalFrequency = 0;

  public Basket(String name) {
    super(name);
    this.setCategory("Basket");
    this.setParentCategory("Sports");
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
