package org.example.casino.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Profile("init")
public class InitDataService {

    @Value("${init.file-name}")
    private String filepath;

    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void initDataSql() throws IOException {
        String sqlQuery = readFile(filepath);
        jdbcTemplate.execute(sqlQuery);
    }

    private String readFile(String filePath) throws IOException {
        StringBuilder result = new StringBuilder();
        try (FileReader fileReader = new FileReader(filePath)) {
            while (fileReader.ready()) {
                char symbol = (char) fileReader.read();
                result.append(symbol);
            }
        } catch (IOException e) {
            throw new IOException("bad file path");
        }
        return result.toString();
    }

    //вой InitDataService выглядит структурировано и решает задачу инициализации базы данных SQL-скриптом, что удобно при начальной настройке приложения. Тем не менее, есть несколько предложений по улучшению кода.
    //
    //1. Упрощение чтения файла
    //Чтение файла посимвольно можно заменить на более оптимизированный и читаемый способ с использованием Files.readString() из java.nio.file.Files. Это сделает код компактнее и улучшит производительность.
    //
    //2. Улучшение обработки исключений
    //В catch блоке readFile() выбрасывается IOException с фиксированным сообщением "bad file path". Чтобы иметь больше информации об ошибке, лучше передать оригинальное сообщение исключения.
    //
    //3. Логирование
    //Добавление логирования может помочь отследить, что скрипт инициализации успешно выполнен, или отловить потенциальные ошибки.
    //
    //Рекомендации
    //Вот улучшенный вариант с учетом вышесказанного:
    //
    //java
    //Копировать код
    //import jakarta.annotation.PostConstruct;
    //import lombok.RequiredArgsConstructor;
    //import org.springframework.beans.factory.annotation.Value;
    //import org.springframework.context.annotation.Profile;
    //import org.springframework.jdbc.core.JdbcTemplate;
    //import org.springframework.stereotype.Service;
    //import java.io.IOException;
    //import java.nio.file.Files;
    //import java.nio.file.Path;
    //import java.util.logging.Logger;
    //
    //@Service
    //@RequiredArgsConstructor
    //@Profile("init")
    //public class InitDataService {
    //
    //    private static final Logger logger = Logger.getLogger(InitDataService.class.getName());
    //
    //    @Value("${init.file-name}")
    //    private String filepath;
    //
    //    private final JdbcTemplate jdbcTemplate;
    //
    //    @PostConstruct
    //    public void initDataSql() {
    //        try {
    //            String sqlQuery = readFile(filepath);
    //            jdbcTemplate.execute(sqlQuery);
    //            logger.info("SQL initialization completed successfully.");
    //        } catch (IOException e) {
    //            logger.severe("Error reading SQL file: " + e.getMessage());
    //            throw new RuntimeException("Failed to initialize data", e);
    //        }
    //    }
    //
    //    private String readFile(String filePath) throws IOException {
    //        return Files.readString(Path.of(filePath));
    //    }
    //}
    //Преимущества улучшенного кода:
    //Чтение файла стало проще с помощью Files.readString().
    //Более подробная обработка исключений с передачей оригинального сообщения и выбросом RuntimeException в случае ошибки.
    //Логирование добавлено для отслеживания статуса инициализации данных.
    //Этот вариант делает код более читаемым и эффективным для инициализации данных в базе при старте приложения.
    //
    //
    //Твой InitDataService выглядит достаточно хорошо, но есть пара моментов, которые можно улучшить для большей надежности и читаемости. Вот несколько рекомендаций:
    //
    //Рекомендации по улучшению
    //Упрощение метода чтения файла:
    //
    //Вместо того чтобы читать по одному символу, можно использовать BufferedReader, который более эффективен и упрощает код.
    //Логгирование:
    //
    //Добавь логгирование, чтобы фиксировать начало и завершение процесса инициализации, а также возможные ошибки. Это упростит отладку и контроль выполнения.
    //Обработка исключений:
    //
    //Сейчас IOException перехватывается и выбрасывается с общей ошибкой "bad file path". Было бы полезно сохранить сообщение исключения, чтобы лучше понимать, что именно пошло не так.
    //Ресурсоемкие SQL-запросы:
    //
    //Убедись, что SQL-запросы в файле не слишком тяжелые, иначе выполнение их сразу при инициализации может замедлить запуск приложения. Если это может быть проблемой, подумай о разделе данных на несколько файлов или добавлении более гибкой логики выполнения.
    //Разделение логики чтения SQL-запросов и их выполнения:
    //
    //Если приложение планируется расширять, возможно, имеет смысл выделить логику чтения SQL в отдельный сервис, чтобы поддерживать ее отдельно от InitDataService.
}

