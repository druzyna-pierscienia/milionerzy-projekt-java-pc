package api;

import connection.Connect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.Random;

@SpringBootApplication
@RestController
public class MyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyApiApplication.class, args);
    }

    @GetMapping("/question")
    public String getQuestion(@RequestParam(name = "roundNumber") String roundNumber) {
        Connect connect = new Connect();
        Connection connection = connect.getConnection();
        String pytanie = "blad";
        if (connection != null) {
            try {
                // Tworzenie zapytania SQL
                String query = "SELECT * FROM milionerzy.pytania WHERE numer_rundy = "+roundNumber;
                Random random = new Random();
                int randomNumber = random.nextInt(10)+1;
                pytanie += randomNumber;
                // Wykonanie zapytania
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                // Przetwarzanie wyników zapytania
                for(int i = 0; i < randomNumber; i++) {
                    resultSet.next();
                }
                // Pobieranie wartości z kolumn w wyniku zapytania

                pytanie = resultSet.getString("tresc");
                pytanie += "/"+resultSet.getString("odpowiedz_a");
                pytanie += "/"+resultSet.getString("odpowiedz_b");
                pytanie += "/"+resultSet.getString("odpowiedz_c");
                pytanie += "/"+resultSet.getString("odpowiedz_d");
                pytanie += "/"+resultSet.getString("prawidlowa");

                // Zamknięcie obiektów ResultSet i Statement
                resultSet.close();
                statement.close();

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                connect.close(); // Zamknięcie połączenia
            }

        }
        return pytanie;
    }

    @GetMapping("/ranking")
    public String getRanking(){
        String ranking = "404";
        Connect connect = new Connect();
        Connection connection = connect.getConnection();
        if (connection != null) {
            try {
                // Tworzenie zapytania SQL
                String query = "SELECT tabilca_wynikow.wynik, uzytkownicy.login FROM milionerzy.tabilca_wynikow INNER JOIN milionerzy.uzytkownicy ON tabilca_wynikow.uzytkownik=uzytkownicy.id_uzytkownika ORDER BY wynik DESC LIMIT 10";


                // Wykonanie zapytania
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                // Przetwarzanie wyników zapytania
                ranking = "";
                while(resultSet.next()){
                    // Pobieranie wartości z kolumn w wyniku zapytania
                    ranking += resultSet.getString("login");
                    ranking += "/"+resultSet.getString("wynik")+";";
                }
                // Zamknięcie obiektów ResultSet i Statement
                resultSet.close();
                statement.close();

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                connect.close(); // Zamknięcie połączenia
            }

        }
        return ranking;
    }

    @GetMapping("/login")
    public String checkLogin(@RequestParam(name = "login") String login, @RequestParam(name = "password") String password){
        String logSucces = "false";
        int count = 0;
        Connect connect = new Connect();
        Connection connection = connect.getConnection();
        if (connection != null) {
            try {
                // Tworzenie zapytania SQL
                String query = "SELECT id_uzytkownika FROM milionerzy.uzytkownicy WHERE login = ? AND haslo = ?";

                PreparedStatement statement = connection.prepareStatement(query);

                // Ustawienie wartości parametrów
                statement.setString(1, login);
                statement.setString(2, password);

                // Wykonanie zapytania
                ResultSet resultSet = statement.executeQuery();


                // Przetwarzanie wyników zapytania
                while(resultSet.next()){
                    count++;
                }
                if(count>0){
                    logSucces="true";
                }
                // Zamknięcie obiektów ResultSet i Statement
                resultSet.close();
                statement.close();

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                connect.close(); // Zamknięcie połączenia
            }

        }
        return logSucces;
    }

    @PostMapping("/register")
    public String addUser(@RequestParam(name = "login") String login, @RequestParam(name = "password") String password, @RequestParam(name = "mail") String mail) {
        String success = "0";
        int count = 0;
        Connect connect = new Connect();
        Connection connection = connect.getConnection();
        if (connection != null) {
            try {
                connection.setAutoCommit(false); // włączenie ręcznego zarządzania transakcjami

                // Tworzenie zapytania SQL
                String query = "SELECT id_uzytkownika FROM milionerzy.uzytkownicy WHERE login = ?";

                PreparedStatement statement = connection.prepareStatement(query);

                // Ustawienie wartości parametrów
                statement.setString(1, login);

                // Wykonanie zapytania
                ResultSet resultSet = statement.executeQuery();


                // Przetwarzanie wyników zapytania
                while(resultSet.next()){
                    count++;
                }
                if(count!=0){
                    success="69";
                }else {

                    // Utworzenie zapytania SQL
                    query = "INSERT INTO milionerzy.uzytkownicy (login, haslo, mail) VALUES (?, ?, ?)";

                    // Przygotowanie instrukcji SQL z parametrami
                    PreparedStatement statement2 = connection.prepareStatement(query);
                    statement2.setString(1, login);
                    statement2.setString(2, password);
                    statement2.setString(3, mail);

                    // Wykonanie instrukcji SQL
                    int rowsAffected = statement2.executeUpdate();

                    if (rowsAffected == 1) { // Jeżeli wstawiono dokładnie jeden wiersz
                        success = "420";
                        connection.commit(); // zatwierdzenie tranzakcji
                    } else {
                        connection.rollback(); // wycofanie tranzakcji
                    }

                    // Zamknięcie obiektów Statement i Connection
                    statement.close();
                    statement2.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    connection.rollback(); // wycofanie tranzakcji w przypadku błędu SQL
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } finally {
                connect.close(); // zamknięcie połączenia z bazą danych
            }
        }
        return success;
    }
    @PostMapping("/saveScore")
    public String saveScore(@RequestParam(name = "login") String login, @RequestParam(name = "score") String score){
        String success = "0";
        int count = 0;
        Connect connect = new Connect();
        Connection connection = connect.getConnection();
        if (connection != null) {
            try {
                connection.setAutoCommit(false); // włączenie ręcznego zarządzania transakcjami




                // Utworzenie zapytania SQL
                String query = "INSERT INTO milionerzy.tabilca_wynikow (uzytkownik,wynik) VALUES((SELECT id_uzytkownika FROM milionerzy.uzytkownicy WHERE login = ?),?)";

                // Przygotowanie instrukcji SQL z parametrami
                PreparedStatement statement2 = connection.prepareStatement(query);
                statement2.setString(1, login);
                statement2.setInt(2, Integer.parseInt(score));

                // Wykonanie instrukcji SQL
                int rowsAffected = statement2.executeUpdate();

                if (rowsAffected == 1) { // Jeżeli wstawiono dokładnie jeden wiersz
                    success = "420";
                    connection.commit(); // zatwierdzenie tranzakcji
                } else {
                    connection.rollback(); // wycofanie tranzakcji
                }

                // Zamknięcie obiektów Statement i Connection

                statement2.close();

            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    connection.rollback(); // wycofanie tranzakcji w przypadku błędu SQL
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } finally {
                connect.close(); // zamknięcie połączenia z bazą danych
            }
        }
        return success;
    }
}