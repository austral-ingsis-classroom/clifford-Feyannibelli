package edu.austral.ingsis.clifford;

import java.util.*;

public final class FileSystemState {
  private final Directory root;
  private final Directory current;

  public FileSystemState() {
    this.root = new Directory("/", null);
    this.current = root;
  }

  private FileSystemState(Directory root, Directory current) {
    this.root = root;
    this.current = current;
  }

  public Directory getCurrent() {
    return current;
  }

  public FileSystemState withCurrent(Directory newCurrent) {
    return new FileSystemState(root, newCurrent);
  }

  public Directory getRoot() {
    return root;
  }

  public FileSystemState withUpdatedDirectory(Directory oldDir, Directory newDir) {
    if (oldDir == root) {
      return new FileSystemState(newDir, current == oldDir ? newDir : current);
    }

    Directory newRoot = replaceInHierarchy(root, oldDir, newDir);
    Directory newCurrent = current == oldDir ? newDir : current;

    return new FileSystemState(newRoot, newCurrent);
  }

  private Directory replaceInHierarchy(Directory dir, Directory oldDir, Directory newDir) {
    if (dir == oldDir) return newDir;

    List<FileSystem> newChildren = new ArrayList<>();
    boolean changed = false;

    for (FileSystem child : dir.getChildren()) {
      if (child.isDirectory()) {
        Directory childDir = (Directory) child;
        if (childDir == oldDir) {
          newChildren.add(newDir);
          changed = true;
        } else {
          Directory processed = replaceInHierarchy(childDir, oldDir, newDir);
          if (processed != childDir) {
            newChildren.add(processed);
            changed = true;
          } else {
            newChildren.add(child);
          }
        }
      } else {
        newChildren.add(child);
      }
    }

    return changed ? new Directory(dir.getName(), dir.getParent().orElse(null), newChildren) : dir;
  }

  public String getPath() {
    List<String> path = new ArrayList<>();
    Directory pointer = current;
    while (pointer != null && pointer.getParent().isPresent()) {
      path.add(pointer.getName());
      pointer = pointer.getParent().orElse(null);
    }
    assert current != null;
    if (!"/".equals(current.getName())) {
      Collections.reverse(path);
      return "/" + String.join("/", path);
    }
    return "/";
  }
}
