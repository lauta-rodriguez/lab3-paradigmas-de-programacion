package topic.Sports;

public class Futbol extends Sports {
  private static int totalFrequency = 0;

  public Futbol(String name, int frequency) {
    super(name, frequency);
    this.setCategory("Futbol");
    this.setParentCategory("Sports");
  }

  public static int getTotalFrequency() {
    return totalFrequency;
  }

  public String StringifyObject() {
    return "(" + this.getCategory() + ", " + getTotalFrequency() + ")]";
  }
}
