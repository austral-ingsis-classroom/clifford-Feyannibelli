package edu.austral.ingsis.clifford;

import java.util.*;

public final class Directory implements FileSystem {
  private final String name;
  private final List<FileSystem> children;
  private final Directory parent;

  public Directory(String name, Directory parent) {
    this(name, parent, new ArrayList<>());
  }

  Directory(String name, Directory parent, List<FileSystem> children) {
    this.name = name;
    this.parent = parent;
    this.children = List.copyOf(children);
  }

  public List<FileSystem> getChildren() {
    return children;
  }

  public Optional<Directory> getParent() {
    return Optional.ofNullable(parent);
  }

  public Optional<FileSystem> getChild(String name) {
    return children.stream().filter(c -> c.getName().equals(name)).findFirst();
  }

  public Directory addChild(FileSystem fs) {
    List<FileSystem> newChildren = new ArrayList<>(children);
    newChildren.add(fs);
    return new Directory(name, parent, newChildren);
  }

  public Directory removeChild(FileSystem fs) {
    List<FileSystem> newChildren = new ArrayList<>(children);
    newChildren.remove(fs);
    return new Directory(name, parent, newChildren);
  }

  public Directory removeAllChildren() {
    return new Directory(name, parent, new ArrayList<>());
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
