import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Scanner;

public class FleetCarrierCalculator {
    private static final long CURRENT_CREDITS = 7379856856L;
    private static final long TRITUM_TONNES = 5000;
    private static final long TRITIUM_COST = TRITUM_TONNES * 50778;

    private static final long CARRIER_COST = 5350000000L;
    private static final long CREDITS_DUMPED_INTO_CARRIER = 520000000L; // 520 million
    private static final long TOTAL_COST = TRITIUM_COST + CARRIER_COST + CREDITS_DUMPED_INTO_CARRIER;
    private static final long CREDITS_LEFT_OVER = CURRENT_CREDITS - TOTAL_COST;

    private static final Scanner scanner = new Scanner(System.in);

    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_LIGHT_BLUE = "\u001B[94m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) {
        boolean restartProgram = true;
        while (restartProgram) {
            runProgram();
            restartProgram = askToRestart();
        }
        scanner.close();
    }

    private static void runProgram() {
        NumberFormat formatter = new DecimalFormat("#,###");
        System.out.println(ANSI_RED + "[UPDATE CREDITS AFTER SELLING DATA]" + ANSI_RESET);
        System.out.println(ANSI_RED + "[CHECK TRITIUM PRICES]" + ANSI_RESET);

        clearConsole();
        System.out.println("-------------------------------------");
        System.out.println("Current Credits: " + ANSI_YELLOW + formatter.format(CURRENT_CREDITS) + ANSI_RESET);
        System.out.println("-------------------------------------");
        System.out.println("\t Carrier Cost: " + ANSI_RED + formatter.format(CARRIER_COST) + ANSI_RESET);
        System.out.println("\t\t Base Price: " + ANSI_RED + "5,000,000,000" + ANSI_RESET);
        System.out.println("\t\t Universal Cartographics: " + ANSI_RED + "150,000,000" + ANSI_RESET);
        System.out.println("\t\t Vista Genomics: " + ANSI_RED + "150,000,000" + ANSI_RESET);
        System.out.println("\t\t Repair: " + ANSI_RED + "50,000,000" + ANSI_RESET);
        System.out
                .println("\t\t Credits Dumped Into Carrier: " + ANSI_RED + formatter.format(CREDITS_DUMPED_INTO_CARRIER)
                        + ANSI_RESET);
        System.out.println("\t Tritium Cost: " + ANSI_RED + formatter.format(TRITIUM_COST) + ANSI_RESET
                + "\t Tritium Tonnes: " + ANSI_LIGHT_BLUE + formatter.format(TRITUM_TONNES) + ANSI_RESET);

        System.out.println("Total Cost: " + ANSI_RED + formatter.format(TOTAL_COST) + ANSI_RESET);
        System.out.println("-------------------------------------");
        System.out.println(
                "Credits Left Over: " + ANSI_YELLOW + formatter.format(CREDITS_LEFT_OVER) + ANSI_RESET);
        System.out.println("-------------------------------------");
        System.out.println("Weekly Upkeep: " + ANSI_GREEN + "9,850,000" + ANSI_RESET);
        System.out.println("Monthly Upkeep: " + ANSI_GREEN + "42,214,285" + ANSI_RESET);
        System.out.println("Yearly Upkeep: " + ANSI_GREEN + "513,607,142" + ANSI_RESET);
        System.out.println("-------------------------------------");
    }

    private static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (final Exception e) {
            System.out.println("Error clearing console: " + e.getMessage());
        }
    }

    private static boolean askToRestart() {
        System.out.print("Press 0 to close: ");
        String input = scanner.nextLine().trim();
        if (input.equals("0")) {
            System.out.println("Closing program...");
            return false;
        } else {
            System.out.println("Invalid input.");
            return askToRestart();
        }
    }
}
