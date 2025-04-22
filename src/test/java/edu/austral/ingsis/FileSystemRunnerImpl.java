package edu.austral.ingsis;

import edu.austral.ingsis.clifford.Command;
import edu.austral.ingsis.clifford.CommandFactory;
import edu.austral.ingsis.clifford.FileSystemImpl;
import java.util.ArrayList;
import java.util.List;

public class FileSystemRunnerImpl implements FileSystemRunner {
  private final FileSystemImpl fileSystem;
  private final CommandFactory commandFactory;

  public FileSystemRunnerImpl() {
    this.fileSystem = new FileSystemImpl();
    this.commandFactory = new CommandFactory(fileSystem);
  }

  @Override
  public List<String> executeCommands(List<String> commands) {
    List<String> results = new ArrayList<>();

    for (String commandLine : commands) {
      Command command = commandFactory.createCommand(commandLine);
      String result = command.execute();
      results.add(result);
    }

    return results;
  }
}
