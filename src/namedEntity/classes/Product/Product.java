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

  public String getComercial() {
    return comercial;
  }

  public String getProductor() {
    return productor;
  }

  public static int getTotalFrequency() {
    return totalFrequency;
  }

  public void setComercial(String comercial) {
    this.comercial = comercial;
  }

  public void setProductor(String productor) {
    this.productor = productor;
  }

  public String StringifyObject() {
    return ("[" + this.getName() + ": (" + this.getCategory() + ", " + getTotalFrequency() + ") ");
  }
}
