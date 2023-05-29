package topic.Sports;

public class Basket extends Sports {

  private static int totalFrequency = 0;

  public Basket(String name, int frequency) {
    super(name, frequency);

    this.setCategory("Basket");
    this.setParentCategory("Sports");
    totalFrequency += frequency;
  }

  public static int getTotalFrequency() {
    return totalFrequency;
  }

  public String StringifyObject() {
    return "(" + this.getCategory() + ", " + getTotalFrequency() + ")]";
  }
}
