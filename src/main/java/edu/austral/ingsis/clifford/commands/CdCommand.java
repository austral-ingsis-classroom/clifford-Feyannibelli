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
    // Handle special case for current directory
    if (path.equals(".")) {
      return "moved to directory '" + state.getCurrent().getName() + "'";
    }

    // Handle special case for parent directory
    if (path.equals("..")) {
      Directory current = state.getCurrent();
      if (current.getParent().isPresent()) {
        updatedState = state.withCurrent(current.getParent().get());
      }
      return "moved to directory '/'";
    }

    // Handle absolute path (starting with /)
    if (path.startsWith("/")) {
      if (path.equals("/")) {
        updatedState = state.withCurrent(state.getRoot());
        return "moved to directory '/'";
      }

      // Process absolute path (skip the first '/')
      return navigatePath(state.getRoot(), path.substring(1));
    }

    // Handle relative path with directory components
    return navigatePath(state.getCurrent(), path);
  }

  private String navigatePath(Directory startDir, String relativePath) {
    Directory current = startDir;
    String[] parts = relativePath.split("/");

    for (int i = 0; i < parts.length; i++) {
      String part = parts[i];

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

    // Successfully navigated to target directory
    updatedState = state.withCurrent(current);
    return "moved to directory '" + current.getName() + "'";
  }

  @Override
  public FileSystemState getUpdatedState() {
    return updatedState;
  }
}