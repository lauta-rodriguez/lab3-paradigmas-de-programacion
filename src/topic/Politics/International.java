package topic.Politics;

public class International extends Politics {
  private static int totalFrequency = 0;

  public International(String name) {
    super(name);
    this.setCategory("International");
    this.setParentCategory("Politics");
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
