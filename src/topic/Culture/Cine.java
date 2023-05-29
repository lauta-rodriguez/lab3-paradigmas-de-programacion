package topic.Culture;

public class Cine extends Culture {
  private static int totalFrequency = 0;

  public Cine(String name) {
    super(name);
    this.setCategory("Cine");
    this.setParentCategory("Culture");
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
