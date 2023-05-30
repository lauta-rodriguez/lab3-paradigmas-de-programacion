package parser;

/*Esta clase modela los atributos y metodos comunes a todos los distintos tipos de parser existentes en la aplicacion*/
public abstract class GeneralParser<T> implements java.io.Serializable {

  /* Este metodo debe ser implementado por todas las subclases de parser */
  public abstract T parse(String data);

  public abstract String getParserType();

}
