package namedEntity.classes.Place;

public class City extends Place {

  private int population;
  private String capital;
  private String country;

  private static int frequency = 0;

  public City(String name) {
    super(name);
    this.setCategory("City");
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
