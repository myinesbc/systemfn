package users;

public class Usuario {
    
    private int id;
    private String fullname;
    private String idnumber;
    private String password;
    public String getFullname;

    public Usuario(String fullname, String idnumber, String password){
        this.fullname = fullname;
        this.idnumber = idnumber;
        this.password = password;
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

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