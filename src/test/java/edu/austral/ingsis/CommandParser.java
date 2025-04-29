package edu.austral.ingsis;

import edu.austral.ingsis.clifford.*;
import edu.austral.ingsis.clifford.commands.*;
import java.util.*;

public class CommandParser {
  public static Command parse(String input, FileSystemState state) {
    String[] parts = input.split(" ");
    String cmd = parts[0];

    return switch (cmd) {
      case "mkdir" -> new MkdirCommand(parts[1], state);
      case "touch" -> new TouchCommand(parts[1], state);
      case "pwd" -> new PwdCommand(state);
      case "ls" -> {
        Optional<String> ord =
            Arrays.stream(parts)
                .filter(s -> s.startsWith("--ord="))
                .map(s -> s.split("=")[1])
                .findFirst();
        yield new LsCommand(state, ord);
      }
      case "cd" -> new CdCommand(parts[1], state);
      case "rm" -> {
        boolean recursive = Arrays.stream(parts).anyMatch(p -> p.equals("--recursive"));
        String name = parts[parts.length - 1];
        yield new RmCommand(name, recursive, state);
      }
      default -> () -> "unknown command";
    };
  }
}
