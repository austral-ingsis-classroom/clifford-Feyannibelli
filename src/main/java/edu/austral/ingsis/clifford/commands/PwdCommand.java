package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.*;

public class PwdCommand implements Command {
  private final FileSystemState state;

  public PwdCommand(FileSystemState state) {
    this.state = state;
  }

  @Override
  public String execute() {
    return state.getPath();
  }
}
