package namedEntity.classes.CDate;

public class CDate extends namedEntity.NamedEntity {

  // precisa, forma canónica
  private java.util.Date precise;
  private String canonicalForm;

  private static int totalFrequency = 0;

  public CDate(String name, int frequency) {
    super(name, frequency);
    this.setCategory("CDate");
    totalFrequency += frequency;
  }

  public static int getTotalFrequency() {
    return totalFrequency;
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
    return ("[" + this.getName() + ": (" + this.getCategory() + ", " + getTotalFrequency() + ") ");
  }

}
