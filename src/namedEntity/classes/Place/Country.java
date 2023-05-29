package namedEntity.classes.Place;

public class Country extends Place {

  private int population;
  private String oficialLanguage;

  private static int totalFrequency = 0;

  public Country(String name, int frequency) {
    super(name, frequency);

    this.setCategory("Country");
    this.setParentCategory("Place");
    totalFrequency += frequency;
  }

  public int getPopulation() {
    return population;
  }

  public String getOficialLanguage() {
    return oficialLanguage;
  }

  public static int getTotalFrequency() {
    return totalFrequency;
  }

  public void setPopulation(int population) {
    this.population = population;
  }

  public void setOficialLanguage(String oficialLanguage) {
    this.oficialLanguage = oficialLanguage;
  }

  public String StringifyObject() {
    return ("[" + this.getName() + ": (" + this.getCategory() + ", " + getTotalFrequency() + ") ");
  }
}
