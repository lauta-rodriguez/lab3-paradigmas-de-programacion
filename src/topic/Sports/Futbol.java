package topic.Sports;

public class Futbol extends Sports {
  private static int totalFrequency = 0;

  public Futbol(String name) {
    super(name);
    this.setCategory("Futbol");
    this.setParentCategory("Sports");
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
