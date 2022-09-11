package rip.hippo.lumos.node.impl;

import rip.hippo.lumos.data.BufferedArguments;
import rip.hippo.lumos.node.AbstractCommandNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Hippo
 */
public final class LabelCommandNode extends AbstractCommandNode {

  private final boolean ignoreCase;
  private final String label;
  private final String[] aliases;

  public LabelCommandNode(boolean ignoreCase, String label, String[] aliases) {
    this.ignoreCase = ignoreCase;
    this.label = label;
    this.aliases = aliases;
  }

  public boolean canUse(BufferedArguments bufferedArguments) {
    bufferedArguments.start();
    List<String> checks = new ArrayList<>(aliases.length + 1);
    checks.add(label);
    checks.addAll(Arrays.asList(aliases));

    for (String check : checks) {
      String[] split = check.split(" ");
      boolean passes = true;
      for (String segment : split) {
        if (!bufferedArguments.hasNext()) {
          passes = false;
          break;
        }
        String next = bufferedArguments.next();
        if (ignoreCase) {
          passes = next.equalsIgnoreCase(segment);
        } else {
          passes = next.equals(segment);
        }
        if (!passes) {
          break;
        }
      }
      if (passes) {
        return true;
      }
      bufferedArguments.reset();
    }
    bufferedArguments.reset();
    return false;
  }

  public boolean isIgnoreCase() {
    return ignoreCase;
  }

  public String getLabel() {
    return label;
  }

  public String[] getAliases() {
    return aliases;
  }
}
