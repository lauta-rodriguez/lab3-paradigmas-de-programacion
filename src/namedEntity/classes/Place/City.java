package namedEntity.classes.Place;

public class City extends Place {

  private int population;
  private String capital;
  private String country;

  private static int totalFrequency = 0;

  public City(String name) {
    super(name);
    this.setCategory("City");
    this.setParentCategory("Place");
    totalFrequency++;
  }

  public static int getFrequency() {
    return totalFrequency;
  }

  public void incrementFrequency() {
    super.incrementFrequency();
    totalFrequency++;
  }

  public int getPopulation() {
    return population;
  }

  public void setPopulation(int population) {
    this.population = population;
  }

  public String getCapital() {
    return capital;
  }

  public void setCapital(String capital) {
    this.capital = capital;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String StringifyObject() {
    return ("[" + this.getName() + ": (" + this.getCategory() + ", " + getFrequency() + ") ");
  }
}
