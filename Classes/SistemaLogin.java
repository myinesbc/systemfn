import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    private static List<Usuario> usuarios = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n1 - Cadastrar");
            System.out.println("2 - Login");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir linha

            switch (opcao) {
                case 1:
                    System.out.print("Nome: ");
                    String fullname = scanner.nextLine();
                    System.out.print("Email: ");
                    String idnumberCadastro = scanner.nextLine();
                    System.out.print("Senha: ");
                    String passwordCadastro = scanner.nextLine();

                    Cadastro cadastro = new Cadastro(fullname, idnumberCadastro, passwordCadastro);
                    cadastro.cadastrar(usuarios);
                    break;

                case 2:
                    System.out.print("Email: ");
                    String idnumberLogin = scanner.nextLine();
                    System.out.print("Senha: ");
                    String passwordLogin = scanner.nextLine();

                    Login login = new Login (idnumberLogin, passwordLogin);
                    login.login(usuarios);
                    break;

                case 0:
                    System.out.println("Encerrando...");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);

        scanner.close();
    }
}
