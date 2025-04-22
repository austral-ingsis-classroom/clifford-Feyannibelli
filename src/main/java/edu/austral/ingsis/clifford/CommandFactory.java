package edu.austral.ingsis.clifford;

public class CommandFactory {
  private final FileSystemImpl fileSystem;

  public CommandFactory(FileSystemImpl fileSystem) {
    this.fileSystem = fileSystem;
  }

  public Command createCommand(String commandLine) {
    String[] parts = commandLine.trim().split("\\s+", 2);
    String commandName = parts[0];
    String args = parts.length > 1 ? parts[1] : "";

    switch (commandName) {
      case "ls":
        return createLsCommand(args);
      case "cd":
        return createCdCommand(args);
      case "touch":
        return createTouchCommand(args);
      case "mkdir":
        return createMkdirCommand(args);
      case "rm":
        return createRmCommand(args);
      case "pwd":
        return createPwdCommand();
      default:
        return () -> "Command not found: " + commandName;
    }
  }

  private Command createLsCommand(String args) {
    String orderOption = null;
    if (args.contains("--ord=")) {
      String[] parts = args.split("--ord=");
      if (parts.length > 1) {
        orderOption = parts[1].trim();
      }
    }
    return new LsCommand(fileSystem, orderOption);
  }

  private Command createCdCommand(String args) {
    return new CdCommand(fileSystem, args.trim());
  }

  private Command createTouchCommand(String args) {
    return new TouchCommand(fileSystem, args.trim());
  }

  private Command createMkdirCommand(String args) {
    return new MkdirCommand(fileSystem, args.trim());
  }

  private Command createRmCommand(String args) {
    boolean recursive = false;
    String name = args.trim();

    if (args.contains("--recursive")) {
      recursive = true;
      name = args.replace("--recursive", "").trim();
    }

    return new RmCommand(fileSystem, name, recursive);
  }

  private Command createPwdCommand() {
    return new PwdCommand(fileSystem);
  }
}
