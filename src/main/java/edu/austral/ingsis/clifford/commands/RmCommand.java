package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.*;

public class RmCommand implements Command {
  private final String name;
  private final boolean recursive;
  private final FileSystemState state;

  public RmCommand(String name, boolean recursive, FileSystemState state) {
    this.name = name;
    this.recursive = recursive;
    this.state = state;
  }

  @Override
  public String execute() {
    Directory current = state.getCurrent();
    var child = current.getChild(name);
    if (child.isEmpty()) return "'" + name + "' does not exist";
    FileSystem target = child.get();

    if (target.isDirectory()) {
      if (!recursive) return "cannot remove '" + name + "', is a directory";
      removeDirectoryRecursively((Directory) target);
    }

    current.remove(target);
    return "'" + name + "' removed";
  }

  private void removeDirectoryRecursively(Directory dir) {
    for (FileSystem child : dir.getChildren()) {
      if (child.isDirectory()) {
        removeDirectoryRecursively((Directory) child);
      }
    }
    dir.clear();
  }
}
