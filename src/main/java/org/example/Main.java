package org.example;

import org.example.entities.Product;
import org.example.repository.ProductRepository;
import org.example.entities.User;
import org.example.repository.UserRepository;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        ProductRepository listaDeProdutos = null;
        UserRepository listaDeUsuarios = null;
        Connection conn = null;
        UserRepository userRepository = null;

        String url = "jdbc:sqlite:database.sqlite";

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
            if (conn != null) {
                listaDeProdutos = new ProductRepository(conn);
                listaDeUsuarios = new UserRepository(conn);
                userRepository = new UserRepository(conn);

                String createUsersTableSQL = """
            CREATE TABLE IF NOT EXISTS users (
                uuid TEXT PRIMARY KEY,
                name TEXT NOT NULL,
                email TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL
            );
        """;

                String createProductsTableSQL = """
            CREATE TABLE IF NOT EXISTS products (
                uuid TEXT PRIMARY KEY,
                name TEXT NOT NULL,
                price REAL NOT NULL
            );
        """;

                Statement stmt = conn.createStatement();
                stmt.execute(createUsersTableSQL);
                stmt.execute(createProductsTableSQL);

                listaDeProdutos = new ProductRepository(conn);
                listaDeUsuarios = new UserRepository(conn);

                System.out.println("Tabelas 'users' e 'products' prontas.");

                listaDeProdutos = new ProductRepository(conn);

            } else {
                System.out.println("Falha na conexão.");
                System.exit(1);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Driver JDBC não encontrado.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Erro de conexão: " + e.getMessage());
        }

        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            System.out.println("\n---MENU---");
            System.out.println("1 - Cadastrar Produto");
            System.out.println("2 - Listas Produtos");
            System.out.println("3 - Cadastrar Usuário");
            System.out.println("4 - Listar Usuários");
            System.out.println("5 - Sair");
            System.out.println("Escolha uma opção: ");
            option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.println("Cadastrar Produto");
                    listaDeProdutos.save(new Product("Teste", 10));
                    listaDeProdutos.save(new Product("Computador", 3000));
                    break;
                case 2:
                    System.out.println("Listar Produtos");
                    List<Product> products = listaDeProdutos.findAll();
                    products.forEach(System.out::println);
                    break;
                case 3:
                    System.out.println("Cadastrar Usuário");
                    System.out.print("Nome: ");
                    scanner.nextLine();
                    String nome = scanner.nextLine();

                    System.out.print("Email: ");
                    String email = scanner.nextLine();

                    System.out.print("Senha: ");
                    String senha = scanner.nextLine();

                    User user = new User(nome, email, senha);
                    userRepository.save(user);
                    System.out.println("Usuário cadastrado com sucesso!");
                    break;
                case 4:
                    System.out.println("Listar Usuários");
                    List<User> users = userRepository.findAll();
                    users.forEach(System.out::println);
                    break;
                case 5:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente");

            }
        } while (option != 5);

        scanner.close();
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}