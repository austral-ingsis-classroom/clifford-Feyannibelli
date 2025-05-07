package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.FileSystemState;
import java.util.*;

public class CommandRegistryImpl implements CommandRegistry {
  private final Map<String, CommandFactory> commandFactories;

  public CommandRegistryImpl() {
    this.commandFactories = new HashMap<>();
  }

  public CommandRegistryImpl(Map<String, CommandFactory> commandFactories) {
    this.commandFactories = new HashMap<>(commandFactories);
  }

  public void registerCommand(String name, CommandFactory factory) {
    commandFactories.put(name, factory);
  }

  @Override
  public Optional<Command> findCommand(String commandName, String[] args, FileSystemState state) {
    return Optional.ofNullable(commandFactories.get(commandName))
        .map(factory -> factory.create(args, state));
  }
}
