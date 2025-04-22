package edu.austral.ingsis.clifford;

public interface FileSystemElement {
  String getName();

  boolean isDirectory();

  long getCreationTime();
}
