package topic.Sports;

public class Tennis extends Sports {
  private static int totalFrequency = 0;

  public Tennis(String name) {
    super(name);
    this.setCategory("Tennis");
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
