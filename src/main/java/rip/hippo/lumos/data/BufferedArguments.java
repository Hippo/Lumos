package rip.hippo.lumos.data;

import java.util.List;

/**
 * @author Hippo
 */
public final class BufferedArguments {

  private final String[] arguments;
  private int index;
  private int lastIndex;

  private BufferedArguments(String[] arguments) {
    this.arguments = arguments;
    this.index = 0;
    this.lastIndex = 0;
  }

  public void start() {
    lastIndex = index;
  }

  public void reset() {
    index = lastIndex;
  }

  public boolean hasNext() {
    return index < arguments.length;
  }

  public boolean hasPrevious() {
    return index > 0;
  }

  public String peek() {
    return arguments[index];
  }

  public String next() {
    return arguments[index++];
  }

  public String previous() {
    return arguments[--index];
  }

  public String[] getArguments() {
    return arguments;
  }

  public int getIndex() {
    return index;
  }

  public static BufferedArguments of() {
    return new BufferedArguments(new String[0]);
  }

  public static BufferedArguments of(String[] arguments) {
    return new BufferedArguments(arguments);
  }

  public static BufferedArguments of(String arguments) {
    return new BufferedArguments(arguments.split(" "));
  }

  public static BufferedArguments of(List<String> arguments) {
    return new BufferedArguments(arguments.toArray(new String[0]));
  }
}
