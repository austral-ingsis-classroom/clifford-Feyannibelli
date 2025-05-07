package edu.austral.ingsis;

import edu.austral.ingsis.clifford.*;
import edu.austral.ingsis.clifford.commands.*;

public class CommandParser {
  private final CommandRegistry registry;

  public CommandParser(CommandRegistry registry) {
    this.registry = registry;
  }

  public Command parse(String input, FileSystemState state) {
    String[] parts = input.split(" ");
    String cmd = parts[0];

    return registry.findCommand(cmd, parts, state).orElse(() -> "unknown command");
  }
}
