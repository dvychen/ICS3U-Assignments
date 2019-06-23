import java.util.*;
//http://mrhudson.pbworks.com/w/file/fetch/94788209/ICS3U%20Assignment%205%20-%20Triangle%20Classification.pdf
class Main { 
  public static void main(String[] args) {
    boolean runAgain = true;
    int sides[] = new int[3]; //side lengths of the triangle
    double angles[] = new double[3];
    String angleClass, sideClass;
    int perimeter;
    double area, circRadius, inscRadius;
    boolean isRounding = askRounding();
    System.out.println("DISCLAIMER: Side lengths greater than 32767 will not work.");
    do {
      System.out.println("Please provide 3 side lengths (enter on separate lines). 0 0 0 to terminate.");
      sides = getInput();
      runAgain =  !(sides[0] == 0 && sides[1] == 0 && sides[2] == 0); //check for 0 0 0 termination request
      if (runAgain) { //only display if user did not terminate program
        if (checkValid(sides[0], sides[1], sides[2])) {
          //calculations
          angles = findAngles(sides[0], sides[1], sides[2]);
          angleClass = angleClass(angles);
          sideClass = sideClass(sides[0], sides[1], sides[2]);
          perimeter = sides[0] + sides[1] + sides[2];
          area = calcArea(sides);
          circRadius = calcRadCirc(sides, area);
          inscRadius = calcRadInsc(perimeter, area);
          //printing the results
          display(angleClass, sideClass, sides, angles, perimeter, area, circRadius,inscRadius, isRounding);
        }
        else { //invalid triangle
          System.out.println("Invalid triangle!");
        }
      }      
      System.out.println("------");
    }
    while (runAgain);
    System.out.println("Program terminated."); 
  }
  public static boolean askRounding() {
    Scanner in = new Scanner(System.in);
    System.out.println("Round to nearest thousandth? (Y/N)");
    String uChoice = in.next().substring(0,1).toUpperCase();
    if (uChoice.equals("Y"))
      return true;
    else if (uChoice.equals("N")) {
      return false;
    } 
    else {
      System.out.println("That is not a valid choice. Please enter \'Y\'or \'N\'");
      return askRounding();
    }
  }
  public static int[] getInput() { //needed to avoid infinite loop w/ try&catch InputMismatchException
    Scanner in = new Scanner(System.in);
    int sides[] = new int[3]; //side lengths of the triangle
    try {
      System.out.print("Side a: ");
      sides[0] = in.nextInt();
      System.out.print("Side b: ");
      sides[1] = in.nextInt();
      System.out.print("Side c: ");
      sides[2] = in.nextInt();
    }
    catch (InputMismatchException e){
      System.out.println("Invalid input. Side lengths must be integers.");
      System.out.println("------");
      System.out.println("Please provide 3 side lengths (enter on separate lines). 0 0 0 to terminate.");
      return getInput();
    }
    return sides;
  }
  public static void display(String angleClass, String sideClass, int[] sides, double[] angles, int perimeter, double area, double circRadius, double inscRadius, boolean round) {
    int a = sides[0];
    int b = sides[1];
    int c = sides[2];
    double[] degAngles = new double[3];
    for (int i = 0; i < 3; i++) { //round all the angles to nearest thousandth
      if (round) {
        degAngles[i] = roundThreeDec(Math.toDegrees(angles[i]));
        angles[i] = roundThreeDec(angles[i]); 
      }
      else {
        degAngles[i] = Math.toDegrees(angles[i]);
      }
    }
    if (round) {
      area = roundThreeDec(area);
      circRadius = roundThreeDec(circRadius);
      inscRadius = roundThreeDec(inscRadius);
    }
    System.out.println("------");
    System.out.println("Triangle is valid!");
    System.out.println("It is a(n) " + angleClass + " " + sideClass + " triangle.");
    System.out.println("a = " + a + ", b = " + b + ", c = " + c + "."); 
    System.out.println("Angle A = " + angles[0] + "rad or " + degAngles[0] + "° , Angle B = " + angles[1] + "rad or " + degAngles[1] + "° , Angle C = " + angles[2] + "rad or " + degAngles[2] + "°.");
    System.out.println("Perimeter = " + perimeter + ".");
    System.out.println("Area = " + area + ".");
    System.out.println("Radius of the Circumscribed Circle = " + circRadius + ".");
    System.out.println("Radius of the Inscribed Circle = " + inscRadius + ".");
  }
  public static boolean checkValid(int a, int b, int c) {
    boolean isValid = true;
    int[] minToMaxArr = minToMax(a, b, c);
    if (a * b * c == 0) { //cannot have side length 0
      isValid = false;
    }
    else if (a < 0 || b < 0 || c < 0) { //cannot have negative side length
      isValid = false;
    }
    else if (minToMaxArr[0] + minToMaxArr[1] <= minToMaxArr[2]) { //according to Triangle Inequality Theorem
      isValid = false;
    }
    return isValid;
  }
  public static String sideClass(int a, int b, int c) {
    int sideClass = 1; //1 = scalene, 2 = isosceles, 3 = equilateral
    String output = "scalene";
    if (a == b && b == c)
      sideClass = 3;
    else if (a == b || b == c || a == c)
      sideClass = 2;
    switch (sideClass) { //case 1: scalene already handled by default (initially intialized as scalene)
      case 2: output = "isosceles";
              break;
      case 3: output = "equilateral";
              break;
    }
    return output;
  }
  public static String angleClass(double[] angles) {
    String angleClass;
    if (angles[3] == (Math.PI/2)) 
      angleClass = "right";
    else if (angles[3] > (Math.PI/2))
      angleClass = "obtuse";
    else 
      angleClass = "acute";
    return angleClass;
  }
  public static double[] findAngles(int a, int b, int c) {
    double[] angles = new double[4]; // {angle A, angle B, angle C, max Angle)}
    double A, B, C, maxAngle;
    A = Math.acos((Math.pow(a, 2) - Math.pow(b, 2) - Math.pow(c, 2)) / (-2 * b * c));
    B = Math.acos((Math.pow(b, 2) - Math.pow(a, 2) - Math.pow(c, 2)) / (-2 * a * c));
    C = Math.acos((Math.pow(c, 2) - Math.pow(a, 2) - Math.pow(b, 2)) / (-2 * a * b));
    maxAngle = Math.max(A, B);
    maxAngle = Math.max(maxAngle, C); //Math.max can only handle 2 parameters at a time
    angles[0] = A;
    angles[1] = B;
    angles[2] = C;
    angles[3] = maxAngle;
    return angles;
  }
  public static double calcArea(int[] sides) {
    double s = (sides[0] + sides[1] + sides[2])/2.0;
    double area = Math.sqrt(s*(s-sides[0])*(s-sides[1])*(s-sides[2])); //Heron's Formula
    return area;
  }
  public static double calcRadCirc(int[] sides, double area) {
    return (sides[0]*sides[1]*sides[0])/(4*area);
  }
  public static double calcRadInsc(int perimeter, double area) {
    return (2*area)/perimeter;
  }
  public static int[] minToMax(int a, int b, int c) { //used in Triangle inequality theorem
    int[] minToMax = new int[3];
    int min, max;
    max = Math.max(a, b);
    max = Math.max(max, c);
    min = Math.min(a, b);
    min = Math.min(min, c);
    minToMax[2] = max;
    minToMax[0] = min;
    minToMax[1] = a + b + c - max - min; //it's the last value left
    return minToMax;
  }
  public static double roundThreeDec(double n) {
    return Math.round(n*1000)/1000.0;
  }
}
