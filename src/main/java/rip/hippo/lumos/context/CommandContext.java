package rip.hippo.lumos.context;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hippo
 */
public final class CommandContext {

  private final Map<String, Object> argumentMap = new HashMap<>();

  public boolean has(String key) {
    return argumentMap.containsKey(key);
  }

  public boolean is(String key, Class<?> parent) {
    return has(key) && parent.isInstance(argumentMap.get(key));
  }

  public Object get(String key) {
    return argumentMap.get(key);
  }

  public <T> T get(String key, Class<T> parent) {
    return parent.cast(argumentMap.get(key));
  }

  public int parseInt(String key) {
    return get(key, Integer.class);
  }

  public long parseLong(String key) {
    return get(key, Long.class);
  }

  public double parseDouble(String key) {
    return get(key, Double.class);
  }

  public float parseFloat(String key) {
    return get(key, Float.class);
  }

  public boolean parseBoolean(String key) {
    return get(key, Boolean.class);
  }

  public String parseString(String key) {
    return get(key, String.class);
  }

  public char parseChar(String key) {
    return get(key, Character.class);
  }

  public byte parseByte(String key) {
    return get(key, Byte.class);
  }

  public short parseShort(String key) {
    return get(key, Short.class);
  }

  public void put(String key, Object value) {
    argumentMap.put(key, value);
  }

  public void remove(String key) {
    argumentMap.remove(key);
  }
}
