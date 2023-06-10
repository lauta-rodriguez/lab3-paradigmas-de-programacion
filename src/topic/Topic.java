package topic;

public class Topic {
  private String name = "-";
  private String category = "Other";
  private String parentCategory = "Topic";

  private static int frequency = 0;

  public Topic(String name) {
    this.name = name;
    frequency++;
  }

  public static int getFrequency() {
    return frequency;
  }

  public void incrementFrequency() {
    frequency++;
  }

  public String getName() {
    return this.name;
  }

  public String getCategory() {
    return this.category;
  }

  protected void setCategory(String category) {
    this.category = category;
  }

  public String getParentCategory() {
    return this.parentCategory;
  }

  protected void setParentCategory(String parentCategory) {
    this.parentCategory = parentCategory;
  }

  public String StringifyObject() {
    return "(" + this.getCategory() + ", " + getFrequency() + ")]";
  }

  public static void main(String[] args) {
    Topic topic = new Topic("name");
    System.out.println(topic.StringifyObject());
  }

}
