import java.util.*;
/*This program can perfectly handle any integer (i.e. negative, > 100, etc.)
Also, please note that this assignment was handed in a 15 minutes late because I didn't realize that I was supposed to handle negative numbers (instead of just saying "invalid input") till the last minute */
class Main {
  public static void main(String[] args) {
    userChoose();
  }
  public static void userChoose() {
    //Variable initialization
    Scanner in = new Scanner(System.in);
    int angle = -1; //because -1 is impossible
    String compass = "";
    int choice;
    System.out.println("(1) Convert bearing to compass.");
    System.out.println("(2) Convert compass to bearing.");
    //User Input
    try {
      choice = in.nextInt();
      if (choice == 1) { //(1) Convert bearing to compass.
        do {
          System.out.println();
          angle = bearingInput(angle);
        }
        while (angle == -1); //Keep asking until valid
        angle = angle % 360;
        System.out.println(bearingToCompass(angle));
      }
      else if (choice == 2) { //(2) Convert compass to bearing.
        do {
          System.out.println();
          compass = compassInput(compass);
        }
        while (compass.length() == 0); //Keep asking until valid
        System.out.println(compassToBearing(compass));
      }
      else {
        System.out.println(choice + " is not a valid choice! Please choose 1 or 2.");
        userChoose();
      }
      if (ynInput()) 
        userChoose();
    }
    catch (InputMismatchException e) { //choice is not an int
      System.out.println("That is not a valid choice! Please choose 1 or 2.");
      userChoose();
    }
  }
  public static boolean ynInput() {
    Scanner in = new Scanner(System.in);
    System.out.println("Run Again? (Y/N)");
    String uChoice = in.next().substring(0,1).toUpperCase();
    if (uChoice.equals("Y"))
      return true;
    else if (uChoice.equals("N")) {
      return false;
    } 
    else {
      System.out.println("That is not a valid choice. Please enter \'Y\'or \'N\'");
      return ynInput();
    }
  }
  public static int bearingInput(int input) {
    Scanner in = new Scanner(System.in);
    System.out.println("Enter a bearing: ");
    try {
      input = in.nextInt();
      while (input < 0) { //Cannot have an angle less than 0
        input += 360;
      }
      return input; 
    }
    catch (InputMismatchException e) {
      System.out.println("That is not a valid input, please try again.");
      return input; //returning the original given argument shows that there was an input issue
    }
  }
  public static String compassInput(String input) {
    Scanner in = new Scanner(System.in);
    System.out.println("Compass direction: ");
    input = in.next();
    if (input.length() == 0) { 
      System.out.println(input + " is not a valid input, please try again.");
      return ""; //returning the "" shows that there was an input issue
    }
    else {
      return input;
    }
  }
  public static String bearingToCompass(int angle) {
    String compass = "";
    int pos = angle / 45; // divide the compass into 8 sections; each has a range/interval of [0 + 45n, 45 + 45n) where n is an integer 
    if (angle % 45 == 0) { //if angle is a multiple of 45 degrees
      switch (pos) {
        case 0: compass = "N";
                break;
        case 1: compass = "NE";
                break;
        case 2: compass = "E";
                break;
        case 3: compass = "SE";
                break;
        case 4: compass = "S";
                break;
        case 5: compass = "SW";
                break;
        case 6: compass = "W";
                break;
        case 7: compass = "NW";
                break;
      }
    }
    else {
      switch (pos) {
        case 0: compass = "N" + angle + "E";
                break;
        case 1: compass = "E" + (90-angle) + "N";
                break;
        case 2: compass = "E" + (angle-90) + "S";
                break;
        case 3: compass = "S" + (180-angle) + "E";
                break;
        case 4: compass = "S" + (angle-180) + "W";
                break;
        case 5: compass = "W" + (270-angle) + "S";
                break;
        case 6: compass = "W" + (angle-270) + "N";
                break;
        case 7: compass = "N" + (360-angle)+ "W";
                break;
      }
    }
    return compass;
  }//bearingToCompass(angle)
  public static int compassToBearing(String compass) {
    int angle;
    String[] dir = new String[2];
    compass = compass.toUpperCase();
    switch (compass.length()) {
      case 1: if (compass.equals("N")) 
              angle = 0;
              else if (compass.equals("E"))
                angle = 90;
              else if (compass.equals("S"))
                angle = 180;
              else if (compass.equals("W"))
                angle = 270;
              else {
                do {
                  System.out.println("That is an invalid input! Please try again.");
                  compass = compassInput(compass);
                }
                while (compass.length() == 0);
                angle = compassToBearing(compass);
              }
              break;
      case 2: if (compass.equals("NE"))
                angle = 45;
              else if (compass.equals("SE"))
                angle = 135;
              else if (compass.equals("SW"))
                angle = 225;
              else if (compass.equals("NW")) 
                angle = 315;
              else {
                do {
                  System.out.println("That is an invalid input! Please try again.");
                  compass = compassInput(compass);
                }
                while (compass.length() == 0);
                angle = compassToBearing(compass);
              }
              break;
      default:dir[0] = compass.substring(0, 1);
              dir[1] = compass.substring(compass.length()-1, compass.length());
              try {
                angle = Integer.parseInt(compass.substring(1, compass.length()-1));
                angle = angle % 360;
                if (dir[0].equals("N")) {
                  if(dir[1].equals("W"))
                    angle = 360-angle;
                  else if (!dir[1].equals("E")) {//if East of North, can just keep the angle as is
                    do {
                    System.out.println("That is an invalid input! Please try again.");
                    compass = compassInput(compass);
                    }
                    while (compass.length() == 0);
                    angle = compassToBearing(compass);
                  }
                }
                else if (dir[0].equals("E")) {
                  if(dir[1].equals("N"))
                    angle = 90-angle;
                  else if (dir[1].equals("S"))
                    angle = 90+angle;
                  else {
                    do {
                    System.out.println("That is an invalid input! Please try again.");
                    compass = compassInput(compass);
                    }
                    while (compass.length() == 0);
                    angle = compassToBearing(compass);
                  }
                }
                else if (dir[0].equals("S")) {
                  if (dir[1].equals("E"))
                    angle = 180-angle;
                  else if (dir[1].equals("W"))
                    angle = 180+angle;
                  else {
                    do {
                    System.out.println("That is an invalid input! Please try again.");
                    compass = compassInput(compass);
                    }
                    while (compass.length() == 0);
                    angle = compassToBearing(compass);
                  }
                }
                else if (dir[0].equals("W")) {
                  if (dir[1].equals("S"))
                    angle = 270-angle;
                  else if (dir[1].equals("N"))
                    angle = 270+angle;
                  else {
                    do {
                    System.out.println("That is an invalid input! Please try again.");
                    compass = compassInput(compass);
                    }
                    while (compass.length() == 0);
                    angle = compassToBearing(compass);
                  }
                }
                else {
                  do {
                    System.out.println("That is an invalid input! Please try again.");
                    compass = compassInput(compass);
                  }
                  while (compass.length() == 0);
                  angle = compassToBearing(compass);
                }
              }//try
              catch (NumberFormatException e) {
                do {
                    System.out.println("That is an invalid input! Please try again.");
                    compass = compassInput(compass);
                  }
                  while (compass.length() == 0);
                  angle = compassToBearing(compass);
              }
    }//switch
    while (angle < 0) { //Integers greater than 45 may result in negative numbers
      angle += 360;
    }
    return angle;
  }//compassToBearing(compass)
}
