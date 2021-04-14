package edu.wpi.teamname.entity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class CSV {
  public static void main(String[] args) throws FileNotFoundException {
    CSV c = new CSV("src/main/resources/MapCSV/MapNNodesAll.csv");
    System.out.println(c.toString());
    System.out.println(c.getLineCount());
  }

  Set<String> csvLines = new HashSet<>();
  Scanner scanner;

  public CSV(String path) throws FileNotFoundException {
    this.scanner = new Scanner(new File(path));
    while (scanner.hasNext()) {
      String a = scanner.nextLine();
      csvLines.add(a);
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (String str : csvLines) {
      sb.append("\n").append(str);
    }
    return sb.toString();
  }

  public int getLineCount() {
    return csvLines.size();
  }
}
