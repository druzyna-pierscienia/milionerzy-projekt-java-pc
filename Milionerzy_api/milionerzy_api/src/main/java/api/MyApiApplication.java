package api;

import connection.Connect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sun.mail.smtp.SMTPTransport;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;


import java.sql.*;
import java.util.Random;

/**
 * The main class for the Milionerzy API application.
 */
@SpringBootApplication
@RestController
public class MyApiApplication {

    /**
     * The main method to start the Spring Boot application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(MyApiApplication.class, args);
    }

    /**
     * Retrieves a question for a specified round number.
     *
     * @param roundNumber The round number for which the question is requested.
     * @return The question and answer choices formatted as a single string.
     */
    @GetMapping("/question")
    public String getQuestion(@RequestParam(name = "roundNumber") String roundNumber) {
        Connect connect = new Connect();
        Connection connection = connect.getConnection();
        String pytanie = "blad";
        if (connection != null) {
            try {
                // Creating SQL query
                String query = "SELECT * FROM milionerzy.pytania WHERE numer_rundy = " + roundNumber;
                Random random = new Random();
                int randomNumber = random.nextInt(10) + 1;
                pytanie += randomNumber;

                // Executing the query
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                // Processing query results
                for (int i = 0; i < randomNumber; i++) {
                    resultSet.next();
                }

                // Retrieving values from query result columns
                pytanie = resultSet.getString("tresc");
                pytanie += "/" + resultSet.getString("odpowiedz_a");
                pytanie += "/" + resultSet.getString("odpowiedz_b");
                pytanie += "/" + resultSet.getString("odpowiedz_c");
                pytanie += "/" + resultSet.getString("odpowiedz_d");
                pytanie += "/" + resultSet.getString("prawidlowa");

                // Closing ResultSet and Statement objects
                resultSet.close();
                statement.close();

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                connect.close(); // Closing the connection
            }
        }
        return pytanie;
    }

    /**
     * Retrieves the top 10 rankings from the database.
     *
     * @return A string containing the user login and score, separated by '/' and terminated by ';'.
     */
    @GetMapping("/ranking")
    public String getRanking() {
        String ranking = "404";
        Connect connect = new Connect();
        Connection connection = connect.getConnection();
        if (connection != null) {
            try {
                // Creating SQL query
                String query = "SELECT tabilca_wynikow.wynik, uzytkownicy.login FROM milionerzy.tabilca_wynikow " +
                        "INNER JOIN milionerzy.uzytkownicy ON tabilca_wynikow.uzytkownik=uzytkownicy.id_uzytkownika " +
                        "ORDER BY wynik DESC LIMIT 10";

                // Executing the query
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                // Processing query results
                ranking = "";
                while (resultSet.next()) {
                    // Retrieving values from columns in the query result
                    ranking += resultSet.getString("login");
                    ranking += "/" + resultSet.getString("wynik") + ";";
                }

                // Closing ResultSet and Statement objects
                resultSet.close();
                statement.close();

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                connect.close(); // Closing the connection
            }
        }
        return ranking;
    }

    /**
     * Checks the login credentials against the database.
     *
     * @param login    The user login.
     * @param password The user password.
     * @return A string indicating whether the login was successful.
     */
    @GetMapping("/login")
    public String checkLogin(@RequestParam(name = "login") String login, @RequestParam(name = "password") String password) {
        String logSucces = "false";
        int count = 0;
        Connect connect = new Connect();
        Connection connection = connect.getConnection();
        if (connection != null) {
            try {
                // Creating SQL query
                String query = "SELECT id_uzytkownika FROM milionerzy.uzytkownicy WHERE login = ? AND haslo = ?";

                PreparedStatement statement = connection.prepareStatement(query);

                // Setting parameter values
                statement.setString(1, login);
                statement.setString(2, password);

                // Executing the query
                ResultSet resultSet = statement.executeQuery();

                // Processing query results
                while (resultSet.next()) {
                    count++;
                }
                if (count > 0) {
                    logSucces = "true";
                }

                // Closing ResultSet and Statement objects
                resultSet.close();
                statement.close();

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                connect.close(); // Closing the connection
            }
        }
        return logSucces;
    }

    /**
     * Registers a new user in the database.
     *
     * @param login    The user login.
     * @param password The user password.
     * @param mail     The user email.
     * @return A string indicating the success or failure of the registration process.
     */
    @PostMapping("/register")
    public String addUser(@RequestParam(name = "login") String login, @RequestParam(name = "password") String password,
                          @RequestParam(name = "mail") String mail) {
        String success = "0";
        int count = 0;

        Connect connect = new Connect();
        Connection connection = connect.getConnection();
        if (connection != null) {
            try {
                connection.setAutoCommit(false); // Enabling manual transaction management

                // Creating SQL query
                String query = "SELECT id_uzytkownika FROM milionerzy.uzytkownicy WHERE login = ?";

                PreparedStatement statement = connection.prepareStatement(query);

                // Setting parameter values
                statement.setString(1, login);

                // Executing the query
                ResultSet resultSet = statement.executeQuery();

                // Processing query results
                while (resultSet.next()) {
                    count++;
                }
                if (count != 0) {
                    success = "69";
                } else {

                    // Creating SQL query
                    query = "INSERT INTO milionerzy.uzytkownicy (login, haslo, mail, aktywowane) VALUES (?, ?, ?, false)";

                    PreparedStatement statement2 = connection.prepareStatement(query);
                    statement2.setString(1, login);
                    statement2.setString(2, password);
                    statement2.setString(3, mail);

                    // Executing SQL statement
                    int rowsAffected = statement2.executeUpdate();

                    if (rowsAffected == 1) { // If exactly one row was inserted
                        success = "420";
                        connection.commit(); // Committing the transaction
                    } else {
                        connection.rollback(); // Rolling back the transaction
                    }

                    // Closing Statement and Connection objects
                    statement.close();
                    statement2.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    connection.rollback(); // Rolling back the transaction in case of SQL error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } finally {
                connect.close(); // Closing the database connection
            }
        }
        return success;
    }

    /**
     * Saves the user's score in the database.
     *
     * @param login The user login.
     * @param score The user score.
     * @return A string indicating the success or failure of the score saving process.
     */
    @PostMapping("/saveScore")
    public String saveScore(@RequestParam(name = "login") String login, @RequestParam(name = "score") String score) {
        String success = "0";
        int count = 0;
        Connect connect = new Connect();
        Connection connection = connect.getConnection();
        if (connection != null) {
            try {
                connection.setAutoCommit(false); // Enabling manual transaction management

                // Creating SQL query
                String query = "INSERT INTO milionerzy.tabilca_wynikow (uzytkownik,wynik) VALUES((SELECT id_uzytkownika FROM milionerzy.uzytkownicy WHERE login = ?),?)";

                // Preparing SQL statement with parameters
                PreparedStatement statement2 = connection.prepareStatement(query);
                statement2.setString(1, login);
                statement2.setInt(2, Integer.parseInt(score));

                // Executing SQL statement
                int rowsAffected = statement2.executeUpdate();

                if (rowsAffected == 1) { // If exactly one row was inserted
                    success = "420";
                    connection.commit(); // Committing the transaction
                } else {
                    connection.rollback(); // Rolling back the transaction
                }

                // Closing Statement and Connection objects
                statement2.close();

            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    connection.rollback(); // Rolling back the transaction in case of SQL error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } finally {
                connect.close(); // Closing the database connection
            }
        }
        return success;
    }

    /**
     * Retrieves the activation code for a user from the database.
     *
     * @param login The user login.
     * @return The activation code or an error message.
     */
    private String getActivationCodeFromDatabase(String login) {
        Connect connect = new Connect();
        Connection connection = connect.getConnection();
        String activationCode = null;

        if (connection != null) {
            try {
                String query = "SELECT kod FROM milionerzy.kody_aktywacji " +
                        "JOIN milionerzy.uzytkownicy ON kody_aktywacji.id_uzytkownika = uzytkownicy.id_uzytkownika " +
                        "WHERE uzytkownicy.login = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, login);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    activationCode = resultSet.getString("kod");
                }

                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                connect.close();
            }
        }

        return activationCode;
    }

    /**
     * Retrieves the user's email from the database.
     *
     * @param login The user login.
     * @return The user's email or null if not found.
     */
    private String getEmailFromDatabase(String login) {
        Connect connect = new Connect();
        Connection connection = connect.getConnection();
        String email = null;

        if (connection != null) {
            try {
                String query = "SELECT mail FROM milionerzy.uzytkownicy WHERE login = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, login);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    email = resultSet.getString("mail");
                }

                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                connect.close();
            }
        }

        return email;
    }

    /**
     * Sends an activation email to the user's email address.
     *
     * @param email          The user's email address.
     * @param activationCode The activation code.
     */
    private void sendActivationEmail(String email, String activationCode) {
        final String username = "javatok121@gmail.com";
        final String password = "ulbfkfxedyvjiwpk";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("from@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(email)
            );
            message.setSubject("Milionerzy - Activation Code");

            // Creating the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Setting the actual message
            messageBodyPart.setText("Activation Code: " + activationCode);

            // Creating a multipart message
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Activation Code Sent");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends an activation code to the user's email address.
     *
     * @param login The user login.
     * @return A string indicating the success or failure of the activation code sending process.
     */
    @GetMapping("/sendActivationCode")
    public String sendActivationCode(@RequestParam(name = "login") String login) {
        String activationCode = getActivationCodeFromDatabase(login);

        if (activationCode != null) {
            String email = getEmailFromDatabase(login);

            if (email != null) {
                sendActivationEmail(email, activationCode);
                return "Activation code sent successfully";
            } else {
                return "Error: User does not have a valid email address.";
            }
        } else {
            return "Error: Activation code not found in the database.";
        }
    }

    /**
     * Activates a user in the database.
     *
     * @param login The user login.
     * @return A string indicating the success or failure of the user activation process.
     */
    @GetMapping("/activateUser")
    public String activateUser(@RequestParam(name = "login") String login) {
        Connect connect = new Connect();
        Connection connection = connect.getConnection();

        if (connection != null) {
            try {
                // Updating activation flag
                String updateActivationFlagQuery = "UPDATE milionerzy.uzytkownicy SET aktywowane = true WHERE login = ?";
                PreparedStatement updateActivationFlagStatement = connection.prepareStatement(updateActivationFlagQuery);
                updateActivationFlagStatement.setString(1, login);

                int rowsAffected = updateActivationFlagStatement.executeUpdate();

                // Closing PreparedStatement objects
                updateActivationFlagStatement.close();

                if (rowsAffected > 0) {
                    return "User activated successfully";
                } else {
                    return "Error: Unable to update activation status";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                connect.close();
            }
        }

        return "Error: Database connection error";
    }

    /**
     * Retrieves the activation code for a user from the database.
     *
     * @param login The user login.
     * @return The activation code or an error message.
     */
    @GetMapping("/getActivationCode")
    public String getActivationCode(@RequestParam(name = "login") String login) {
        Connect connect = new Connect();
        Connection connection = connect.getConnection();

        if (connection != null) {
            try {
                String query = "SELECT kod FROM milionerzy.kody_aktywacji " +
                        "JOIN milionerzy.uzytkownicy ON kody_aktywacji.id_uzytkownika = uzytkownicy.id_uzytkownika " +
                        "WHERE uzytkownicy.login = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, login);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String activationCode = resultSet.getString("kod");
                    resultSet.close();
                    statement.close();
                    connect.close();
                    return activationCode;
                } else {
                    resultSet.close();
                    statement.close();
                    connect.close();
                    return "Error: Activation code not found in the database.";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                connect.close();
            }
        }

        return "Error: Database connection error";
    }

    /**
     * Retrieves the activation status for a user from the database.
     *
     * @param login The user login.
     * @return A string indicating whether the user is activated or not.
     */
    @GetMapping("/getActivationStatus")
    public String getActivationStatus(@RequestParam(name = "login") String login) {
        Connect connect = new Connect();
        Connection connection = connect.getConnection();

        if (connection != null) {
            try {
                String query = "SELECT aktywowane FROM milionerzy.uzytkownicy WHERE login = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, login);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    boolean activationStatus = resultSet.getBoolean("aktywowane");
                    resultSet.close();
                    statement.close();
                    connect.close();
                    return activationStatus ? "Activated" : "Not Activated";
                } else {
                    resultSet.close();
                    statement.close();
                    connect.close();
                    return "Error: User not found in the database.";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                connect.close();
            }
        }

        return "Error: Database connection error";
    }

    /**
     * Sends the user's score via email.
     *
     * @param login The user login.
     * @param wynik The user score.
     * @return A string indicating the success or failure of the score sending process.
     */
    @GetMapping("/sendScore")
    public String sendScore(@RequestParam(name = "login") String login, @RequestParam(name = "wynik") Integer wynik) {
        String email = getEmailFromDatabase(login);

        if (email != null) {
            sendScoreEmail(email, wynik);
            return "Score sent successfully";
        } else {
            return "Error: User does not have a valid email address.";
        }
    }
}
