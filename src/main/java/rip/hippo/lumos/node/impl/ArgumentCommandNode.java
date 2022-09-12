package rip.hippo.lumos.node.impl;

import rip.hippo.lumos.data.BufferedArguments;
import rip.hippo.lumos.node.AbstractCommandNode;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author Hippo
 */
public final class ArgumentCommandNode<T> extends AbstractCommandNode {

  private final String key;
  private final Function<BufferedArguments, T> function;

  public ArgumentCommandNode(String key, Function<BufferedArguments, T> function) {
    this.key = key;
    this.function = function;
  }

  public Optional<T> parse(BufferedArguments arguments) {
    return Optional.ofNullable(function.apply(arguments));
  }

  public String getKey() {
    return key;
  }

  @Override
  public String toString() {
    return "ArgumentCommandNode{" +
        "key='" + key + '\'' +
        '}';
  }
}
