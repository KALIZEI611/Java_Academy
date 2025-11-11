package CSV;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CSVHandler {
    public static List<String[]> openCSV(String filepath)
            throws IOException {
        List<String[]> data = new ArrayList<>();

        try (BufferedReader open = new BufferedReader(
                new FileReader(filepath, StandardCharsets.UTF_8)
        )) {
            String str;
            while ((str = open.readLine()) != null) {
                String[] fields = shareCSV(str);
                data.add(fields);
            }
        }

        return data;
    }

    /**
     * Правильное разделение CSV (учитывает кавычки)
     */
    private static String[] shareCSV(String filepath) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean quotes = false;

        for (int i = 0; i < filepath.length(); i++) {
            char symbol = filepath.charAt(i);

            if (symbol == '"') {
                quotes = !quotes;
            } else if (symbol == ',' && !quotes) {
                fields.add(current.toString().trim());
                current = new StringBuilder();
            } else {
                current.append(symbol);
            }
        }

        fields.add(current.toString().trim());
        return fields.toArray(new String[0]);
    }

    /**
     * Запись CSV файла
     */
    public static void writeCSV(String filepath, List<String[]> data)
            throws IOException {
        try (PrintWriter writer = new PrintWriter(
                new BufferedWriter(
                        new FileWriter(filepath, StandardCharsets.UTF_8)
                )
        )) {
            for (String[] str : data) {
                String csv = joinCSV(str);
                writer.println(csv);
            }
        }
    }

    /**
     * Объединение полей в CSV строку
     */
    private static String joinCSV(String[] filepath) {
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < filepath.length; i++) {
            String field = filepath[i];

            // Если поле содержит запятую или кавычку, оборачиваем в кавычки
            if (field.contains(",") || field.contains("\"") ||
                    field.contains("\n")) {
                res.append("\"")
                        .append(field.replace("\"", "\"\""))
                        .append("\"");
            } else {
                res.append(field);
            }

            if (i < filepath.length - 1) {
                res.append(";");
            }
        }

        return res.toString();
    }
}
