package edu.austral.ingsis.clifford;

public class FileSystemImpl implements FileSystem {
  private final Directory root;
  private Directory currentDirectory;

  public FileSystemImpl() {
    root = new Directory("/");
    currentDirectory = root;
  }

  public Directory getRoot() {
    return root;
  }

  public Directory getCurrentDirectory() {
    return currentDirectory;
  }

  public void setCurrentDirectory(Directory directory) {
    this.currentDirectory = directory;
  }

  public String getCurrentPath() {
    if (currentDirectory == root) {
      return "/";
    }

    StringBuilder path = new StringBuilder();
    buildPath(currentDirectory, path);

    return path.toString();
  }

  private void buildPath(Directory dir, StringBuilder path) {
    if (dir == root) {
      if (path.length() == 0 || path.charAt(0) != '/') {
        path.insert(0, "/");
      }
      return;
    }

    path.insert(0, dir.getName());

    path.insert(0, "/");

    if (dir.getParent() != null) {
      buildPath(dir.getParent(), path);
    } else {
      if (path.length() == 0 || path.charAt(0) != '/') {
        path.insert(0, "/");
      }
    }
  }
}
