package topic.Culture;

public class Music extends Culture {
  private static int frequency = 0;

  public Music(String name) {
    super(name);
    this.setCategory("Music");
    this.setParentCategory("Culture");
    frequency++;
  }

  public static int getFrequency() {
    return frequency;
  }

  @Override
  public void incrementFrequency() {
    super.incrementFrequency();
    frequency++;
  }

  public String StringifyObject() {
    return "(" + this.getCategory() + ", " + getFrequency() + ")]";
  }
}
