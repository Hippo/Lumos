package rip.hippo.lumos;

import rip.hippo.lumos.broker.CommandBroker;
import rip.hippo.lumos.data.BufferedArguments;
import rip.hippo.lumos.impl.StandardCommandDispatcher;

import java.util.List;

/**
 * @author Hippo
 */
public interface CommandDispatcher {
  void register(String command, CommandTree commandTree);
  void unregister(String command);

  boolean dispatch(CommandBroker commandBroker, String command, BufferedArguments bufferedArguments);
  boolean dispatch(String command, BufferedArguments bufferedArguments);
  List<String> getSuggestions(CommandBroker commandBroker, String command, BufferedArguments bufferedArguments);
  List<String> getSuggestions(String command, BufferedArguments bufferedArguments);

  static CommandDispatcher create() {
    return new StandardCommandDispatcher();
  }
}
