package edu.austral.ingsis;

import edu.austral.ingsis.clifford.*;
import edu.austral.ingsis.clifford.commands.*;
import java.util.*;

public class FileSystemRunnerImpl implements FileSystemRunner {
  private FileSystemState state;
  private final CommandParser parser;

  public FileSystemRunnerImpl() {
    this.state = new FileSystemState();
    CommandRegistry registry = initRegistry();
    this.parser = new CommandParser(registry);
  }

  private CommandRegistry initRegistry() {
    CommandRegistryImpl registry = new CommandRegistryImpl();

    registry.registerCommand("mkdir", (args, state) -> new MkdirCommand(args[1], state));
    registry.registerCommand("touch", (args, state) -> new TouchCommand(args[1], state));
    registry.registerCommand("pwd", (args, state) -> new PwdCommand(state));
    registry.registerCommand(
        "ls",
        (args, state) -> {
          Optional<String> ord =
              Arrays.stream(args)
                  .filter(s -> s.startsWith("--ord="))
                  .map(s -> s.split("=")[1])
                  .findFirst();
          return new LsCommand(state, ord);
        });
    registry.registerCommand("cd", (args, state) -> new CdCommand(args[1], state));
    registry.registerCommand(
        "rm",
        (args, state) -> {
          boolean recursive = Arrays.asList(args).contains("--recursive");
          String name = args[args.length - 1];
          return new RmCommand(name, recursive, state);
        });

    return registry;
  }

  @Override
  public List<String> executeCommands(List<String> commands) {
    List<String> results = new ArrayList<>();

    for (String cmd : commands) {
      Command command = parser.parse(cmd, state);
      String result = command.execute();
      results.add(result);

      if (command instanceof StateUpdatingCommand) {
        state = ((StateUpdatingCommand) command).getUpdatedState();
      }
    }

    return results;
  }
}
