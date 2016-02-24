package pack.test.sql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

    public static final String DB_URL = "jdbc:h2:~/test";
    public static final String LOGIN = "sa";
    public static final String PASSWORD = "";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_TEXT = "text";
    public static final String H2_DRIVHER = "org.h2.Driver";
    public static final String EXIT = "-stop";
    public static final String SHOW_ALL = "-all";
    public static final String CURRENT_AUTHOR = "SELECT * FROM messages WHERE author = ?;";
    public static final String MESSAGES_FROM_TABLE = "SELECT * FROM messages;";
    private static final String AUTHOR_ADD_VALUES = "INSERT INTO messages (author, text) VALUES (?, ?);";
    private static final String DELETE_VALUES = "DELETE FROM messages WHERE author = 'o';";

    public static void main(String[] args) {
        try {
            Class.forName(H2_DRIVHER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Enter name of author or to print her text or '-all' to all table content. For Exit enter '-stop'");
        try (Scanner sc = new Scanner(System.in);) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (isStopTheApp(line)) {
                    break;
                } else if (isPrintAll(line)) {
                    printAll();
                } else {
                    printAuthor(line);

                    String[] authorAndMessage = line.split(":");
                    if (authorAndMessage.length == 2) {
                        AddNewUnit(authorAndMessage[0], authorAndMessage[1]);
                        printAll();
                     //   DeleteUnit(authorAndMessage[0], authorAndMessage[1]);
                     //   printAll();
                    } else {
                        System.out.println("Incorrect command '" + line + "'");
                    }

                }
            }
        }
    }

    private static void printAuthor(String line) {
        try (Connection connection = DriverManager.getConnection(DB_URL, LOGIN, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(CURRENT_AUTHOR);) {
            statement.setString(1, line);
            try (ResultSet resultSet = statement.executeQuery();) {
                printFetchedMessages(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void printAll() {
        try (Connection connection = DriverManager.getConnection(DB_URL, LOGIN, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(MESSAGES_FROM_TABLE);) {
            printFetchedMessages(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void printFetchedMessages(ResultSet resultSet) throws SQLException {
        System.out.println("Enter something");
        while (resultSet.next()) {
            String author = resultSet.getString(COLUMN_AUTHOR);
            String text = resultSet.getString(COLUMN_TEXT);
            System.out.println(author + " : " + text);
        }
    }

    /*private static void AddNewUnit(String author, String text) {
        System.out.println("New columns added");
        try (Connection connection = DriverManager.getConnection(DB_URL, LOGIN, PASSWORD);
             PreparedStatement pstm = connection.prepareStatement(AUTHOR_TEXT_VALUES);){
            pstm.setString(1, author);
            pstm.setString(1, text);
            pstm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/

    private static void AddNewUnit(String author, String message) {
        try (Connection connection = DriverManager.getConnection(DB_URL, LOGIN, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(AUTHOR_ADD_VALUES);) {
            statement.setString(1, author);
            statement.setString(2, message);
            int rowCount = statement.executeUpdate();
            if (rowCount == 0) {
                throw new RuntimeException("INSERT ERROR");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void DeleteUnit(String author, String message) {
        try (Connection connection = DriverManager.getConnection(DB_URL, LOGIN, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(DELETE_VALUES);) {
            statement.setString(1, author);
            statement.setString(2, message);
            int rowCount = statement.executeUpdate();
            if (rowCount == 0) {
                throw new RuntimeException("INSERT ERROR");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private static boolean isStopTheApp(String line) {
        return line.equals(EXIT);
    }

    private static boolean isPrintAll(String line) {
        return line.equals(SHOW_ALL);
    }
}



/*package pack.test.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

    public static final String DB_URL = "jdbc:h2:~/test";
    public static final String LOGIN = "sa";
    public static final String PASSWORD = "";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_TEXT = "text";
    public static final String H2_DRIVHER = "org.h2.Driver";
    public static final String EXIT = "-stop";
    public static final String SHOW_ALL = "-all";
    public static final String CURRENT_AUTHOR = "SELECT * FROM messages WHERE author = ?;";
    public static final String MESSAGES_FROM_TABLE = "SELECT * FROM messages;";

    public static void main(String[] args) {
        try {
            Class.forName(H2_DRIVHER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Enter name of author or to print her text or '-all' to all table content. For Exit enter '-stop'");
        try (Scanner sc = new Scanner(System.in);) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (isStopTheApp(line)) {
                    break;
                } if  (isPrintAll(line)) {
                    printAll();
                } else {
                    printAuthor(line);
                }
            }
        }
    }

    private static void printAuthor(String line) {
        try (Connection connection = DriverManager.getConnection(DB_URL, LOGIN, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(CURRENT_AUTHOR);) {
            statement.setString(1, line);
            try (ResultSet resultSet = statement.executeQuery();) {
                printFetchedMessages(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void printAll() {
        try (Connection connection = DriverManager.getConnection(DB_URL, LOGIN, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(MESSAGES_FROM_TABLE);) {
            printFetchedMessages(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void printFetchedMessages(ResultSet resultSet) throws SQLException {
        System.out.println("Enter something");
        while (resultSet.next()) {
            String author = resultSet.getString(COLUMN_AUTHOR);
            String text = resultSet.getString(COLUMN_TEXT);
            System.out.println(author + " : " + text);
        }
    }

    private static boolean isStopTheApp(String line) {
        return line.equals(EXIT);
    }

    private static boolean isPrintAll(String line) {
        return line.equals(SHOW_ALL);
    }
}

*/

/*import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static final String DB_URL = "jdbc:h2:~/test";
    public static final String LOGIN = "sa";
    public static final String PASSWORD = "";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_TEXT = "text";
    public static final String H2_DRIVHER = "org.h2.Driver";

    public static void main(String[] args) {
        try {
            /* Заставляем ClassLoader подгрузить класс драйвера в память java. */
/*            Class.forName(H2_DRIVHER);
        } catch (ClassNotFoundException e) {
            System.out.println("Can't get driver: " + e.getMessage());
            return;
        }
*/
        /* Получаем connection к базе данных. Для этого нужно указать url, login, password.
           Каждый connection должен быть закрыт после использования.
           В моем случае connection закроется автоматически т.к. я использую try-with-resources (try () {}). */
 /*       try (Connection connection = DriverManager.getConnection(DB_URL, LOGIN, PASSWORD);
             /* Получаем statement. Его будем использовать для выполнения SQL запросов.
                Каждый statement должен быть закрыт после использования.
                В моем случае statement закроется автоматически т.к. я использую try-with-resources (try () {}). */
 /*            Statement statement = connection.createStatement();
             /* Получаем resultSet. ResultSet - Это результат, полученный после выполнения SQL запроса.
                "SELECT * FROM messages;" - SQL запрос, который извлекает все данные из таблицы messages.
                Каждый resultSet должен быть закрыт после использования.
                В моем случае resultSet закроется автоматически т.к. я использую try-with-resources (try () {}). */
 /*            ResultSet resultSet = statement.executeQuery("SELECT * FROM messages;");) {
             /* resultSet - можно представить в виде таблицы с результатами выполнения запросов.
                На каждой итерации мы "просматриваем" одну строку этой таблицы.
                Первая строка всегда пустая поэтому мы вызываем метод next() чтобы перейти на строку с данными.
                next() - возвращает false, если мы проитерировали весь resultSet. */
 /*           while (resultSet.next()) {
                /* Т. к. в "SELECT * FROM messages;" мы указали * (извлечь все колонки таблицы), а не конкретные,
                   то resultSet выглядит как таблица messages. Чтобы извлечь данные из строки resultSet
                   указываем из какой колонки берем значение.*/
                /* Берем значение из колонки author у текущей строки 'таблицы'.*/
 /*               String author = resultSet.getString(COLUMN_AUTHOR);
                /* Берем значение из колонки text у текущей строки 'таблицы'.*/
 /*               String text = resultSet.getString(COLUMN_TEXT);
                System.out.println(author + " : " + text);
            }
        } catch (SQLException e) {
            System.out.println("Can't get connection: " + e.getMessage());
        }
    }
}
 */