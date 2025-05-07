package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.*;

public class CdCommand implements StateUpdatingCommand {
  private final String path;
  private final FileSystemState state;
  private FileSystemState updatedState;

  public CdCommand(String path, FileSystemState state) {
    this.path = path;
    this.state = state;
    this.updatedState = state;
  }

  @Override
  public String execute() {
    if (path.equals(".")) {
      return "moved to directory '" + state.getCurrent().getName() + "'";
    }

    if (path.equals("..")) {
      Directory current = state.getCurrent();
      if (current.getParent().isPresent()) {
        updatedState = state.withCurrent(current.getParent().get());
        return "moved to directory '" + updatedState.getCurrent().getName() + "'";
      }
      updatedState = state.withCurrent(state.getRoot());
      return "moved to directory '/'";
    }

    // Handle absolute path (starting with /)
    if (path.startsWith("/")) {
      if (path.equals("/")) {
        updatedState = state.withCurrent(state.getRoot());
        return "moved to directory '/'";
      }

      return navigatePath(state.getRoot(), path.substring(1));
    }

    if (path.contains("/")) {
      return navigatePath(state.getRoot(), path);
    }

    return navigatePath(state.getCurrent(), path);
  }

  private String navigatePath(Directory startDir, String relativePath) {
    Directory current = startDir;
    String[] parts = relativePath.split("/");

    for (String part : parts) {
      if (part.isEmpty() || part.equals(".")) {
        continue;
      }

      if (part.equals("..")) {
        if (current.getParent().isPresent()) {
          current = current.getParent().get();
        }
        continue;
      }

      var childOpt = current.getChild(part);
      if (childOpt.isEmpty()) {
        return "'" + part + "' directory does not exist";
      }

      FileSystem child = childOpt.get();
      if (!child.isDirectory()) {
        return "'" + part + "' is not a directory";
      }

      current = (Directory) child;
    }

    updatedState = state.withCurrent(current);
    return "moved to directory '" + current.getName() + "'";
  }

  @Override
  public FileSystemState getUpdatedState() {
    return updatedState;
  }
}
