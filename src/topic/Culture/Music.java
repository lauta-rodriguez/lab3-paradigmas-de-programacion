package topic.Culture;

public class Music extends Culture {

  private static int totalFrequency = 0;

  public Music(String name, int frequency) {
    super(name, frequency);

    this.setCategory("Music");
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
