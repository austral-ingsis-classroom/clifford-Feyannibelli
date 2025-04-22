package edu.austral.ingsis;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class FileSystemTests {

  private final FileSystemRunner runner = new FileSystemRunnerImpl();

  private void executeTest(List<Map.Entry<String, String>> commandsAndResults) {
    final List<String> commands = commandsAndResults.stream().map(Map.Entry::getKey).toList();
    final List<String> expectedResult =
        commandsAndResults.stream().map(Map.Entry::getValue).toList();

    final List<String> actualResult = runner.executeCommands(commands);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void test1() {
    executeTest(
        List.of(
            entry("ls", ""),
            entry("mkdir horace", "'horace' directory created"),
            entry("ls", "horace"),
            entry("mkdir emily", "'emily' directory created"),
            entry("ls", "horace emily"),
            entry("ls --ord=asc", "emily horace")));
  }

  @Test
  void test2() {
    executeTest(
        List.of(
            entry("mkdir horace", "'horace' directory created"),
            entry("mkdir emily", "'emily' directory created"),
            entry("mkdir jetta", "'jetta' directory created"),
            entry("ls", "horace emily jetta"),
            entry("cd emily", "moved to directory 'emily'"),
            entry("pwd", "/emily"),
            entry("touch elizabeth.txt", "'elizabeth.txt' file created"),
            entry("mkdir t-bone", "'t-bone' directory created"),
            entry("ls", "elizabeth.txt t-bone")));
  }

  @Test
  void test3() {
    executeTest(
        List.of(
            entry("mkdir horace", "'horace' directory created"),
            entry("mkdir emily", "'emily' directory created"),
            entry("mkdir jetta", "'jetta' directory created"),
            entry("cd emily", "moved to directory 'emily'"),
            entry("touch elizabeth.txt", "'elizabeth.txt' file created"),
            entry("mkdir t-bone", "'t-bone' directory created"),
            entry("ls", "t-bone elizabeth.txt"),
            entry("rm t-bone", "cannot remove 't-bone', is a directory"),
            entry("rm --recursive t-bone", "'t-bone' removed"),
            entry("ls", "elizabeth.txt"),
            entry("rm elizabeth.txt", "'elizabeth.txt' removed"),
            entry("ls", "")));
  }

  @Test
  void test4() {
    executeTest(
        List.of(
            entry("mkdir horace", "'horace' directory created"),
            entry("mkdir emily", "'emily' directory created"),
            entry("cd horace", "moved to directory 'horace'"),
            entry("mkdir jetta", "'jetta' directory created"),
            entry("cd ..", "moved to directory '/'"),
            entry("cd horace/jetta", "moved to directory 'jetta'"),
            entry("pwd", "/horace/jetta"),
            entry("cd /", "moved to directory '/'")));
  }

  @Test
  void test5() {
    executeTest(
        List.of(
            entry("mkdir emily", "'emily' directory created"),
            entry("cd horace", "'horace' directory does not exist")));
  }

  @Test
  void test6() {
    executeTest(List.of(entry("cd ..", "moved to directory '/'")));
  }

  @Test
  void test7() {
    executeTest(
        List.of(
            entry("mkdir horace", "'horace' directory created"),
            entry("cd horace", "moved to directory 'horace'"),
            entry("touch emily.txt", "'emily.txt' file created"),
            entry("touch jetta.txt", "'jetta.txt' file created"),
            entry("ls", "emily.txt jetta.txt"),
            entry("rm emily.txt", "'emily.txt' removed"),
            entry("ls", "jetta.txt")));
  }

  @Test
  void test8() {
    executeTest(
        List.of(
            entry("mkdir emily", "'emily' directory created"),
            entry("cd emily", "moved to directory 'emily'"),
            entry("mkdir emily", "'emily' directory created"),
            entry("touch emily.txt", "'emily.txt' file created"),
            entry("touch jetta.txt", "'jetta.txt' file created"),
            entry("ls", "emily emily.txt jetta.txt"),
            entry("rm --recursive emily", "'emily' removed"),
            entry("ls", "emily.txt jetta.txt"),
            entry("ls --ord=desc", "jetta.txt emily.txt")));
  }

  @Test
  void test9_cdDotAndNonDirectoryPaths() {
    executeTest(
        List.of(
            entry("mkdir testdir", "'testdir' directory created"),
            entry("cd testdir", "moved to directory 'testdir'"),
            entry("cd .", "moved to directory 'testdir'"),
            entry("touch testfile.txt", "'testfile.txt' file created"),
            entry("cd testfile.txt", "'testfile.txt' is not a directory"),
            entry("cd /", "moved to directory '/'"),
            entry("mkdir emptydir", "'emptydir' directory created"),
            entry("touch rootfile.txt", "'rootfile.txt' file created"),
            entry("cd rootfile.txt", "'rootfile.txt' is not a directory"),
            entry("cd /nonexistent", "'nonexistent' directory does not exist"),
            entry("cd nonexistent/subdir", "'nonexistent' directory does not exist")));
  }

  @Test
  void test10_complexPathBuilding() {
    executeTest(
        List.of(
            entry("mkdir level1", "'level1' directory created"),
            entry("cd level1", "moved to directory 'level1'"),
            entry("mkdir level2", "'level2' directory created"),
            entry("cd level2", "moved to directory 'level2'"),
            entry("pwd", "/level1/level2"),
            entry("cd /", "moved to directory '/'"),
            entry("cd level1/level2", "moved to directory 'level2'"),
            entry("pwd", "/level1/level2"),
            entry("cd ../", "moved to directory 'level1'"),
            entry("pwd", "/level1")));
  }

  @Test
  void test11_nestedDirectoryListing() {
    executeTest(
        List.of(
            entry("mkdir dir1", "'dir1' directory created"),
            entry("cd dir1", "moved to directory 'dir1'"),
            entry("mkdir nesteddir", "'nesteddir' directory created"),
            entry("touch file1.txt", "'file1.txt' file created"),
            entry("ls", "nesteddir file1.txt"),
            entry("cd nesteddir", "moved to directory 'nesteddir'"),
            entry("pwd", "/dir1/nesteddir"),
            entry("touch nestedfile.txt", "'nestedfile.txt' file created"),
            entry("ls", "nestedfile.txt"),
            entry("cd ../..", "moved to directory '/'")));
  }

  @Test
  void test12_specialCaseDirectoryOperations() {
    executeTest(
        List.of(
            entry("mkdir folder1", "'folder1' directory created"),
            entry("cd folder1/", "moved to directory 'folder1'"),
            entry("mkdir folder2", "'folder2' directory created"),
            entry("cd .", "moved to directory 'folder1'"),
            entry("cd /folder1/folder2", "moved to directory 'folder2'"),
            entry("cd ../", "moved to directory 'folder1'"),
            entry("pwd", "/folder1")));
  }

  @Test
  void test13_emptyComponentNavigation() {
    executeTest(
        List.of(
            entry("mkdir testdir", "'testdir' directory created"),
            entry("cd testdir//subdir", "'subdir' directory does not exist"),
            entry("cd /testdir//", "moved to directory 'testdir'"),
            entry("pwd", "/testdir")));
  }
}
