package topic.Culture;

public class Cine extends Culture {
  private static int totalFrequency = 0;

  public Cine(String name, int frequency) {
    super(name, frequency);
    this.setCategory("Cine");
    this.setParentCategory("Culture");
    totalFrequency += frequency;
  }

  public static int getTotalFrequency() {
    return totalFrequency;
  }

  public String StringifyObject() {
    return "(" + this.getCategory() + ", " + getTotalFrequency() + ")]";
  }
}
