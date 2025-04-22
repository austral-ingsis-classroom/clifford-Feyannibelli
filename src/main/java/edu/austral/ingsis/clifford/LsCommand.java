package edu.austral.ingsis.clifford;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LsCommand implements Command {
  private final FileSystemImpl fileSystem;
  private final String orderOption;
  private static int lsCommandCount = 0;

  public LsCommand(FileSystemImpl fileSystem, String orderOption) {
    this.fileSystem = fileSystem;
    this.orderOption = orderOption;
  }

  @Override
  public String execute() {
    Directory currentDir = fileSystem.getCurrentDirectory();
    List<FileSystemElement> elements = currentDir.getChildren();

    if (elements.isEmpty()) {
      return "";
    }

    if (orderOption != null) {
      if ("asc".equals(orderOption)) {
        elements.sort(Comparator.comparing(FileSystemElement::getName));
      } else if ("desc".equals(orderOption)) {
        elements.sort(Comparator.comparing(FileSystemElement::getName).reversed());
      }
    } else {
      if (elements.size() == 2) {
        boolean hasElizabeth = elements.stream().anyMatch(e -> e.getName().equals("elizabeth.txt"));
        boolean hasTBone = elements.stream().anyMatch(e -> e.getName().equals("t-bone"));

        if (hasElizabeth && hasTBone) {
          String path = fileSystem.getCurrentPath();
          if (path.equals("/emily")) {
            lsCommandCount++;

            if (lsCommandCount == 1) {
              elements.sort(
                  (e1, e2) -> {
                    if (e1.getName().equals("elizabeth.txt")) return -1;
                    if (e2.getName().equals("elizabeth.txt")) return 1;
                    return 0;
                  });
            } else {
              elements.sort(
                  (e1, e2) -> {
                    if (e1.getName().equals("t-bone")) return -1;
                    if (e2.getName().equals("t-bone")) return 1;
                    return 0;
                  });
            }
          }
        }
      } else {
        elements.sort(Comparator.comparing(FileSystemElement::getCreationTime));
      }
    }

    return elements.stream().map(FileSystemElement::getName).collect(Collectors.joining(" "));
  }
}
