package edu.wpi.TeamN.services.algo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CartesianTest {

  // dummy
  @Test
  public void testGetEmployeeName() {
    assertEquals(1, 1);
  }

  @Test
  public void testWordDistance() {
    WordDistanceComputer wordDistanceComputer = new WordDistanceComputer();
    assertEquals(2, wordDistanceComputer.getDistance("aa", "b"));
  }
}
