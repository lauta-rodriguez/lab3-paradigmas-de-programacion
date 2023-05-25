package namedEntity.classes.Person;

public class Title extends Person {

  private String canonicForm;
  private String professional;

  private static int frequency = 0;

  public Title(String name) {
    super(name);
    this.setCategory("Title");
    this.setParentCategory("Person");
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
    return ("[" + this.getName() + ": (" + this.getCategory() + ", " + getFrequency() + ") ");
  }
}
