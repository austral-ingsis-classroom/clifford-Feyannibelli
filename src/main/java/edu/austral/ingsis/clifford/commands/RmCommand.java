package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.*;

public class RmCommand implements StateUpdatingCommand {
  private final String name;
  private final boolean recursive;
  private final FileSystemState state;
  private FileSystemState updatedState;

  public RmCommand(String name, boolean recursive, FileSystemState state) {
    this.name = name;
    this.recursive = recursive;
    this.state = state;
    this.updatedState = state;
  }

  @Override
  public String execute() {
    Directory current = state.getCurrent();
    var childOption = current.getChild(name);

    if (childOption.isEmpty()) {
      return "'" + name + "' does not exist";
    }

    FileSystem target = childOption.get();

    if (target.isDirectory() && !recursive) {
      return "cannot remove '" + name + "', is a directory";
    }

    Directory updatedCurrent = current.removeChild(target);
    updatedState = state.withUpdatedDirectory(current, updatedCurrent);

    return "'" + name + "' removed";
  }

  @Override
  public FileSystemState getUpdatedState() {
    return updatedState;
  }
}
