package rip.hippo.lumos.builder;

import rip.hippo.lumos.context.CommandContext;
import rip.hippo.lumos.data.BufferedArguments;
import rip.hippo.lumos.node.CommandNode;
import rip.hippo.lumos.node.SuggestionNode;
import rip.hippo.lumos.node.impl.ArgumentCommandNode;
import rip.hippo.lumos.node.impl.ExecutionCommandNode;
import rip.hippo.lumos.node.impl.LabelCommandNode;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Hippo
 */
public final class CommandTreeBuilder {

  private final CommandNode commandNode;

  public CommandTreeBuilder(boolean ignoreCase, String label, String... aliases) {
    this.commandNode = new LabelCommandNode(ignoreCase, label, aliases);
  }

  public CommandTreeBuilder(String key, Function<BufferedArguments, ?> function) {
    this.commandNode = new ArgumentCommandNode<>(key, function);
  }

  public CommandTreeBuilder(Consumer<CommandContext> consumer) {
    this.commandNode = new ExecutionCommandNode(consumer);
  }


  public CommandTreeBuilder then(CommandTreeBuilder commandTreeBuilder) {
    commandNode.addChild(commandTreeBuilder.commandNode);
    return this;
  }

  public CommandTreeBuilder suggest(SuggestionNode suggestionNode) {
    commandNode.setSuggestionNode(suggestionNode);
    return this;
  }

  public CommandNode build() {
    return commandNode;
  }
}
