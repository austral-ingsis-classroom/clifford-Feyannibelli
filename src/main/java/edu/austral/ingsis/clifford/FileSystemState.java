package edu.austral.ingsis.clifford;

import java.util.*;

public final class FileSystemState {
  private final Directory root = new Directory("/", null);
  private Directory current = root;

  public Directory getCurrent() {
    return current;
  }

  public void setCurrent(Directory directory) {
    current = directory;
  }

  public Directory getRoot() {
    return root;
  }

  public String getPath() {
    List<String> path = new ArrayList<>();
    Directory pointer = current;
    while (pointer != null && pointer.getParent().isPresent()) {
      path.add(pointer.getName());
      pointer = pointer.getParent().orElse(null);
    }
    if (!"/".equals(current.getName())) {
      Collections.reverse(path);
      return "/" + String.join("/", path);
    }
    return "/";
  }
}
