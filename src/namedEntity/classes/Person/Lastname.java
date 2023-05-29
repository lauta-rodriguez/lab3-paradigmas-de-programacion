package namedEntity.classes.Person;

public class Lastname extends Person {

  private String canonicalForm;
  private String origin;

  private static int totalFrequency = 0;

  public Lastname(String name, int frequency) {
    super(name, frequency);

    this.setCategory("Lastname");
    this.setParentCategory("Person");
    totalFrequency += frequency;
  }

  public String getCanonicalForm() {
    return canonicalForm;
  }

  public String getOrigin() {
    return origin;
  }

  public static int getTotalFrequency() {
    return totalFrequency;
  }

  public void setCanonicalForm(String canonicalForm) {
    this.canonicalForm = canonicalForm;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public String StringifyObject() {
    return ("[" + this.getName() + ": (" + this.getCategory() + ", " + getTotalFrequency() + ") ");
  }
}
