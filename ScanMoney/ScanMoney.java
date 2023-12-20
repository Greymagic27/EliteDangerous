import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Scanner;

public class ScanMoney {

    private static final Scanner scanner = new Scanner(System.in);

    private static final String ANSI_LIGHT_BLUE = "\u001B[94m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RED = "\u001B[31m";
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

        long CURRENT_CREDITS = currentCredits();
        long scanValue = getScanValue();
        long payoutAmount = getPayoutAmount();
        long payoutAmountMultiplier = payoutAmount * 5;
        long newCreditsFF = CURRENT_CREDITS + payoutAmountMultiplier + scanValue;
        long newCreditsNONFF = CURRENT_CREDITS + payoutAmount + scanValue;

        clearConsole();
        System.out.println("-------------------------------------");
        System.out.println("Current Credits: " + ANSI_RED + formatter.format(CURRENT_CREDITS) + ANSI_RESET);
        System.out.println("-------------------------------------");
        System.out.println("Total Scan Earnings (FF): " + ANSI_LIGHT_BLUE + "\033[4m"
                + formatter.format(payoutAmountMultiplier + scanValue) + ANSI_RESET);

        System.out.println(
                "\tBiological Scan Earnings: " + ANSI_LIGHT_BLUE + formatter.format(payoutAmountMultiplier)
                        + ANSI_RESET);
        System.out.println("\tPlanetary Scan Earnings: " + ANSI_LIGHT_BLUE + formatter.format(scanValue) + ANSI_RESET);
        System.out.println("New Credit Total: " + ANSI_YELLOW
                + formatter.format(newCreditsFF) + ANSI_RESET);
        System.out.println("-------------------------------------");
        System.out.println("Total Scan Earnings (NON-FF): " + ANSI_LIGHT_BLUE + "\033[4m"
                + formatter.format(scanValue + payoutAmount) + ANSI_RESET);
        System.out.println(
                "\tBiological Scan Earnings: " + ANSI_LIGHT_BLUE + formatter.format(payoutAmount)
                        + ANSI_RESET);
        System.out.println("\tPlanetary Scan Earnings: " + ANSI_LIGHT_BLUE + formatter.format(scanValue) + ANSI_RESET);
        System.out.println("New Credit Total: " + ANSI_YELLOW
                + formatter.format(newCreditsNONFF) + ANSI_RESET);
        System.out.println("-------------------------------------");
    }

    private static long currentCredits() {
        System.out.print("Enter current credits: ");
        String input = scanner.nextLine().replaceAll("\\s+", "");
        input = input.replaceAll(",", "");
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return currentCredits();
        }
    }

    private static long getScanValue() {
        System.out.print("Enter scan value: ");
        String input = scanner.nextLine().replaceAll("\\s+", "");
        input = input.replaceAll(",", "");
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return getScanValue();
        }
    }

    private static long getPayoutAmount() {
        System.out.print("Enter organic scans value: ");
        String input = scanner.nextLine().replaceAll("\\s+", "");
        input = input.replaceAll(",", "");
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return getPayoutAmount();
        }
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
        System.out.print("Press 0 to close or 1 to restart: ");
        String input = scanner.nextLine().trim();
        if (input.equals("0")) {
            System.out.println("Closing program...");
            return false;
        } else if (input.equals("1")) {
            clearConsole();
            return true;
        } else {
            System.out.println("Invalid input. Please enter 0 to close or 1 to restart.");
            return askToRestart();
        }
    }
}