package namedEntity.classes.CDate;

public class CDate extends namedEntity.NamedEntity {

  // precisa, forma canónica
  private java.util.Date precise;
  private String canonicalForm;

  private static int frequency = 0;

  public CDate(String name) {
    super(name);
    this.setCategory("CDate");
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

  public java.util.Date getPrecise() {
    return precise;
  }

  public void setPrecise(java.util.Date precise) {
    this.precise = precise;
  }

  public String getCanonicalForm() {
    return canonicalForm;
  }

  public void setCanonicalForm(String canonicalForm) {
    this.canonicalForm = canonicalForm;
  }

  public String StringifyObject() {
    return ("[" + this.getName() + ": (" + this.getCategory() + ", " + getFrequency() + ") ");
  }

}
