package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.*;
import java.util.*;
import java.util.stream.Collectors;

public class LsCommand implements Command {
  private final FileSystemState state;
  private final Optional<String> order;

  public LsCommand(FileSystemState state, Optional<String> order) {
    this.state = state;
    this.order = order;
  }

  @Override
  public String execute() {
    List<FileSystem> children = state.getCurrent().getChildren();
    if (order.isPresent()) {
      Comparator<FileSystem> comparator = Comparator.comparing(FileSystem::getName);
      if (order.get().equals("desc")) comparator = comparator.reversed();
      children = children.stream().sorted(comparator).collect(Collectors.toList());
    }
    return children.stream().map(FileSystem::getName).collect(Collectors.joining(" "));
  }
}
