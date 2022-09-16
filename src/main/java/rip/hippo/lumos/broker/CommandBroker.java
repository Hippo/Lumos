package rip.hippo.lumos.broker;

import java.util.function.Consumer;

/**
 * @author Hippo
 */
public final class CommandBroker {

  private final Object broker;

  public CommandBroker(Object broker) {
    this.broker = broker;
  }

  public <T> void accept(Class<T> type, Consumer<T> consumer) {
    if (type.isInstance(broker)) {
      consumer.accept(type.cast(broker));
    }
  }

  public Object getBroker() {
    return broker;
  }

  public static CommandBroker of(Object sender) {
    return new CommandBroker(sender);
  }
}
