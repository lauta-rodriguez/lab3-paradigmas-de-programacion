package topic.Culture;

public class Music extends Culture {
  private static int totalFrequency = 0;

  public Music(String name) {
    super(name);
    this.setCategory("Music");
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
