package rip.hippo.lumos.node;

import rip.hippo.lumos.node.impl.ArgumentCommandNode;
import rip.hippo.lumos.node.impl.LabelCommandNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

/**
 * @author Hippo
 */
@FunctionalInterface
public interface SuggestionNode {
  List<String> getSuggestions(CommandNode node, String input);


  static SuggestionNode ofLabel() {
    return (node, input) -> {
      if (node instanceof LabelCommandNode) {
        LabelCommandNode labelCommandNode = (LabelCommandNode) node;
        List<String> suggestions = new ArrayList<>(labelCommandNode.getAliases().length + 1);
        boolean ignoreCase = labelCommandNode.isIgnoreCase();
        if ((ignoreCase && labelCommandNode.getLabel().toLowerCase().startsWith(input.toLowerCase()))
            || labelCommandNode.getLabel().startsWith(input)) {
          suggestions.add(labelCommandNode.getLabel());
        }
        for (String alias : labelCommandNode.getAliases()) {
          if ((ignoreCase && alias.toLowerCase().startsWith(input.toLowerCase()))
              || alias.startsWith(input)) {
            suggestions.add(alias);
          }
        }
        return suggestions;
      }
      return Collections.emptyList();
    };
  }

  static SuggestionNode ofLabel(BiFunction<LabelCommandNode, String, List<String>> function) {
    return (node, input) -> {
      if (node instanceof LabelCommandNode) {
        return function.apply((LabelCommandNode) node, input);
      }
      return Collections.emptyList();
    };
  }

  @SuppressWarnings("unchecked")
  static <T> SuggestionNode ofArgument(Class<T> type, BiFunction<ArgumentCommandNode<T>, String, List<String>> function) {
    return (node, input) -> {
      if (node instanceof ArgumentCommandNode) {
        return function.apply((ArgumentCommandNode<T>) node, input);
      }
      return Collections.emptyList();
    };
  }
}
