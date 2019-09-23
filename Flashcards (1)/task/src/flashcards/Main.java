package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Main {
    static String card;
    static String definition;
    static String fileName;

    static Scanner scanner = new Scanner(System.in);
    static Map<String, String> cardToDefinition = new LinkedHashMap<>();
    static Map<String, String> definitionToCard = new LinkedHashMap<>();

    static ArrayList<String> logList = new ArrayList<>();
    static Map<String, Integer> hardestCards = new HashMap<>();
    static Integer mistake;

    public static void resetStats() {
        hardestCards = new HashMap<>();
        System.out.println("Card statistics has been reset.\n");
        logList.add("Card statistics has been reset.\n");
    }

    public static void log() {
        System.out.println("File name:");
        logList.add("File name:");

        fileName = scanner.nextLine();
        logList.add(fileName);

        File file = new File(fileName);
        try (PrintWriter printWriter = new PrintWriter(file)) {
            for (String line : logList) {
                printWriter.println(line);
            }
            System.out.println("The log has been saved.\n");
            logList.add("The log has been saved.\n");

        } catch (FileNotFoundException ex) {
            System.out.println("File not found.\n");
            logList.add("File not found.\n");
        }
    }

    public static void hardestCard() {
        if (hardestCards.size() == 0) {
            System.out.println("There are no cards with errors.\n");
            logList.add("There are no cards with errors.\n");
        } else {
            Integer[] values = hardestCards.values().toArray(new Integer[0]);
            Integer maxValue = values[0];
            for (int i = 0; i < values.length; i++) {
                if (values[i] > maxValue) {
                    maxValue = values[i];
                }
            }
            ArrayList<String> keys = new ArrayList<>();
            for (Map.Entry<String, Integer> entry: hardestCards.entrySet()) {
                if (entry.getValue() == maxValue) {
                    keys.add(entry.getKey());
                }
            }
            if (keys.size() == 1) {
                System.out.println("The hardest card is \"" + keys.get(0) + "\". You have " + maxValue + " errors answering it.\n");
                logList.add("The hardest card is \"" + keys.get(0) + "\". You have " + maxValue + " errors answering it.\n");

            } else {
                String line = "";
                for (String word : keys) {
                    line += ("\"" + word + "\", ");
                }
                line = line.substring(0,line.length()-2);
                System.out.println("The hardest cards are " + line + ". You have " + maxValue + " errors answering them.\n");
                logList.add("The hardest cards are " + line + ". You have " + maxValue + " errors answering them.\n");

            }
        }
    }

    public static void add()  {
        System.out.println("The card:");
        logList.add("The card:");

        card = scanner.nextLine();
        logList.add(card);

        if (cardToDefinition.containsKey(card)) {
            System.out.println("The card \"" + card + "\" already exists.\n");
            logList.add("The card \"" + card + "\" already exists.\n");

        } else {
            System.out.println("The definition of the card:");
            logList.add("The definition of the card:");

            definition = scanner.nextLine();
            logList.add(definition);

            if (definitionToCard.containsKey(definition)) {
                System.out.println("The definition \"" + definition + "\" already exists.\n");
                logList.add("The definition \"" + definition + "\" already exists.\n");

            } else {
                cardToDefinition.put(card, definition);
                definitionToCard.put(definition, card);
                System.out.println("The pair (\""+ card + "\":\"" + definition+ "\") has been added.\n");
                logList.add("The pair (\""+ card + "\":\"" + definition+ "\") has been added.\n");

            }
        }
    }

    public static void remove() {
        System.out.println("The card:");
        logList.add("The card:");

        card = scanner.nextLine();
        logList.add(card);

        if (cardToDefinition.containsKey(card)) {
            definitionToCard.remove(cardToDefinition.get(card));
            cardToDefinition.remove(card);
            if (hardestCards.containsKey(card)) {
                hardestCards.remove(card);
            }
            System.out.println("The card has been removed.\n");
            logList.add("The card has been removed.\n");

        } else {
            System.out.println("Can't remove \"" + card + "\": there is no such card.\n");
            logList.add("Can't remove \"" + card + "\": there is no such card.\n");

        }
    }

    public static void importFlashcards(String fileName) {
        File file = new File(fileName);
        int count = 0;
        try (Scanner scannerFile = new Scanner(file)) {
            while (scannerFile.hasNext()) {
                card = scannerFile.nextLine();
                if (scannerFile.hasNext()) {
                    definition = scannerFile.nextLine();
                    if (cardToDefinition.containsKey(card)) {
                        definitionToCard.remove(cardToDefinition.get(card));
                        cardToDefinition.replace(card, definition);
                        definitionToCard.put(definition, card);
                    } else {
                        cardToDefinition.put(card, definition);
                        definitionToCard.put(definition, card);
                    }
                    if (scannerFile.hasNext()) {
                        mistake = Integer.parseInt(scannerFile.nextLine());
                        if (mistake != 0) {
                            hardestCards.put(card, mistake);
                        }
                    }
                    count++;
                }
            }
            System.out.println(count + " cards have been loaded.\n");
            logList.add(count + " cards have been loaded.\n");

        } catch (FileNotFoundException ex) {
            System.out.println("File not found.\n");
            logList.add("File not found.\n");

        }
    }

    public static void exportFlashcards(String fileName) {
        File file = new File(fileName);
        int count = 0;
        try (PrintWriter printWriter = new PrintWriter(file)) {
            for (Map.Entry<String, String> entry : cardToDefinition.entrySet()) {
                printWriter.println(entry.getKey());
                printWriter.println(entry.getValue());
                if (hardestCards.containsKey(entry.getKey())) {
                    printWriter.println(hardestCards.get(entry.getKey()));
                } else {
                    printWriter.println(0);
                }
                count++;
            }
            System.out.println(count + " cards have been saved.\n");
            logList.add(count + " cards have been saved.\n");

        } catch (FileNotFoundException ex) {
            System.out.println("File not found.\n");
            logList.add("File not found.\n");

        }
    }

    public static void ask() {
        if (cardToDefinition.size() > 0) {
            System.out.println("How many times to ask?");
            logList.add("How many times to ask?");

            int numbersAsk = Integer.parseInt(scanner.nextLine());
            logList.add(String.valueOf(numbersAsk));

            String[] cards = new String[cardToDefinition.size()];
            int j = 0;
            for (Map.Entry<String, String> entry : cardToDefinition.entrySet()) {
                cards[j] = entry.getKey();
                j++;
            }
            Random random = new Random();
            for (int i = 0; i < numbersAsk; i++) {
                card = cards[random.nextInt(cards.length)];
                System.out.println("Print the definition of \"" + card + "\":");
                logList.add("Print the definition of \"" + card + "\":");

                String answer = scanner.nextLine();
                logList.add(answer);

                if (cardToDefinition.get(card).equals(answer)) {
                    System.out.println("Correct answer.\n");
                    logList.add("Correct answer.\n");

                } else {
                    if (definitionToCard.containsKey(answer)) {
                        System.out.println("Wrong answer. (The correct one is \"" +
                                cardToDefinition.get(card) +
                                "\", you've just written the definition of \"" +
                                definitionToCard.get(answer) +
                                "\").\n");
                        logList.add("Wrong answer. (The correct one is \"" +
                                cardToDefinition.get(card) +
                                "\", you've just written the definition of \"" +
                                definitionToCard.get(answer) +
                                "\").\n");

                    } else {
                        System.out.println("Wrong answer. (The correct one is \"" +
                                cardToDefinition.get(card) +
                                "\").\n");
                        logList.add("Wrong answer. (The correct one is \"" +
                                cardToDefinition.get(card) +
                                "\").\n");

                    }
                    if (hardestCards.containsKey(card)) {
                        hardestCards.replace(card, hardestCards.get(card) + 1);
                    } else {
                        hardestCards.put(card, 1);
                    }
                }
            }
        }
    }


    public static void main(String[] args) {
            if (args.length == 4) {
                if ("-import".equals(args[0])) {
                    importFlashcards(args[1]);
                } else if ("-import".equals(args[2])) {
                    importFlashcards(args[3]);
                }
            } else if (args.length == 2) {
                if ("-import".equals(args[0])) {
                    importFlashcards(args[1]);
                }
            }
            String action;
            do {
                System.out.println("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
                logList.add("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");

                action = scanner.nextLine();
                logList.add(action);

                if ("add".equals(action)) {
                    add();
                } else if ("remove".equals(action)) {
                    remove();
                } else if ("import".equals(action)) {
                    System.out.println("File name:");
                    logList.add("File name:");

                    fileName = scanner.nextLine();
                    logList.add(fileName);

                    importFlashcards(fileName);
                } else if ("export".equals(action)) {
                    System.out.println("File name:");
                    logList.add("File name:");

                    fileName = scanner.nextLine();
                    logList.add(fileName);

                    exportFlashcards(fileName);
                } else if ("ask".equals(action)) {
                    ask();
                } else if ("log".equals(action)) {
                    log();
                } else if ("hardest card".equals(action)) {
                    hardestCard();
                } else if ("reset stats".equals(action)) {
                    resetStats();
                } else if ("exit".equals(action)) {
                    System.out.println("Bye bye!");
                    logList.add("Bye bye!");
                    if (args.length == 4) {
                        if ("-export".equals(args[0])) {
                            exportFlashcards(args[1]);
                        } else if ("-export".equals(args[2])) {
                            exportFlashcards(args[3]);
                        }
                    } else if (args.length == 2) {
                        if ("-export".equals(args[0])) {
                            exportFlashcards(args[1]);
                        }
                    }
                    break;
                }
            } while (!"exit".equals(action));

    }
}
