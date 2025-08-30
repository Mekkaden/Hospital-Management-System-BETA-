public class Administrator{
    private int userID;
    private String name;
    private String username;
    private String password;
    public Administrator(int userID,String name,String username,String password){
        this.userID=userID;
        this.name=name;
        this.password=password;
    }
    public boolean login(String username,String password){
        return this.username.equals(username) && this.password.equals(password);
    }

}