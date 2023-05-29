package topic.Sports;

public class F1 extends Sports {

  private static int totalFrequency = 0;

  public F1(String name, int frequency) {
    super(name, frequency);

    this.setCategory("F1");
    this.setParentCategory("Sports");
    totalFrequency += frequency;
  }

  public static int getTotalFrequency() {
    return totalFrequency;
  }

  public String StringifyObject() {
    return "(" + this.getCategory() + ", " + getTotalFrequency() + ")]";
  }

}
