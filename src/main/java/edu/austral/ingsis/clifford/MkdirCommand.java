package edu.austral.ingsis.clifford;

public class MkdirCommand implements Command {
  private final FileSystemImpl fileSystem;
  private final String dirName;

  public MkdirCommand(FileSystemImpl fileSystem, String dirName) {
    this.fileSystem = fileSystem;
    this.dirName = dirName;
  }

  @Override
  public String execute() {
    if (dirName.contains("/") || dirName.contains(" ")) {
      return "Invalid directory name: " + dirName;
    }

    Directory currentDir = fileSystem.getCurrentDirectory();
    Directory newDir = new Directory(dirName, currentDir);
    currentDir.addChild(newDir);

    return "'" + dirName + "' directory created";
  }
}
