package namedEntity.classes.Place;

public class Address extends Place {

  private String city;

  private static int frequency = 0;

  public Address(String name) {
    super(name);
    this.setCategory("Address");
    this.setParentCategory("Place");
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

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String StringifyObject() {
    return ("[" + this.getName() + ": (" + this.getCategory() + ", " + getFrequency() + ") ");
  }
}
