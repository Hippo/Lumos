package rip.hippo.lumos.node.impl;

import rip.hippo.lumos.context.CommandContext;
import rip.hippo.lumos.node.CommandNode;
import rip.hippo.lumos.node.SuggestionNode;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author Hippo
 */
public final class ExecutionCommandNode implements CommandNode {

  private final Consumer<CommandContext> consumer;

  public ExecutionCommandNode(Consumer<CommandContext> consumer) {
    this.consumer = consumer;
  }

  public void accept(CommandContext commandContext) {
    consumer.accept(commandContext);
  }

  @Override
  public void addChild(CommandNode node) {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<CommandNode> getChildren() {
    return Collections.emptyList();
  }

  @Override
  public <T extends CommandNode> List<T> getChildren(Class<T> parent) {
    return Collections.emptyList();
  }

  @Override
  public Optional<SuggestionNode> getSuggestionNode() {
    return Optional.empty();
  }

  @Override
  public void setSuggestionNode(SuggestionNode suggestionNode) {
    throw new UnsupportedOperationException();
  }
}
