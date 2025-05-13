import java.util.List;

public class Cadastro extends Usuario {

    public Cadastro(String fullname, String idnumber, String passwoerd) {
        super(fullname, idnumber, passwoerd);
        //TODO Auto-generated constructor stub
    }

    public void cadastrar(List<Usuario> usuarios){
        usuarios.add(this);
        System.out.println("Usuário cadastrado com sucesso.");
    }
}

}
