package com.checkout.payment.gateway.util;

import static java.lang.System.out;

public class Print {

  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_GREEN = "\u001B[32m";
  public static final String ANSI_BLUE = "\u001B[34m";
  public static final String ANSI_PURPLE = "\u001B[35m";


  public static void print(String message) {
    out.println(message);
  }

  public static void print(String message, Object... args) {
    out.printf(message, args);
  }

  public static void section(String section) {
    print("");
    printPurple("--------------------------------------------------");
    printPurple(section);
    printPurple("--------------------------------------------------");
  }

  public static void printRed(String message) {
    print(ANSI_RED + message + ANSI_RESET);
  }

  public static void printGreen(String message) {
    print(ANSI_GREEN + message + ANSI_RESET);
  }

  public static void printBlue(String message) {
    print(ANSI_BLUE + message + ANSI_RESET);
  }

  public static void printPurple(String message) {
    print(ANSI_PURPLE + message + ANSI_RESET);
  }

}
