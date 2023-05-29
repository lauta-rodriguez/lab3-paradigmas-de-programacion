package namedEntity.classes.Person;

public class Title extends Person {

  private String canonicForm;
  private String professional;

  private static int totalFrequency = 0;

  public Title(String name, int frequency) {
    super(name, frequency);
    this.setCategory("Title");
    this.setParentCategory("Person");
    totalFrequency += frequency;
  }

  public static int getTotalFrequency() {
    return totalFrequency;
  }

  public String getCanonicForm() {
    return canonicForm;
  }

  public void setCanonicForm(String canonicForm) {
    this.canonicForm = canonicForm;
  }

  public String getProfessional() {
    return professional;
  }

  public void setProfessional(String professional) {
    this.professional = professional;
  }

  public String StringifyObject() {
    return ("[" + this.getName() + ": (" + this.getCategory() + ", " + getTotalFrequency() + ") ");
  }
}
