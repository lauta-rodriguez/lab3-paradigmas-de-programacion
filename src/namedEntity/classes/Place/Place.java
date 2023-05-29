package namedEntity.classes.Place;

public class Place extends namedEntity.NamedEntity {

  private String id;

  private static int totalFrequency = 0;

  public Place(String name, int frequency) {
    super(name, frequency);
    this.setCategory("Place");
    totalFrequency += frequency;
  }

  public static int getFrequency() {
    return totalFrequency;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String StringifyObject() {
    return ("[" + this.getName() + ": (" + this.getCategory() + ", " + getFrequency() + ") ");
  }
}
