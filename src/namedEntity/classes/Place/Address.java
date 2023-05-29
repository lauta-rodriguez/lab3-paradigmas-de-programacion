package namedEntity.classes.Place;

public class Address extends Place {

  private String city;

  private static int totalFrequency = 0;

  public Address(String name) {
    super(name);
    this.setCategory("Address");
    this.setParentCategory("Place");
    totalFrequency++;
  }

  public static int getTotalFrequency() {
    return totalFrequency;
  }

  public void incrementFrequency() {
    super.incrementFrequency();
    totalFrequency++;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String StringifyObject() {
    return ("[" + this.getName() + ": (" + this.getCategory() + ", " + getTotalFrequency() + ") ");
  }
}
