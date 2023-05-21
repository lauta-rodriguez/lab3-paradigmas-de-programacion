package namedEntity.classes.Place;

public class Country extends Place {

  private int population;
  private String oficialLanguage;

  private static int frequency = 0;

  public Country(String name) {
    super(name);
    this.setCategory("Country");
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

  public String getOficialLanguage() {
    return oficialLanguage;
  }

  public void setOficialLanguage(String oficialLanguage) {
    this.oficialLanguage = oficialLanguage;
  }

  public String StringifyObject() {
		return ("[" + this.getName() + ": (" + this.getCategory() + ", " + getFrequency() + ") ");
	}
}
