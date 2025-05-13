public class Usuario {
    
    //ATRIBUTOS
    private String fullname;
    private String idnumber;
    private String password;

    //MÉTODOS
    public Usuario(String fullname, String idnumber, String passwoerd){
        this.fullname = fullname;
        this.idnumber = idnumber;
        this.password = passwoerd;
    }

    //FUNCIONALIDADES
    public String getFullname(){
        return fullname;
    }
    public void setFullname(String fullname){
        this.fullname = fullname;
    }

    public String getIdnumber(){
        return idnumber;
    }
    public void setIdnumber(String idnumber){
        this.idnumber = idnumber;
    }

    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
}
