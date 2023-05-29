package namedEntity.classes.Place;

public class Address extends Place {

  private String city;

  private static int totalFrequency = 0;

  public Address(String name, int frequency) {
    super(name, frequency);

    this.setCategory("Address");
    this.setParentCategory("Place");
    totalFrequency += frequency;
  }

  public String getCity() {
    return city;
  }

  public static int getTotalFrequency() {
    return totalFrequency;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String StringifyObject() {
    return ("[" + this.getName() + ": (" + this.getCategory() + ", " + getTotalFrequency() + ") ");
  }
}
