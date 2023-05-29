package namedEntity.classes.Person;

public class Name extends Person {

  private String canonicalForm;
  private String origin;
  private String[] variants;

  private static int totalFrequency = 0;

  public Name(String name, int frequency) {
    super(name, frequency);

    this.setCategory("Name");
    this.setParentCategory("Person");
    totalFrequency += frequency;
  }

  public String getCanonicalForm() {
    return canonicalForm;
  }

  public String getOrigin() {
    return origin;
  }

  public String[] getVariants() {
    return variants;
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

  public void setVariants(String[] variants) {
    this.variants = variants;
  }

  public String StringifyObject() {
    return ("[" + this.getName() + ": (" + this.getCategory() + ", " + getTotalFrequency() + ") ");
  }
}
