package CSV;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainCSV {
    public static void main(String[] args) throws IOException {
        // === ЧТЕНИЕ ===
        List<String[]> data = CSVHandler.openCSV("data.csv");

        System.out.println("📖 Прочитано из CSV:");
        for (String[] strData : data) {
            System.out.println(Arrays.toString(strData));
        }

        // === ЗАПИСЬ ===
        List<String[]> newData =Arrays.asList(
                new String[]{"Имя", "Возраст", "Город"},
                new String[]{"Иван", "25", "Москва"},
                new String[]{"Мария", "30", "Санкт-Петербург"},
                new String[]{"Петр, инженер", "35", "Казань"}  // Запятая в данных!
        );

        CSVHandler.writeCSV("data_copy.csv", newData);
        System.out.println("\n✅ Записано в CSV");

        // === ПРОВЕРКА ===
        List<String[]> check = CSVHandler.openCSV("data_copy.csv");
        System.out.println("\n📖 Проверка записанного:");
        for (String[] strCheck : check) {
            System.out.println(Arrays.toString(strCheck));
        }
    }
}
