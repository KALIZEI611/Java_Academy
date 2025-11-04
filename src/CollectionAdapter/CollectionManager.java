package CollectionAdapter;

import java.util.*;
import java.util.List;

public class CollectionManager {
    private Collection<Integer> collection;
    private String collectionType;
    private boolean isList; // Для определения, является ли коллекция списком

    public CollectionManager(int type) {
        switch (type) {
            case 1:
                collection = new LinkedList<>();
                collectionType = "LinkedList";
                isList = true;
                break;
            case 2:
                collection = new HashSet<>();
                collectionType = "HashSet";
                isList = false;
                break;
            case 3:
                collection = new LinkedHashSet<>();
                collectionType = "LinkedHashSet";
                isList = false;
                break;
            case 4:
                collection = new TreeSet<>();
                collectionType = "TreeSet";
                isList = false;
                break;
            default:
                collection = new ArrayList<>();
                collectionType = "ArrayList";
                isList = true;
        }
        System.out.println("Создана коллекция: " + collectionType);
    }

    // Добавить элемент
    public void addElement(int number) {
        collection.add(number);
    }

    // Добавить элемент с вводом
    public void addElementFromInput(Scanner scanner) {
        System.out.print("Введите число для добавления: ");
        try {
            int number = Integer.parseInt(scanner.nextLine());
            if (collection.add(number)) {
                System.out.println("Число " + number + " успешно добавлено в коллекцию.");
            } else {
                System.out.println("Число " + number + " уже существует в коллекции (актуально для Set).");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка! Пожалуйста, введите целое число.");
        }
    }

    // Удалить элемент
    public void removeElement(Scanner scanner) {
        if (collection.isEmpty()) {
            System.out.println("Коллекция пуста. Нечего удалять.");
            return;
        }

        System.out.print("Введите число для удаления: ");
        try {
            int number = Integer.parseInt(scanner.nextLine());
            if (collection.remove(number)) {
                System.out.println("Число " + number + " успешно удалено из коллекции.");
            } else {
                System.out.println("Число " + number + " не найдено в коллекции.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка! Пожалуйста, введите целое число.");
        }
    }

    // Показать коллекцию
    public void showCollection() {
        if (collection.isEmpty()) {
            System.out.println("Коллекция пуста.");
        } else {
            System.out.println("Содержимое коллекции (" + collectionType + "): " + collection);

            // Дополнительная информация для разных типов коллекций
            if (collection instanceof List) {
                System.out.println("Размер: " + collection.size() + " элементов");
            } else if (collection instanceof Set) {
                Set<Integer> set = (Set<Integer>) collection;
                System.out.println("Размер: " + set.size() + " уникальных элементов");

                if (collection instanceof TreeSet) {
                    TreeSet<Integer> treeSet = (TreeSet<Integer>) collection;
                    System.out.println("Минимальный элемент: " + treeSet.first());
                    System.out.println("Максимальный элемент: " + treeSet.last());
                }
            }
        }
    }

    // Проверить наличие значения
    public void checkValue(Scanner scanner) {
        System.out.print("Введите число для проверки: ");
        try {
            int number = Integer.parseInt(scanner.nextLine());
            if (collection.contains(number)) {
                System.out.println("Число " + number + " найдено в коллекции.");
            } else {
                System.out.println("Число " + number + " не найдено в коллекции.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка! Пожалуйста, введите целое число.");
        }
    }

    // Заменить значение (только для List)
    public void replaceValue(Scanner scanner) {
        if (!isList) {
            System.out.println("Операция замены доступна только для List коллекций (LinkedList).");
            System.out.println("Для Set коллекций используйте удаление и добавление.");
            return;
        }

        if (collection.isEmpty()) {
            System.out.println("Коллекция пуста. Нечего заменять.");
            return;
        }

        List<Integer> list = (List<Integer>) collection;

        System.out.print("Введите число, которое нужно заменить: ");
        try {
            int oldValue = Integer.parseInt(scanner.nextLine());

            if (list.contains(oldValue)) {
                System.out.print("Введите новое число: ");
                int newValue = Integer.parseInt(scanner.nextLine());

                int index = list.indexOf(oldValue);
                list.set(index, newValue);
                System.out.println("Число " + oldValue + " успешно заменено на " + newValue + ".");
            } else {
                System.out.println("Число " + oldValue + " не найдено в коллекции.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка! Пожалуйста, введите целое число.");
        }
    }

    // Показать тип коллекции
    public void showCollectionType() {
        System.out.println("Тип коллекции: " + collectionType);
        System.out.println("Характеристики:");

        if (collection instanceof LinkedList) {
            System.out.println("- Сохраняет порядок добавления");
            System.out.println("- Разрешает дубликаты");
            System.out.println("- Быстрая вставка/удаление в начале и конце");
        } else if (collection instanceof HashSet) {
            System.out.println("- Не гарантирует порядок");
            System.out.println("- Запрещает дубликаты");
            System.out.println("- Самая быстрая поисковая операция");
        } else if (collection instanceof LinkedHashSet) {
            System.out.println("- Сохраняет порядок добавления");
            System.out.println("- Запрещает дубликаты");
            System.out.println("- Немного медленнее HashSet");
        } else if (collection instanceof TreeSet) {
            System.out.println("- Хранит элементы в отсортированном порядке");
            System.out.println("- Запрещает дубликаты");
            System.out.println("- Элементы автоматически сортируются");
        }

        System.out.println("Текущий размер: " + collection.size() + " элементов");
    }

    // Геттер для типа коллекции
    public String getCollectionType() {
        return collectionType;
    }
}