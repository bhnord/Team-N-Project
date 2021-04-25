package edu.wpi.TeamN.services.algo;

public class WordDistanceComputer {
  public int getDistance(String word1, String word2) {
    int[][] table = new int[word2.length() + 1][word1.length() + 1];
    for (int i = 0; i <= word1.length(); i++) {
      table[0][i] = i;
    }
    for (int i = 0; i <= word2.length(); i++) {
      table[i][0] = i;
    }
    for (int i = 1; i <= word1.length(); i++) {
      for (int j = 1; j <= word2.length(); j++) {
        if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
          table[j][i] = table[j - 1][i - 1];
        } else {
          int num1 = table[j - 1][i - 1];
          int num2 = table[j][i - 1];
          int num3 = table[j - 1][i];
          num1 = Math.min(num1, num2);
          num1 = Math.min(num1, num3);
          table[j][i] = num1 + 1;
        }
      }
    }
    return table[word2.length()][word1.length()];
  }
}
