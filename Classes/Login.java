import java.util.List;

public class Login extends Usuario {

    public Login(String idnumber, String password) {
        super(null, idnumber, password);
        //TODO Auto-generated constructor stub
    }

    public boolean login(List<Usuario> usuarios){
        for(Usuario u : usuarios){
            if (u.getFullname().equals(this.getFullname()) && u.getPassword().equals(this.getPassword())) {
                System.out.println("Bem Vindo, " + u.getFullname() + "!");
                return true;
            }
        }
        System.out.println("Idnumber ou password incorretos.");
        return false;
    }

}
