package edu.austral.ingsis.clifford;

import java.util.*;

public final class Directory implements FileSystem {
  private final String name;
  private final List<FileSystem> children;
  private final Directory parent;

  public Directory(String name, Directory parent) {
    this.name = name;
    this.parent = parent;
    this.children = new ArrayList<>();
  }

  public List<FileSystem> getChildren() {
    return new ArrayList<>(children);
  }

  public Optional<Directory> getParent() {
    return Optional.ofNullable(parent);
  }

  public Optional<FileSystem> getChild(String name) {
    return children.stream().filter(c -> c.getName().equals(name)).findFirst();
  }

  public void add(FileSystem fs) {
    children.add(fs);
  }

  public void remove(FileSystem fs) {
    children.remove(fs);
  }

  public void clear() {
    children.clear();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean isDirectory() {
    return true;
  }
}
