package rip.hippo.lumos.impl;

import rip.hippo.lumos.CommandDispatcher;
import rip.hippo.lumos.CommandTree;
import rip.hippo.lumos.context.CommandContext;
import rip.hippo.lumos.data.BufferedArguments;
import rip.hippo.lumos.node.CommandNode;
import rip.hippo.lumos.node.SuggestionNode;
import rip.hippo.lumos.node.impl.ArgumentCommandNode;
import rip.hippo.lumos.node.impl.ExecutionCommandNode;
import rip.hippo.lumos.node.impl.LabelCommandNode;

import java.util.*;

/**
 * @author Hippo
 */
public final class StandardCommandDispatcher implements CommandDispatcher {

  private final Map<String, CommandTree> commandMap = new HashMap<>();

  @Override
  public void register(String command, CommandTree commandTree) {
    commandMap.put(command, commandTree);
  }

  @Override
  public void unregister(String command) {
    commandMap.remove(command);
  }

  @Override
  public boolean dispatch(String command, BufferedArguments bufferedArguments) {
    CommandTree commandTree = commandMap.get(command);
    if (commandTree == null) {
      return false;
    }
    CommandContext commandContext = new CommandContext();
    return handle(commandTree, bufferedArguments, commandContext);
  }

  @Override
  public List<String> getSuggestions(String command, BufferedArguments bufferedArguments) {
    CommandTree commandTree = commandMap.get(command);
    List<String> suggestions = new LinkedList<>();
    if (commandTree == null) {
      return suggestions;
    }
    String[] arguments = bufferedArguments.getArguments();
    if (arguments.length == 0) {
      for (CommandNode child : commandTree.getChildren()) {
        child.getSuggestionNode().ifPresent(node -> suggestions.addAll(node.getSuggestions(child, "")));
      }
    }

    BufferedArguments completeArguments = BufferedArguments.of(Arrays.copyOf(arguments, arguments.length - 1));
    CommandNode commandNode = handleSuggestions(commandTree, completeArguments);
    if (commandNode != null) {
      for (CommandNode child : commandNode.getChildren()) {
        child.getSuggestionNode().ifPresent(node -> suggestions.addAll(node.getSuggestions(child, arguments[arguments.length - 1])));
      }
    }

    return suggestions;
  }

  private CommandNode handleSuggestions(CommandNode commandNode, BufferedArguments bufferedArguments) {
    for (LabelCommandNode labelNode : commandNode.getChildren(LabelCommandNode.class)) {
      if (labelNode.canUse(bufferedArguments)) {
        CommandNode result = handleSuggestions(labelNode, bufferedArguments);
        if (result != null) {
          return result;
        }
      }
    }

    for (ArgumentCommandNode<?> argumentNode : commandNode.getChildren(ArgumentCommandNode.class)) {
      bufferedArguments.start();
      Optional<?> parse = argumentNode.parse(bufferedArguments);
      if (parse.isPresent()) {
        CommandNode result = handleSuggestions(argumentNode, bufferedArguments);
        if (result != null) {
          return result;
        }
      } else {
        bufferedArguments.reset();
      }
    }

    return bufferedArguments.hasNext() ? null : commandNode;
  }

  private boolean handle(CommandNode commandNode, BufferedArguments bufferedArguments, CommandContext commandContext) {
    for (LabelCommandNode labelNode : commandNode.getChildren(LabelCommandNode.class)) {
      if (labelNode.canUse(bufferedArguments)) {
        boolean result = handle(labelNode, bufferedArguments, commandContext);
        if (result) {
          return true;
        }
      }
    }

    for (ArgumentCommandNode<?> argumentNode : commandNode.getChildren(ArgumentCommandNode.class)) {
      bufferedArguments.start();
      Optional<?> parse = argumentNode.parse(bufferedArguments);
      if (parse.isPresent()) {
        Object arg = parse.get();
        commandContext.put(argumentNode.getKey(), arg);
        boolean result = handle(argumentNode, bufferedArguments, commandContext);
        if (result) {
          return true;
        }
        commandContext.remove(argumentNode.getKey());
      } else {
        bufferedArguments.reset();
      }
    }

    boolean result = false;
    for (ExecutionCommandNode executionNode : commandNode.getChildren(ExecutionCommandNode.class)) {
      executionNode.accept(commandContext);
      result = true;
    }

    return result;
  }
}
