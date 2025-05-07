package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.FileSystemState;
import java.util.*;

public interface CommandRegistry {
  Optional<Command> findCommand(String commandName, String[] args, FileSystemState state);
}
