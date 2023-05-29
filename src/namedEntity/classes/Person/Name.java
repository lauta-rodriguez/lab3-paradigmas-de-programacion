package namedEntity.classes.Person;

public class Name extends Person {

  private String canonicalForm;
  private String origin;
  private String[] variants;

  private static int totalFrequency = 0;

  public Name(String name) {
    super(name);
    this.setCategory("Name");
    this.setParentCategory("Person");
    totalFrequency++;
  }

  public static int getFrequency() {
    return totalFrequency;
  }

  public void incrementFrequency() {
    super.incrementFrequency();
    totalFrequency++;
  }

  public String getCanonicalForm() {
    return canonicalForm;
  }

  public void setCanonicalForm(String canonicalForm) {
    this.canonicalForm = canonicalForm;
  }

  public String getOrigin() {
    return origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public String[] getVariants() {
    return variants;
  }

  public void setVariants(String[] variants) {
    this.variants = variants;
  }

  public String StringifyObject() {
    return ("[" + this.getName() + ": (" + this.getCategory() + ", " + getFrequency() + ") ");
  }
}
