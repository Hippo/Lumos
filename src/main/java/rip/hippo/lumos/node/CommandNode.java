package rip.hippo.lumos.node;

import rip.hippo.lumos.builder.CommandTreeBuilder;
import rip.hippo.lumos.context.CommandContext;
import rip.hippo.lumos.data.BufferedArguments;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Hippo
 */
public interface CommandNode {
  void addChild(CommandNode node);
  List<CommandNode> getChildren();
  <T extends CommandNode> List<T> getChildren(Class<T> parent);

  Optional<SuggestionNode> getSuggestionNode();
  void setSuggestionNode(SuggestionNode suggestionNode);

  static CommandTreeBuilder execute(Consumer<CommandContext> consumer) {
    return new CommandTreeBuilder(consumer);
  }

  static CommandTreeBuilder label(boolean ignoreCase, boolean defaultSuggestion, String label, String... aliases) {
    return new CommandTreeBuilder(ignoreCase, defaultSuggestion, label, aliases);
  }

  static CommandTreeBuilder label(boolean defaultSuggestion, String label, String... aliases) {
    return new CommandTreeBuilder(true, defaultSuggestion, label, aliases);
  }

  static CommandTreeBuilder label(String label, String... aliases) {
    return new CommandTreeBuilder(true, true, label, aliases);
  }

  static CommandTreeBuilder string(String key, boolean endless) {
    return new CommandTreeBuilder(key, args -> {
      if (args.hasNext()) {
        if (endless) {
          StringBuilder builder = new StringBuilder();
          while (args.hasNext()) {
            builder.append(args.next()).append(" ");
          }
          return builder.toString().trim();
        } else {
          return args.next();
        }
      }
      return null;
    });
  }

  static CommandTreeBuilder string(String key) {
    return string(key, false);
  }

  static CommandTreeBuilder bool(String key) {
    return new CommandTreeBuilder(key, args -> {
      if (args.hasNext()) {
        String next = args.next();
        if (next.equalsIgnoreCase("true")) {
          return true;
        } else if (next.equalsIgnoreCase("false")) {
          return false;
        }
      }
      return null;
    });
  }

  static CommandTreeBuilder integer(String key) {
    return new CommandTreeBuilder(key, args -> {
      if (args.hasNext()) {
        try {
          return Integer.parseInt(args.next());
        } catch (NumberFormatException ignored) {
        }
      }
      return null;
    });
  }

  static CommandTreeBuilder floating(String key) {
    return new CommandTreeBuilder(key, args -> {
      if (args.hasNext()) {
        try {
          return Double.parseDouble(args.next());
        } catch (NumberFormatException ignored) {
        }
      }
      return null;
    });
  }

  static CommandTreeBuilder longInteger(String key) {
    return new CommandTreeBuilder(key, args -> {
      if (args.hasNext()) {
        try {
          return Long.parseLong(args.next());
        } catch (NumberFormatException ignored) {
        }
      }
      return null;
    });
  }

  static CommandTreeBuilder parseArgument(String key, Function<BufferedArguments, ?> function) {
    return new CommandTreeBuilder(key, function);
  }
}
