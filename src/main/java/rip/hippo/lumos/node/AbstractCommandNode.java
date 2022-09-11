package rip.hippo.lumos.node;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * @author Hippo
 */
public class AbstractCommandNode implements CommandNode {

  protected final List<CommandNode> children;
  private SuggestionNode suggestionNode;

  public AbstractCommandNode() {
    this.children = new LinkedList<>();
  }

  @Override
  public void addChild(CommandNode node) {
    children.add(node);
  }

  @Override
  public List<CommandNode> getChildren() {
    return children;
  }

  @Override
  public <T extends CommandNode> List<T> getChildren(Class<T> parent) {
    List<T> nodes = new LinkedList<>();
    for (CommandNode commandNode : children) {
      if (parent.isInstance(commandNode)) {
        nodes.add(parent.cast(commandNode));
      }
    }
    return nodes;
  }

  @Override
  public Optional<SuggestionNode> getSuggestionNode() {
    return Optional.ofNullable(suggestionNode);
  }

  @Override
  public void setSuggestionNode(SuggestionNode suggestionNode) {
    this.suggestionNode = suggestionNode;
  }
}
