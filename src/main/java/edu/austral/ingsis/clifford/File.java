package edu.austral.ingsis.clifford;

public class File implements FileSystemElement {
  private final String name;
  private final long creationTime;

  public File(String name) {
    this.name = name;
    this.creationTime = System.currentTimeMillis();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean isDirectory() {
    return false;
  }

  @Override
  public long getCreationTime() {
    return creationTime;
  }
}
