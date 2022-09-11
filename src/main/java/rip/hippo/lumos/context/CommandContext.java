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

  public void put(String key, Object value) {
    argumentMap.put(key, value);
  }

  public void remove(String key) {
    argumentMap.remove(key);
  }
}
