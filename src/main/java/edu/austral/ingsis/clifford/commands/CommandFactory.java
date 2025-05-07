package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.FileSystemState;

public interface CommandFactory {
  Command create(String[] args, FileSystemState state);
}
