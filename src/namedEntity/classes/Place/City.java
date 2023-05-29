package namedEntity.classes.Place;

public class City extends Place {

  private int population;
  private String capital;
  private String country;

  private static int totalFrequency = 0;

  public City(String name, int frequency) {
    super(name, frequency);

    this.setCategory("City");
    this.setParentCategory("Place");
    totalFrequency += frequency;
  }

  public int getPopulation() {
    return population;
  }

  public String getCapital() {
    return capital;
  }

  public String getCountry() {
    return country;
  }

  public static int getTotalFrequency() {
    return totalFrequency;
  }

  public void setPopulation(int population) {
    this.population = population;
  }

  public void setCapital(String capital) {
    this.capital = capital;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String StringifyObject() {
    return ("[" + this.getName() + ": (" + this.getCategory() + ", " + getTotalFrequency() + ") ");
  }
}
