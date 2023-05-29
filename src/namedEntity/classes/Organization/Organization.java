package namedEntity.classes.Organization;

public class Organization extends namedEntity.NamedEntity {

  private String canonicalForm;
  private int members;
  private String type;

  private static int totalFrequency = 0;

  public Organization(String name, int frequency) {
    super(name, frequency);

    this.setCategory("Organization");
    totalFrequency += frequency;
  }

  public String getCanonicalForm() {
    return canonicalForm;
  }

  public int getMembers() {
    return members;
  }

  public String getType() {
    return type;
  }

  public static int getTotalFrequency() {
    return totalFrequency;
  }

  public void setCanonicalForm(String canonicalForm) {
    this.canonicalForm = canonicalForm;
  }

  public void setMembers(int members) {
    this.members = members;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String StringifyObject() {
    return ("[" + this.getName() + ": (" + this.getCategory() + ", " + getTotalFrequency() + ") ");
  }
}
