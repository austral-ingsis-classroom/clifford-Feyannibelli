package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.FileSystemState;

public interface StateUpdatingCommand extends Command {
  FileSystemState getUpdatedState();
}
