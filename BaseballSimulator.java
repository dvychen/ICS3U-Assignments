import java.util.*;

class Main {
  static final int numInnings = 3;
  static int cInning = 1;
  static int homeScore = 0;
  static int awayScore = 0;
  static String homeName, awayName;
  static int[] basePop = {0, 0, 0, 0}; //{base1, base2, base3, homeBase / points scored}
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    System.out.println("Please enter the name of the Home team.");
    homeName = in.next();
    System.out.println("Please enter the name of the Away team.");
    awayName = in.next();

    while (cInning <= numInnings || homeScore == awayScore) {
      inning(cInning);
      cInning++;
    }
    System.out.println("-----");
    if (homeScore > awayScore) {
      System.out.println("The winner is " + homeName + "!");
    }
    else if (awayScore < homeScore) {
      System.out.println("The winner is " + awayName + "!");
    }
  }
  public static void inning(int inningNum) {
    int outs = 0;
    int rng;
    boolean isHomeTeam = true;
    System.out.println("Inning #" + inningNum + ":");
    System.out.println(homeName + " is up to bat.");
    for (int t = 0; t < 2; t++) { //iterate twice
       while (outs < 3) {
        rng = rngHit();
        switch (rng) {
          case 0:   outs++;
                    System.out.println("The batter has hit an out! They are currently at " + outs + " out(s)!");
                    break;
          default:  shiftBases(rng, isHomeTeam);
                    break; 
        }
      }//while loop
      outs = 0;
      for (int j = 0; j < basePop.length; j++) { //get all the HOME players off the field
        basePop[j] = 0;
      }
      if (isHomeTeam)
        System.out.println(homeName + " has hit 3 out(s). Switching roles! " + awayName + " is now batting.");
      else {
        System.out.println("End of Inning #" + inningNum + "!");
        scoreCheck();
      }
      isHomeTeam = !isHomeTeam;
    }
  }
  public static int rngHit() {
    /* Ranges
    45% Out: [0, 8]
    30% Single: [9, 14]
    10% Double: [15, 16]
    10% Triple: [17, 18]
    5% Home run: 19
    */
    int hit;
    int rng = (int)Math.floor(Math.random() * 20); //rng [0, 19]
    if (rng <= 8) //out
      hit = 0;
    else if (rng <= 14) //single
      hit = 1;
    else if (rng <= 16) //double
      hit = 2;
    else if (rng <= 18) //triple
      hit = 3;
    else {
      hit = 4;
    }
    return hit;
  }
  public static void shiftBases(int bases, boolean isHomeTeam) {
    int scoreInc = 0;
    if (bases == 4) { //Home Run
      scoreInc++; //the batter scores a point
      for (int i = 0; i < 3; i++) {
        scoreInc += basePop[i]; 
        basePop[i] = 0;
      }
      System.out.println("The batter has hit a homerun!");
    }
    else { //Single, double, or triple
      switch (bases) {
        case 1: System.out.println("The batter has hit a single!");
                break;
        case 2: System.out.println("The batter has hit a double!");
                break;
        case 3: System.out.println("The batter has hit a triple!");
                break;
      }
      basePop[bases-1]++; //batter runs to the base
      for (int i = 0; i < 3; i++) { //loop through every base
        if (basePop[i] > 1 || i < bases-1 && basePop[i] != 0) { //they should move
          basePop[i]--;
          try {
            basePop[i+bases]++;
          }
          catch (ArrayIndexOutOfBoundsException e) {
            basePop[3]++;
          }
        }
      }
    }
    scoreInc += basePop[3];
    basePop[3] = 0;
    if (isHomeTeam) {
      homeScore += scoreInc;
      if (scoreInc != 0) {
        System.out.println(homeName + " has scored " + scoreInc + " points!");
        scoreCheck();
        }
    }
    else {
      awayScore += scoreInc;
      if (scoreInc != 0) {
        System.out.println(awayName + " has scored " + scoreInc + " points!");
        scoreCheck();
      }
    }
    scoreInc = 0;
  }
  public static void scoreCheck() {
    System.out.println("The score is now " + homeScore + " (" + homeName + ") to " + awayScore + " (" + awayName + ").");
  }
}
