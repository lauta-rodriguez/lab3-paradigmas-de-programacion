package topic.Sports;

public class Sports extends topic.Topic {
  private static int totalFrequency = 0;

  public Sports(String name) {
    super(name);
    this.setCategory("Sports");
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
