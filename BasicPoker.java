//NOTE: I HAD TO USE STRINGS TO STORE THE CARDS; this was a requirement for the assignment. I was prohibited from using objects, classes, etc. for this assignment (hence the inefficacy).
import java.util.*;
class Main {
  public static void main(String[] args) {
    welcomeMessage();
    System.out.println("---");
    int numPlayers = getPlayerNumber();
    System.out.println("---");
    int numRounds = getRoundNumber();

    String[] deck = createDeck();
    String[][] hand;
    hand = new String[numPlayers][5];
    int hCounter;
    int bal = 100;
    int bet;
    int winnerIndex;

    for (int round = 1; round <= numRounds; round++) {
      deck = shuffleDeck(deck);
      hCounter = 0;
      bet = 0;
      System.out.println("------------------------");
      System.out.println("Round " + round + " of " + numRounds);
      displayBalance(bal);
      System.out.println();
      for (int i = 0; i < hand.length; i++) { //deal hands
        hand[i] = dealHand(deck, hCounter);
        hCounter += 5;
        hand[i] = sort(hand[i]);      
      }
      displayHand(hand, 0);
      bet = getBet();
      System.out.println("---");
      for (int j = 1; j < hand.length; j++) {
        displayHand(hand, j);
      }
      winnerIndex = getWinningHand(hand);
      System.out.println("Player " + (winnerIndex+1) + " wins! ");
      if (winnerIndex == 0) {
        if (bet == 0) {
          bal -= 5;
          System.out.print("However, because you folded, you do not gain any money and you lose $5. ");
        }
        else {
          bal += bet * (numPlayers-1);
          System.out.print("You won $" + (bet * (numPlayers-1)) + "! ");
        }
      }
      else {
        if (bet == 0) {
          bal -= 5;
          System.out.print("Because you folded, you only lose $5.  ");
        }
        else {
          bal -= bet;
          System.out.print("You lost $" + bet + "! ");
        }
      }
      displayBalance(bal);
    }
    System.out.println("------------------------");
    System.out.print("GG! You had net winnings of $" + (bal-100) + " after " + numRounds + " rounds.");
  }
  public static void welcomeMessage() {
    Scanner in = new Scanner(System.in);
    boolean askAgain = true;
    char input;
    System.out.println("------------------------");
    System.out.println("Welcome to DC Poker!");
    while (askAgain) {
      System.out.println("Would you like to read the rules? (Y/N)");
      input = in.next().toUpperCase().charAt(0);
      if (input == 'Y') {
        displayRules();
        askAgain = false;
      }
      else if (input == 'N') 
        askAgain = false;
    }
  }
  public static void displayRules() {
    System.out.println("------------------------");
    System.out.println("Rules: ");
    System.out.println("Every player is dealt 5 cards. You can only see the 5 cards that you have been dealt. You can then bet an integer amount of money between $10 and $100 or choose to fold.");
    System.out.println();
    System.out.println("After you choose your action, everyone's hand is revealed, and the player with the greatest hand wins.");
    System.out.println();
    System.out.println("If you bet money and you win, you gain that amount of money from every other player; if you bet money and you lose, you lose that amount of money. In the case of ties, the lowest number player wins (Player 2 would win against Player 3). However, if you folded, then you forfeit your hand and you lose $5 and you don't gain any money even if you did have the greatest hand.");
    System.out.println();
    System.out.println("Please note that it is possible to go into debt (negative balance), but you may still play on.");
  }
  public static int getPlayerNumber() {
    Scanner in = new Scanner(System.in);
    int numPlayers = 0;
    System.out.println("Please enter the number of players. (2 to 10 players)");
    try {
      numPlayers = in.nextInt(); //2 to 10 players
      if (numPlayers <= 1 || numPlayers > 10) {
        System.out.println("Please enter a number between 2 and 10.");
        System.out.println();
        return getPlayerNumber();
      }
      else
        return numPlayers;
    }
    catch (InputMismatchException e) {
      System.out.println("Invalid input! Please input an integer.");
      System.out.println();
      return getPlayerNumber();
    }
  }
  public static int getRoundNumber() {
    Scanner in = new Scanner(System.in);
    int numRounds = 0;
    System.out.println("How many rounds would you like to play? (1 to 100) rounds.");
    try {
      numRounds = in.nextInt();
      if (numRounds < 1 || numRounds > 100) {
        System.out.println("Please enter a number between 1 and 100.");
        System.out.println();
        return getRoundNumber();
      }
      else
        return numRounds;
    }
    catch (InputMismatchException e) {
      System.out.println("Invalid input! Please input an integer.");
      System.out.println();
      return getRoundNumber();
    }
  }
  public static int getBet() {
    System.out.println("How much would you like to bet ($10 to $100)? Enter 0 to fold.");
    Scanner in = new Scanner(System.in);
    int bet;
    try {
      bet = in.nextInt();
      if ((bet < 10 && bet != 0) || bet > 100) {
        System.out.println("Please enter 0 or a number between 10 and 100.");
        System.out.println();
        return getBet();
      }
      else
        return bet;
    }
    catch (InputMismatchException e) {
      System.out.println("Invalid input! Please input an integer.");
      System.out.println();
      return getBet();
    }
  }
  public static void displayBalance(int balance) {
    System.out.println("Your current balance: $" + balance + ".");
  }
  public static void displayHand(String[][] hand, int i) {
    String msg = "Player " + (i+1) + "\'s Hand: ";
    if (i == 0)
      msg = "Player 1's (your) Hand: ";
    System.out.println(msg);
    System.out.println(Arrays.toString(hand[i]));
    System.out.println("Rank: " + rankToStr(identifyHand(hand[i])[0]));
    //System.out.println("Rank: " + rankToStr(identifyHand(hand[i])[0]) + ": " + identifyHand(hand[i])[1]);
    System.out.println();
  }
  public static String[] createDeck() {
    String[] deck = new String[52];
    String[] suits = {"Spades", "Clubs", "Hearts", "Diamonds"};
    String[] numbers = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
    int i = 0;
    for (int s = 0; s < suits.length; s++) {
      for (int n = 0; n < numbers.length; n++) {
        deck[i] = numbers[n] + " of " + suits[s];
        i++;
      }
    }
    return deck;
  }
  public static String[] switchCards(int a, int b, String[] cards) {
    String temp = cards[a];
    cards[a] = cards[b];
    cards[b] = temp;
    return cards;
  }
  public static String[] shuffleDeck(String[] deck) {
    for (int i = 0; i < rng(60, 180); i++) {
      deck = switchCards(rng(0, 51), rng(0, 51), deck);
    }
    return deck;
  }
  public static String draw(String[] deck, int index) {
    return deck[index];
  }
  public static String[] dealHand(String[] deck, int startInd) {
    String hand[] = new String[5];
    for (int i = 0; i < 5; i++) {
      hand[i] = draw(deck, i + startInd);
    }
    return hand;
  }
  public static String[] sort(String[] hand) {
    boolean isSorted = true;
    int nTemp;
    String sTemp;
    String[] suits = getSuits(hand);
    int[] numbers = getNumbers(hand);
    do {
      isSorted = true;
      for (int i = 0; i < hand.length-1; i++) {
        if (numbers[i] > numbers[i+1]) {
          nTemp = numbers[i];
          numbers[i] = numbers[i+1];
          numbers[i+1] = nTemp;

          sTemp = suits[i];
          suits[i] = suits[i+1];
          suits[i+1] = sTemp;

          isSorted = false; //if a change was made, then it was not completely sorted
        }
      }
    }
    while (!isSorted);
    return combineProps(numbers, suits);
  }
  public static boolean sameSuits(String[] suits) {
    for (int i = 0; i < suits.length-1; i++) {
      if (!Objects.equals(suits[i], suits[i+1]))
        return false;
    }
    return true;
  }
  public static int[] repeats(int[] numbers) {
    int[] repeats = {0, 0, 0, 0}; //{# of pairs, three-of-a-kind, four-of-a-kind, card number}
    switch (numbers.length) { //switch here to allow for recursion
      case 5: //Four-of-a-kind
              if (numbers[0] == numbers [3]) {
              repeats[2] = 1;
              repeats[3] = numbers[0];
              }
              else if (numbers[4] == numbers[1]) {
                repeats[2] = 1;
                repeats[3] = numbers[4];
              }
              else {
                //Three-of-a-kind / Full House
                if (numbers[0] == numbers[2]) {
                  repeats[1] = 1;
                  repeats[3] = numbers[0];
                  if (numbers[3] == numbers[4])
                    repeats[0] = 1; //full house
                }
                else if (numbers[4] == numbers[2]) {
                  repeats[1] = 1;
                  repeats[3] = numbers[4];
                  if (numbers[0] == numbers[1])
                    repeats[0] = 1; //full house
                }
                //Pairs
                else {
                  for (int i = 0; i < numbers.length-1; i++) {
                    if (numbers[i] == numbers[i+1]) {
                      repeats[0]++;
                      repeats[3] = numbers[i];
                    }
                  }
                }
              }
              break;
      case 4: //Three-of-a-kind
              if (numbers[0] == numbers[2]) {
                repeats[1] = 1;
                repeats[3] = numbers[0];
              }
              else if (numbers[3] == numbers[1]) {
                repeats[1] = 1;
                repeats[3] = numbers[3];
              }
              //Pairs
              else {
                for (int i = 0; i < numbers.length-1; i++) {
                  if (numbers[i] == numbers[i+1]) {
                    repeats[0]++;
                    repeats[3] = numbers[i];
                  }
                }
              }
              break;
      case 3: //Three-of-a-kind
              if (numbers[0] == numbers[2]) {
                repeats[1] = 1;
                repeats[3] = numbers[0];
              }
              //Pairs
              else {
                for (int i = 0; i < numbers.length-1; i++) {
                  if (numbers[i] == numbers[i+1]) {
                    repeats[0]++;
                    repeats[3] = numbers[i];
                  }
                }
              }
              break;
      case 2: //Pairs
              for (int i = 0; i < numbers.length-1; i++) {
                if (numbers[i] == numbers[i+1]) {
                  repeats[0]++;
                  repeats[3] = numbers[i];
                }
              }
              break;
    }//switch
    return repeats;
  }
  public static boolean[] isSequence(int[] numbers) {//[isSequential, isRoyalSequential]
    boolean[] isSequence = {true, false}; 
    int[] otherNumbers = new int[numbers.length-1];
    if (numbers[0] == 10 && numbers.length == 5) { //potential royal flush
      for (int i = 0; i < otherNumbers.length; i++) {
        otherNumbers[i] = numbers[i+1];
      }
      if (isSequence(otherNumbers)[0] && otherNumbers[0] == 11) //the other numbers are sequential from Queen
        isSequence = new boolean[]{true, true};
    }
    else if (!isSequence[1]) {
      for (int i = 0; i < numbers.length-1; i++) {
        if (numbers[i] + 1 != numbers[i+1]) {
          isSequence = new boolean[]{false, false};
        }
      }
      //initialized as {true, false} already so no need to change it 
    }
    return isSequence;
  }
  public static int[] identifyHand(String[] hand) { //[points, card number]
    //POINT SYSTEM: Top 9 and if tied, then check the card number
    int[] points = {0, 0};
    String[] suits = getSuits(hand);
    int[] numbers = getNumbers(hand);
    int[] repeats = repeats(numbers);
    boolean[] isSequence = isSequence(numbers);
    int max = numbers[numbers.length-1];
    //Determine the poker hand
    if (isSequence[0]) { //there is a sequence
      if (sameSuits(suits)) {
        if (isSequence[1]) 
          points[0] = 9; //royal flush
        else 
          points[0] = 8; //straight flush
      }
      else
        points = new int[]{4, numbers[0]}; //straight
    }
    else if (repeats[0] + repeats[1] + repeats[2] != 0) { //there is a repeat and not a sequence
      if (repeats[2] != 0) 
        points[0] = 7; //four-of-a-kind
      else if (repeats[1] != 0) {
        if (repeats[0] != 0)
          points[0] = 6; //Full House
        else 
          points[0] = 3;
      }
      else if (repeats[0] == 2) {
        points[0] = 2; //2 Pairs
      }
      else if (repeats[0] == 1) {
        points[0] = 1; //Pair
      }
      points[1] = repeats[3];
    }
    else { //just the single highest card
      points[1] = max; //points[0] stays zero
    }
    if (sameSuits(suits) && points[0] < 5)
      points = new int[]{5, max}; //flush
    return points;
  }
  public static int greaterThan(String[] hand1, String[] hand2) { //0 for false, 1 for tie, 2 for true
    int[] idHand1 = identifyHand(hand1);
    int[] idHand2 = identifyHand(hand2);
    int value = -1; 
    if (idHand1[0] > idHand2[0]) {
      value = 2;
    }
    else if (idHand1[0] < idHand2[0]) {
      value = 0;
    }
    else if (idHand1[0] == idHand2[0]) {
      if (idHand1[1] > idHand2[1]) 
        value = 2;
      else if (idHand1[1] < idHand2[1]) 
        value = 0;
      else 
        value = 1;
    }
    if (value == 1 && hand1.length != 1 && idHand1[0] != 9 && idHand1[0] != 8 && idHand1[0] != 4) { //allows for recursion. Check if both tied royal flush / straight flush / straight before recurring
      return greaterThan(nextHandCycle(hand1), nextHandCycle(hand2));
    }
    else 
      return value;
  }
  public static int getWinningHand(String[][] hand) {
    int greatestIndex = 0;
    for (int i = 1; i < hand.length; i++) {
      if (greaterThan(hand[greatestIndex], hand[i]) == 0) {
        greatestIndex = i;
      }
    }
    return greatestIndex;
  }
  public static String[] nextHandCycle(String[] hand) {
    int[] nums = getNumbers(hand);
    String[] strNums = new String[nums.length];
    for (int i = 0; i < strNums.length; i++) {
      strNums[i] = intToStr(nums[i]);
    }
    String[] suits = getSuits(hand);
    int[] idHand = identifyHand(hand); //{score, highest card value}
    int cardsToRemove = 1;
    switch (idHand[0]) {
      case 7: cardsToRemove = 4;
              break;
      case 6: cardsToRemove = 3;
              break;
      case 3: cardsToRemove = 3;
              break;
      case 2: cardsToRemove = 2;
              break;
      case 1: cardsToRemove = 2;
              break;
    }
    String[] nextHand = new String[hand.length-cardsToRemove];
    int c = 0;
    for (int i = 0; i < hand.length; i++) {
      if (nums[i] != idHand[1]) {
          // System.out.println("c: " + c);
          // System.out.println("i: " + i);
          nextHand[c] = intToStr(nums[i]) + " of " + suits[i];
          c++;
      }
    }
    return nextHand;
  }
  public static int rng(int min, int max) {
    int range = max - min;
    return (int)Math.floor(Math.random()*(range+1) + min);
  }
  public static int strToInt(String str) {
    int num = 0;
    if (Objects.equals(str, "Two"))
      num =  2;
    else if (Objects.equals(str, "Three"))
      num =  3;
    else if (Objects.equals(str, "Four"))
      num =  4;
    else if (Objects.equals(str, "Five"))
      num =  5;
    else if (Objects.equals(str, "Six"))
      num =  6;
    else if (Objects.equals(str, "Seven"))
      num =  7;
    else if (Objects.equals(str, "Eight"))
      num =  8;
    else if (Objects.equals(str, "Nine"))
      num =  9;
    else if (Objects.equals(str, "Ten"))
      num =  10;
    else if (Objects.equals(str, "Jack"))
      num =  11;
    else if (Objects.equals(str, "Queen"))
      num =  12;
    else if (Objects.equals(str, "King"))
      num =  13;
    else if (Objects.equals(str, "Ace"))
      num = 14;
    return num;
  }
  public static String intToStr(int num) {
    String str = "";
    switch (num) {
      case 2: str = "Two";
              break;
      case 3: str = "Three";
              break;
      case 4: str = "Four";
              break;
      case 5: str = "Five";
              break;
      case 6: str = "Six";
              break;
      case 7: str = "Seven";
              break;
      case 8: str = "Eight";
              break;
      case 9: str = "Nine";
              break;
      case 10: str = "Ten";
              break;
      case 11: str = "Jack";
              break;
      case 12: str = "Queen";
              break;
      case 13: str = "King";
              break;
      case 14: str = "Ace";
              break;
    }
    return str;
  }
  public static String rankToStr(int num) {
    String str = "";
    switch (num) {
      case 0: str = "High Card";
              break;
      case 1: str = "Pair";
              break;
      case 2: str = "Two Pair";
              break;
      case 3: str = "Three of a Kind";
              break;
      case 4: str = "Straight";
              break;
      case 5: str = "Flush";
              break;
      case 6: str = "Full House";
              break;
      case 7: str = "Three of a Kind";
              break;
      case 8: str = "Straight Flush";
              break;
      case 9: str = "Royal Flush";
              break;
    }
    return str;
  }
  public static String resultToStr(int value) {
    String output = "Something went wrong!";
    switch (value) {
      case 2: output = "You won!";
              break;
      case 1: output = "You tied!";
              break;
      case 0: output = "You lost!";
              break;
    }
    return output;
  }
  public static String[] combineProps(int[] numbers, String[] suits) {
    String[] cards = new String[numbers.length];
    for (int i = 0; i < numbers.length; i++) {
      cards[i] = intToStr(numbers[i]) + " of " + suits[i];
    }
    return cards;
  }
  public static int[] getNumbers(String[] cards) {
    int[] numbers = new int[cards.length];
    String handStr = "";
    for (int i = 0; i < cards.length; i++) {
      handStr += cards[i] + " ";
    }
    String[] splitHand = handStr.trim().split(" "); //trim() first because there will be trailing space after the last element
    for (int i = 0; i < cards.length; i++) {
      numbers[i] = strToInt(splitHand[i*3]); //starting from the first string, every 3rd string will be the number of the card
    }
    return numbers;
  }
  public static String[] getSuits(String[] cards) {
    String[] suits = new String[cards.length];
    String handStr = "";
    //Find the properties of each card
    for (int i = 0; i < cards.length; i++) {
      handStr += cards[i] + " ";
    }
    String[] splitHand = handStr.trim().split(" ");
    for (int i = 0; i < cards.length; i++) {
      suits[i] = splitHand[i*3 + 2];
    }
    return suits;
  }
}
