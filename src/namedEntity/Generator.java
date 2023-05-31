package namedEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import namedEntity.classes.CDate.CDate;
import namedEntity.classes.Event.Event;
import namedEntity.classes.Organization.Organization;
import namedEntity.classes.Person.Lastname;
import namedEntity.classes.Person.Name;
import namedEntity.classes.Person.Title;
import namedEntity.classes.Place.Address;
import namedEntity.classes.Place.City;
import namedEntity.classes.Place.Country;
import namedEntity.classes.Place.Place;
import namedEntity.classes.Product.Product;

public class Generator {
  private static final Map<String, Class<? extends NamedEntity>> CATEGORY_CLASS_MAP = new HashMap<>();

  static {
    CATEGORY_CLASS_MAP.put("Lastname", Lastname.class);
    CATEGORY_CLASS_MAP.put("Name", Name.class);
    CATEGORY_CLASS_MAP.put("Title", Title.class);
    CATEGORY_CLASS_MAP.put("Place", Place.class);
    CATEGORY_CLASS_MAP.put("City", City.class);
    CATEGORY_CLASS_MAP.put("Country", Country.class);
    CATEGORY_CLASS_MAP.put("Address", Address.class);
    CATEGORY_CLASS_MAP.put("Organization", Organization.class);
    CATEGORY_CLASS_MAP.put("Product", Product.class);
    CATEGORY_CLASS_MAP.put("Event", Event.class);
    CATEGORY_CLASS_MAP.put("CDate", CDate.class);
  }

  public static Class<? extends NamedEntity> getNamedEntity(String category)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
      NoSuchMethodException, SecurityException, ClassNotFoundException {

    return CATEGORY_CLASS_MAP.getOrDefault(category, NamedEntity.class);
  }
}
