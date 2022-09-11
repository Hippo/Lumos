package rip.hippo.lumos.impl;

import rip.hippo.lumos.CommandTree;
import rip.hippo.lumos.node.AbstractCommandNode;
import rip.hippo.lumos.node.CommandNode;

import java.util.List;

/**
 * @author Hippo
 */
public final class StandardCommandTree extends AbstractCommandNode implements CommandTree {
  public StandardCommandTree(List<CommandNode> nodeList) {
    this.children.addAll(nodeList);
  }
}
