package rip.hippo.lumos.testing;

import org.junit.jupiter.api.Test;
import rip.hippo.lumos.CommandDispatcher;
import rip.hippo.lumos.CommandTree;
import rip.hippo.lumos.broker.CommandBroker;
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
        CommandNode.label("label", "my alias").accept(builder -> {
          builder.suggest(SuggestionNode.ofLabel());

          builder.label(true, "sublabel").accept(subBuilder -> { // You can just pass true to provide the default suggestion for labels (SuggestionNode.ofLabel())
            subBuilder.bool("bool_value")
                .accept(argBuilder -> argBuilder
                    .execute(context -> System.out.println("Sub label context: " + context.parseBoolean("bool_value"))));
          });

          builder.string("string_value").accept(subBuilder -> {
            subBuilder.suggest(SuggestionNode.ofArgument(String.class, (broker, node, input) -> Collections.singletonList("<value>")));

            subBuilder.execute(context -> System.out.println("Label context: " + context.parseString("string_value")));
          });
        }),

        CommandNode.label("other label").accept(builder -> {
          builder.parseArgument("uuid", bufferedArguments -> {
            if (bufferedArguments.hasNext()) {
              return UUID.fromString(bufferedArguments.next());
            }
            return null;
          }).then(CommandNode.execute(context -> System.out.println("UUID: " + context.get("uuid"))));
        }),

        CommandNode.label("exec label")
            .then(CommandNode.execute(context -> System.out.println("Label execution"))),

        CommandNode.label("val").accept(builder -> {
          builder.integer("int_value").accept(intBuilder -> {
            intBuilder.label("post").then(CommandNode.execute(context -> System.out.println("Int value: " + context.get("int_value"))));
          });
        }),

        CommandNode.label("broker").then(CommandNode.execute(commandContext -> {
          commandContext.getCommandBroker().accept(MyBroker.class, broker -> {
            System.out.println("Name: " + broker.getName());
            System.out.println("Age: " + broker.getAge());
            System.out.println("Is cool: " + broker.isCool());
          });
        }))
    ));


    commandDispatcher.dispatch("my command", BufferedArguments.of("label sublabel true"));
    commandDispatcher.dispatch("my command", BufferedArguments.of("my alias sublabel true"));
    commandDispatcher.dispatch("my command", BufferedArguments.of("label cool_value"));
    commandDispatcher.dispatch("my command", BufferedArguments.of("my alias cool_value"));
    commandDispatcher.dispatch("my command", BufferedArguments.of("other label f4a3dde9-5c00-409d-a71d-7f23507de0f2"));
    commandDispatcher.dispatch("my command", BufferedArguments.of("exec label"));
    commandDispatcher.dispatch("my command", BufferedArguments.of("val 5 post"));
    commandDispatcher.dispatch(CommandBroker.of(new MyBroker("Hippo", 19, true)), "my command", BufferedArguments.of("broker"));
    commandDispatcher.dispatch("my command", BufferedArguments.of());


    System.out.println("\n-- Suggestions -- \n");

    System.out.println(commandDispatcher.getSuggestions("my command", BufferedArguments.of("labe")));
    System.out.println(commandDispatcher.getSuggestions("my command", BufferedArguments.of("label sub")));
  }

  private static final class MyBroker {
    private final String name;
    private final int age;
    private final boolean isCool;

    public MyBroker(String name, int age, boolean isCool) {
      this.name = name;
      this.age = age;
      this.isCool = isCool;
    }

    public String getName() {
      return name;
    }

    public int getAge() {
      return age;
    }

    public boolean isCool() {
      return isCool;
    }
  }
}
