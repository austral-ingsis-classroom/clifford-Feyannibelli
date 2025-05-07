package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.*;

public class MkdirCommand implements StateUpdatingCommand {
  private final String name;
  private final FileSystemState state;
  private FileSystemState updatedState;

  public MkdirCommand(String name, FileSystemState state) {
    this.name = name;
    this.state = state;
    this.updatedState = state;
  }

  @Override
  public String execute() {
    if (name.contains("/") || name.contains(" ")) {
      return "invalid directory name";
    }

    Directory current = state.getCurrent();
    if (current.getChild(name).isPresent()) {
      return "entry already exists";
    }

    Directory newDir = new Directory(name, current);
    Directory updatedCurrent = current.addChild(newDir);
    updatedState = state.withUpdatedDirectory(current, updatedCurrent);

    return "'" + name + "' directory created";
  }

  @Override
  public FileSystemState getUpdatedState() {
    return updatedState;
  }
}
