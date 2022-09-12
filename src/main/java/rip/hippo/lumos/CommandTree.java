package rip.hippo.lumos;

import rip.hippo.lumos.builder.CommandTreeBuilder;
import rip.hippo.lumos.impl.StandardCommandTree;
import rip.hippo.lumos.node.CommandNode;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Hippo
 */
public interface CommandTree extends CommandNode {

  List<CommandNode> getChildrenDeep();

  static CommandTree of(CommandTreeBuilder... builders) {
    List<CommandNode> nodes = new LinkedList<>();
    for (CommandTreeBuilder builder : builders) {
      nodes.add(builder.build());
    }
    return new StandardCommandTree(nodes);
  }
}
