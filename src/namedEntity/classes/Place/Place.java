package namedEntity.classes.Place;

public class Place extends namedEntity.NamedEntity {

  private String id;

  private static int totalFrequency = 0;

  public Place(String name) {
    super(name);
    this.setCategory("Place");
    totalFrequency++;
  }

  public static int getFrequency() {
    return totalFrequency;
  }

  public void incrementFrequency() {
    super.incrementFrequency();
    totalFrequency++;
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
