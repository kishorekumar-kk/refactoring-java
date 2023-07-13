package main.utils;

import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

/**
 * Class responsible for taking and parsing
 * user input from console
 */
public class IOUtils {
    private final Scanner scanner;

    public static final Integer OPERATION_RETRIES = 3;
    private static IOUtils instance;

    public static synchronized IOUtils getInstance() {
        if(instance == null) {
            instance = new IOUtils();
        }
        return instance;
    }

    private IOUtils() {
        this.scanner = new Scanner(System.in);
    }

    public Optional<Integer> getNumericInput(int attempt) {
        Integer numericInput = null;
        try {
            Optional<String> input = getStringInput(attempt, false);
            numericInput = Integer.valueOf(input.orElseThrow(NumberFormatException::new));
        } catch (NumberFormatException | InputMismatchException exception) {
            attempt++;
            if (attempt <= OPERATION_RETRIES) {
                System.out.printf("Please enter valid numeric value; %d attempts remaining%n", OPERATION_RETRIES + 1 - attempt);
                getNumericInput(attempt);
            }
        }
        if (attempt == OPERATION_RETRIES) {
            return Optional.empty();
        }
        return Optional.ofNullable(numericInput);
    }

    public Optional<String> getStringInput(int attempt, boolean suppressException) {
        String input;
        input = scanner.nextLine();
        attempt++;
        if (attempt <= OPERATION_RETRIES && input.isEmpty()) {
            System.out.printf("Please enter valid non empty string; %d attempts remaining%n", OPERATION_RETRIES + 1 - attempt);
            getStringInput(attempt, suppressException);
        }
        if (!suppressException && attempt == OPERATION_RETRIES) {
            throw new InputMismatchException("Input not valid");
        }
        return Optional.ofNullable(input);
    }
}
