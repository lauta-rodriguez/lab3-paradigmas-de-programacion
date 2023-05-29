package topic.Sports;

public class Tennis extends Sports {
  private static int totalFrequency = 0;

  public Tennis(String name, int frequency) {
    super(name, frequency);
    this.setCategory("Tennis");
    this.setParentCategory("Sports");
    totalFrequency += frequency;
  }

  public static int getFrequency() {
    return totalFrequency;
  }

  public String StringifyObject() {
    return "(" + this.getCategory() + ", " + getFrequency() + ")]";
  }

}
