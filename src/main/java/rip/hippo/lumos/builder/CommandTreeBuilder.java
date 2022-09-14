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

  public CommandTreeBuilder(boolean ignoreCase, boolean defaultSuggestion, String label, String... aliases) {
    this.commandNode = new LabelCommandNode(ignoreCase, label, aliases);
    if (defaultSuggestion) {
      this.commandNode.setSuggestionNode(SuggestionNode.ofLabel());
    }
  }

  public CommandTreeBuilder(String key, Function<BufferedArguments, ?> function) {
    this.commandNode = new ArgumentCommandNode<>(key, function);
  }

  public CommandTreeBuilder(Consumer<CommandContext> consumer) {
    this.commandNode = new ExecutionCommandNode(consumer);
  }

  public CommandTreeBuilder accept(Consumer<CommandTreeBuilder> consumer) {
    consumer.accept(this);
    return this;
  }

  public CommandTreeBuilder then(CommandTreeBuilder commandTreeBuilder) {
    commandNode.addChild(commandTreeBuilder.commandNode);
    return this;
  }

  public CommandTreeBuilder suggest(SuggestionNode suggestionNode) {
    commandNode.setSuggestionNode(suggestionNode);
    return this;
  }

  public CommandTreeBuilder execute(Consumer<CommandContext> consumer) {
    CommandTreeBuilder builder = CommandNode.execute(consumer);
    then(builder);
    return builder;
  }

  public CommandTreeBuilder label(boolean ignoreCase, boolean defaultSuggestion, String label, String... aliases) {
    CommandTreeBuilder builder = CommandNode.label(ignoreCase, defaultSuggestion, label, aliases);
    then(builder);
    return builder;
  }

  public CommandTreeBuilder label(boolean defaultSuggestion, String label, String... aliases) {
    CommandTreeBuilder builder = CommandNode.label(defaultSuggestion, label, aliases);
    then(builder);
    return builder;
  }

  public CommandTreeBuilder label(String label, String... aliases) {
    CommandTreeBuilder builder = CommandNode.label(label, aliases);
    then(builder);
    return builder;
  }

  public CommandTreeBuilder string(String key, boolean endless) {
    CommandTreeBuilder builder = CommandNode.string(key, endless);
    then(builder);
    return builder;
  }

  public CommandTreeBuilder string(String key) {
    CommandTreeBuilder builder = CommandNode.string(key);
    then(builder);
    return builder;
  }

  public CommandTreeBuilder bool(String key) {
    CommandTreeBuilder builder = CommandNode.bool(key);
    then(builder);
    return builder;
  }

  public CommandTreeBuilder integer(String key) {
    CommandTreeBuilder builder = CommandNode.integer(key);
    then(builder);
    return builder;
  }

  public CommandTreeBuilder floating(String key) {
    CommandTreeBuilder builder = CommandNode.floating(key);
    then(builder);
    return builder;
  }

  public CommandTreeBuilder longInteger(String key) {
    CommandTreeBuilder builder = CommandNode.longInteger(key);
    then(builder);
    return builder;
  }

  public CommandTreeBuilder parseArgument(String key, Function<BufferedArguments, ?> function) {
    CommandTreeBuilder builder = CommandNode.parseArgument(key, function);
    then(builder);
    return builder;
  }

  public CommandNode build() {
    return commandNode;
  }
}
