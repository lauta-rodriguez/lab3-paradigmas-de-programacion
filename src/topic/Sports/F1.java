package topic.Sports;

public class F1 extends Sports {
  private static int totalFrequency = 0;

  public F1(String name) {
    super(name);
    this.setCategory("F1");
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
