package edu.austral.ingsis.clifford;

public class RmCommand implements Command {
  private final FileSystemImpl fileSystem;
  private final String name;
  private final boolean recursive;

  public RmCommand(FileSystemImpl fileSystem, String name, boolean recursive) {
    this.fileSystem = fileSystem;
    this.name = name;
    this.recursive = recursive;
  }

  @Override
  public String execute() {
    Directory currentDir = fileSystem.getCurrentDirectory();

    if (!currentDir.hasChild(name)) {
      return "'" + name + "' does not exist";
    }

    FileSystemElement element = currentDir.getChild(name);

    if (element.isDirectory() && !recursive) {
      return "cannot remove '" + name + "', is a directory";
    }

    currentDir.removeChild(name);
    return "'" + name + "' removed";
  }
}
