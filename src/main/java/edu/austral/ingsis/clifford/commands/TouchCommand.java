package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.*;

public class TouchCommand implements StateUpdatingCommand {
  private final String name;
  private final FileSystemState state;
  private FileSystemState updatedState;

  public TouchCommand(String name, FileSystemState state) {
    this.name = name;
    this.state = state;
    this.updatedState = state;
  }

  @Override
  public String execute() {
    if (name.contains("/") || name.contains(" ")) {
      return "invalid file name";
    }

    Directory current = state.getCurrent();
    if (current.getChild(name).isPresent()) {
      return "entry already exists";
    }

    File newFile = new File(name);
    Directory updatedCurrent = current.addChild(newFile);
    updatedState = state.withUpdatedDirectory(current, updatedCurrent);

    return "'" + name + "' file created";
  }

  @Override
  public FileSystemState getUpdatedState() {
    return updatedState;
  }
}
