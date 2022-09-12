# Lumos
A functional based command system written in Java.

# Adding Lumos to your project
```kotlin
repositories {
    maven("https://jitpack.io")
}
```

Then:

```kotlin
dependencies {
    implementation("rip.hippo:Lumos:2.0.1")
}
```

# Using Lumos

First create a command dispatcher:
```java
CommandDispatcher commandDispatcher = CommandDispatcher.create();
```

Then register a command:
```java
commandDispatcher.register("test command", CommandTree.of(
    CommandNode.label("count").then(CommandNode.integer("arg").then(CommandNode.execute(context -> {
    int arg = context.parseInt("arg");
    for (int i = 0; i < arg; i++) {
      System.out.println("Counting: " + i);
    }
  })))
));
```

You could also implement the Command like this, which may be easier to manage for larger commands:
```java
commandDispatcher.register("test command", CommandTree.of(
  CommandNode.label("count").accept(builder -> {
    builder.integer("arg").accept(subBuilder -> {
      subBuilder.execute(context -> {
        int arg = context.parseInt("arg");
        for (int i = 0; i < arg; i++) {
          System.out.println("Counting: " + i);
        }
      });
    });
  })
));
```

You are able to you `accept` and `then` interchangeably.

Then execute the command:
```java
boolean success = commandDispatcher.dispatch("test command", BufferedArguments.of("count 10"));
// if success is true, the command was executed successfully
// if its false then the dispatcher either could not find an execution node or the command was not found
```