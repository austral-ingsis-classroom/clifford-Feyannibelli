package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.*;

public class CdCommand implements Command {
  private final String path;
  private final FileSystemState state;

  public CdCommand(String path, FileSystemState state) {
    this.path = path;
    this.state = state;
  }

  @Override
  public String execute() {
    Directory target = path.startsWith("/") ? state.getRoot() : state.getCurrent();
    if (path.equals(".")) return "moved to directory '" + target.getName() + "'";
    if (path.equals("..")) {
      if (target.getParent().isPresent()) {
        state.setCurrent(target.getParent().get());
      }
      return "moved to directory '/'";
    }

    for (String part : path.split("/")) {
      if (part.isEmpty() || part.equals(".")) continue;
      if (part.equals("..")) {
        if (target.getParent().isPresent()) target = target.getParent().get();
        continue;
      }
      var next = target.getChild(part);
      if (next.isEmpty()) return "'" + part + "' directory does not exist";
      if (!next.get().isDirectory()) return "'" + part + "' is not a directory";
      target = (Directory) next.get();
    }
    state.setCurrent(target);
    return "moved to directory '" + target.getName() + "'";
  }
}
