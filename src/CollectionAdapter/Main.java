package CollectionAdapter;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Выберите тип коллекции:");
        System.out.println("1. LinkedList");
        System.out.println("2. HashSet");
        System.out.println("3. LinkedHashSet");
        System.out.println("4. TreeSet");
        System.out.print("Ваш выбор: ");

        int collectionType = Integer.parseInt(scanner.nextLine());
        CollectionManager collectionManager = new CollectionManager(collectionType);

        System.out.println("\nВведите числа для добавления в коллекцию:");
        System.out.print("Введите число: ");
        String input = scanner.nextLine();

        try {
            int number = Integer.parseInt(input);
            collectionManager.addElement(number);
            System.out.println("Число " + number + " добавлено в коллекцию.");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка! Пожалуйста, введите целое число или 'стоп' для завершения.");
        }

        while (true) {
            MenuManager.showMenu();
            System.out.print("Выберите пункт меню: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    collectionManager.addElementFromInput(scanner);
                    break;
                case "2":
                    collectionManager.removeElement(scanner);
                    break;
                case "3":
                    collectionManager.showCollection();
                    break;
                case "4":
                    collectionManager.checkValue(scanner);
                    break;
                case "5":
                    collectionManager.replaceValue(scanner);
                    break;
                case "6":
                    collectionManager.showCollectionType();
                    break;
                case "0":
                    System.out.println("Программа завершена.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }
}