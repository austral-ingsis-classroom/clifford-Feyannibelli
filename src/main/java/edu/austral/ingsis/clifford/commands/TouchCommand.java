package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.*;

public class TouchCommand implements Command {
  private final String name;
  private final FileSystemState state;

  public TouchCommand(String name, FileSystemState state) {
    this.name = name;
    this.state = state;
  }

  @Override
  public String execute() {
    if (name.contains("/") || name.contains(" ")) return "invalid file name";
    Directory current = state.getCurrent();
    if (current.getChild(name).isPresent()) return "entry already exists";
    current.add(new File(name));
    return "'" + name + "' file created";
  }
}
