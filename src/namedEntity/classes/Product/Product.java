package namedEntity.classes.Product;

public class Product extends namedEntity.NamedEntity {
  private String comercial;
  private String productor;

  private static int frequency = 0;

  public Product(String name) {
    super(name);
    this.setCategory("Product");
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
		return ("[(" + getName() + ", " + getNEFrequency() + "): (" + getCategory() + ", " + getFrequency() + ") ");
	}
}
