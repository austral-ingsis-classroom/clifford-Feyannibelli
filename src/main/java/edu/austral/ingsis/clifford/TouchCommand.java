package edu.austral.ingsis.clifford;

public class TouchCommand implements Command {
  private final FileSystemImpl fileSystem;
  private final String fileName;

  public TouchCommand(FileSystemImpl fileSystem, String fileName) {
    this.fileSystem = fileSystem;
    this.fileName = fileName;
  }

  @Override
  public String execute() {
    if (fileName.contains("/") || fileName.contains(" ")) {
      return "Invalid file name: " + fileName;
    }

    Directory currentDir = fileSystem.getCurrentDirectory();
    File newFile = new File(fileName);
    currentDir.addChild(newFile);

    return "'" + fileName + "' file created";
  }
}
