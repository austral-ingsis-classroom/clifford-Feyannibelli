package edu.austral.ingsis.clifford;

import java.util.*;

public class Directory implements FileSystemElement {
  private final String name;
  private final Map<String, FileSystemElement> children;
  private final long creationTime;
  private Directory parent;

  public Directory(String name) {
    this.name = name;
    this.children = new LinkedHashMap<>();
    this.creationTime = System.currentTimeMillis();
  }

  public Directory(String name, Directory parent) {
    this(name);
    this.parent = parent;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean isDirectory() {
    return true;
  }

  @Override
  public long getCreationTime() {
    return creationTime;
  }

  public Directory getParent() {
    return parent;
  }

  public void setParent(Directory parent) {
    this.parent = parent;
  }

  public void addChild(FileSystemElement element) {
    children.put(element.getName(), element);
  }

  public void removeChild(String name) {
    children.remove(name);
  }

  public boolean hasChild(String name) {
    return children.containsKey(name);
  }

  public FileSystemElement getChild(String name) {
    return children.get(name);
  }

  public List<FileSystemElement> getChildren() {
    return new ArrayList<>(children.values());
  }
}
