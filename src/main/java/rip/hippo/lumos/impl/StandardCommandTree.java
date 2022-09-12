package rip.hippo.lumos.impl;

import rip.hippo.lumos.CommandTree;
import rip.hippo.lumos.node.AbstractCommandNode;
import rip.hippo.lumos.node.CommandNode;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Hippo
 */
public final class StandardCommandTree extends AbstractCommandNode implements CommandTree {
  public StandardCommandTree(List<CommandNode> nodeList) {
    this.children.addAll(nodeList);
  }

  @Override
  public List<CommandNode> getChildrenDeep() {
    List<CommandNode> nodes = new LinkedList<>();
    addAllChildren(nodes, this);
    return nodes;
  }

  private void addAllChildren(List<CommandNode> nodes, CommandNode node) {
    for (CommandNode child : node.getChildren()) {
      nodes.add(child);
      addAllChildren(nodes, child);
    }
  }

  @Override
  public String toString() {
    return "StandardCommandTree{" +
        "children=" + children +
        '}';
  }
}
