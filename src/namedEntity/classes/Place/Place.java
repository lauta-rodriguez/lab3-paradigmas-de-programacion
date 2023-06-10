package namedEntity.classes.Place;

public class Place extends namedEntity.NamedEntity {

  private String id;

  private static int frequency = 0;

  public Place(String name) {
    super(name);
    this.setCategory("Place");
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
