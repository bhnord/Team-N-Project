package edu.wpi.TeamN.entity;

import java.io.FileNotFoundException;
import java.util.*;

public class CSV implements Iterable<String> {
  public static void main(String[] args) throws FileNotFoundException {
    CSV c = new CSV("MapCSV/MapNNodesAll.csv");
    System.out.println(c.toString());
    System.out.println(c.getLineCount());
  }

  Set<String> csvLines = new HashSet<>();
  Scanner scanner;

  public CSV(String path) throws FileNotFoundException {
    this.scanner =
        new Scanner(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(path)));
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

  @Override
  public Iterator<String> iterator() {
    return csvLines.iterator();
  }
}
