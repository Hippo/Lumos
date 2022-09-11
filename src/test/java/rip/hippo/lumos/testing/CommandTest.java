package rip.hippo.lumos.testing;

import org.junit.jupiter.api.Test;
import rip.hippo.lumos.CommandDispatcher;
import rip.hippo.lumos.CommandTree;
import rip.hippo.lumos.data.BufferedArguments;
import rip.hippo.lumos.node.CommandNode;
import rip.hippo.lumos.node.SuggestionNode;

import java.util.Collections;
import java.util.UUID;

/**
 * @author Hippo
 */
public final class CommandTest {

  @Test
  public void test() {
    CommandDispatcher commandDispatcher = CommandDispatcher.create();

    commandDispatcher.register("my command", CommandTree.of(
        CommandNode.label("label", "my alias").suggest(SuggestionNode.ofLabel())
            .then(CommandNode.label("sublabel").suggest(SuggestionNode.ofLabel())
                    .then(CommandNode.bool("bool_value")
                        .then(CommandNode.execute(context -> System.out.println("Sub label context: " + context.get("bool_value")))))
            ).then(CommandNode.string("string_value").suggest(SuggestionNode.ofArgument(String.class, (node, input) -> Collections.singletonList("<value>")))
                .then(CommandNode.execute(context -> System.out.println("Label context: " + context.get("string_value"))))),


        CommandNode.label("other label")
            .then(CommandNode.parseArgument("uuid", bufferedArguments -> {
              if (bufferedArguments.hasNext()) {
                return UUID.fromString(bufferedArguments.next());
              }
              return null;
            }).then(CommandNode.execute(context -> System.out.println("UUID: " + context.get("uuid"))))),


        CommandNode.label("exec label")
                .then(CommandNode.execute(context -> System.out.println("Label execution"))),


        CommandNode.label("val")
            .then(CommandNode.integer("int_value")
                .then(CommandNode.label("post")
                    .then(CommandNode.execute(context -> System.out.println("Int value: " + context.get("int_value")))))),
        CommandNode.execute(context -> System.out.println("Fallback execution"))
    ));


    commandDispatcher.dispatch("my command", BufferedArguments.of("label sublabel true"));
    commandDispatcher.dispatch("my command", BufferedArguments.of("my alias sublabel true"));
    commandDispatcher.dispatch("my command", BufferedArguments.of("label cool_value"));
    commandDispatcher.dispatch("my command", BufferedArguments.of("my alias cool_value"));
    commandDispatcher.dispatch("my command", BufferedArguments.of("other label f4a3dde9-5c00-409d-a71d-7f23507de0f2"));
    commandDispatcher.dispatch("my command", BufferedArguments.of("exec label"));
    commandDispatcher.dispatch("my command", BufferedArguments.of("val 5 post"));
    commandDispatcher.dispatch("my command", BufferedArguments.of());


    System.out.println("\n-- Suggestions -- \n");

    System.out.println(commandDispatcher.getSuggestions("my command", BufferedArguments.of("labe")));
    System.out.println(commandDispatcher.getSuggestions("my command", BufferedArguments.of("label sub")));
  }
}
