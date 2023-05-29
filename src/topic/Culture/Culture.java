package topic.Culture;

public class Culture extends topic.Topic {
  private static int totalFrequency = 0;

  public Culture(String name) {
    super(name);
    this.setCategory("Culture");
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
