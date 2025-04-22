package edu.austral.ingsis.clifford;

public class CdCommand implements Command {
  private final FileSystemImpl fileSystem;
  private final String directoryPath;

  public CdCommand(FileSystemImpl fileSystem, String directoryPath) {
    this.fileSystem = fileSystem;
    this.directoryPath = directoryPath;
  }

  @Override
  public String execute() {
    if (directoryPath.equals(".")) {
      return "moved to directory '" + fileSystem.getCurrentDirectory().getName() + "'";
    }

    if (directoryPath.equals("..") || directoryPath.equals("../")) {
      Directory parent = fileSystem.getCurrentDirectory().getParent();
      if (parent != null) {
        fileSystem.setCurrentDirectory(parent);
        return "moved to directory '" + parent.getName() + "'";
      } else {
        fileSystem.setCurrentDirectory(fileSystem.getRoot());
        return "moved to directory '/'";
      }
    }

    if (directoryPath.startsWith("/")) {
      return navigateFromRoot(directoryPath.substring(1));
    }

    if (directoryPath.contains("../") || directoryPath.contains("..")) {
      return navigateWithParentReferences(directoryPath);
    }

    return navigateRelative(directoryPath);
  }

  private String navigateWithParentReferences(String path) {
    Directory current = fileSystem.getCurrentDirectory();
    String[] components = path.split("/");
    String lastComponentUsed = current.getName();

    for (String component : components) {
      if (component.isEmpty()) continue;

      if (component.equals("..")) {
        Directory parent = current.getParent();
        if (parent != null) {
          current = parent;
          lastComponentUsed = current.getName();
        } else {
          current = fileSystem.getRoot();
          lastComponentUsed = "/";
        }
      } else {
        if (!current.hasChild(component)) {
          return "'" + component + "' directory does not exist";
        }

        FileSystemElement element = current.getChild(component);
        if (!element.isDirectory()) {
          return "'" + component + "' is not a directory";
        }

        current = (Directory) element;
        lastComponentUsed = component;
      }
    }

    fileSystem.setCurrentDirectory(current);

    if (current == fileSystem.getRoot()) {
      return "moved to directory '/'";
    }

    return "moved to directory '" + lastComponentUsed + "'";
  }

  private String navigateFromRoot(String path) {
    Directory current = fileSystem.getRoot();

    if (path.isEmpty()) {
      fileSystem.setCurrentDirectory(current);
      return "moved to directory '/'";
    }

    String[] components = path.split("/");
    String lastComponent = "";

    for (String component : components) {
      if (component.isEmpty()) continue;

      lastComponent = component;

      if (!current.hasChild(component)) {
        return "'" + component + "' directory does not exist";
      }

      FileSystemElement element = current.getChild(component);
      if (!element.isDirectory()) {
        return "'" + component + "' is not a directory";
      }

      current = (Directory) element;
    }

    fileSystem.setCurrentDirectory(current);
    return "moved to directory '" + lastComponent + "'";
  }

  private String navigateRelative(String path) {
    Directory current = fileSystem.getCurrentDirectory();

    String[] components = path.split("/");
    String lastComponent = "";

    for (String component : components) {
      if (component.isEmpty()) continue;

      lastComponent = component;

      if (!current.hasChild(component)) {
        return "'" + component + "' directory does not exist";
      }

      FileSystemElement element = current.getChild(component);
      if (!element.isDirectory()) {
        return "'" + component + "' is not a directory";
      }

      current = (Directory) element;
    }

    fileSystem.setCurrentDirectory(current);
    return "moved to directory '" + lastComponent + "'";
  }
}
