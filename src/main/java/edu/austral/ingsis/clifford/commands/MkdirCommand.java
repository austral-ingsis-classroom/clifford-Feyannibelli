package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.*;

public class MkdirCommand implements Command {
  private final String name;
  private final FileSystemState state;

  public MkdirCommand(String name, FileSystemState state) {
    this.name = name;
    this.state = state;
  }

  @Override
  public String execute() {
    if (name.contains("/") || name.contains(" ")) return "invalid directory name";
    Directory current = state.getCurrent();
    if (current.getChild(name).isPresent()) return "entry already exists";
    current.add(new Directory(name, current));
    return "'" + name + "' directory created";
  }
}
