package edu.austral.ingsis;

import edu.austral.ingsis.clifford.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileSystemRunnerImpl implements FileSystemRunner {
  private final FileSystemState state = new FileSystemState();

  @Override
  public List<String> executeCommands(List<String> commands) {
    return commands.stream()
        .map(cmd -> CommandParser.parse(cmd, state).execute())
        .collect(Collectors.toList());
  }
}
