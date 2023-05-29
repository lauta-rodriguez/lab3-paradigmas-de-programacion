package namedEntity.classes.Product;

public class Product extends namedEntity.NamedEntity {
  private String comercial;
  private String productor;

  private static int totalFrequency = 0;

  public Product(String name, int frequency) {
    super(name, frequency);
    this.setCategory("Product");
    totalFrequency += frequency;
  }

  public static int getFrequency() {
    return totalFrequency;
  }

  public String getComercial() {
    return comercial;
  }

  public void setComercial(String comercial) {
    this.comercial = comercial;
  }

  public String getProductor() {
    return productor;
  }

  public void setProductor(String productor) {
    this.productor = productor;
  }

  public String StringifyObject() {
    return ("[" + this.getName() + ": (" + this.getCategory() + ", " + getFrequency() + ") ");
  }
}
