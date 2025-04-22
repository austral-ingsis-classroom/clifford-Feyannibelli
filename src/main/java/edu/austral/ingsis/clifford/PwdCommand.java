package edu.austral.ingsis.clifford;

public class PwdCommand implements Command {
  private final FileSystemImpl fileSystem;

  public PwdCommand(FileSystemImpl fileSystem) {
    this.fileSystem = fileSystem;
  }

  @Override
  public String execute() {
    return fileSystem.getCurrentPath();
  }
}
