package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user_DCM")
public class User {
	@Id
	private String UserName;
	private String Password;
	
	
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public User(String userName, String password) {
		super();
		UserName = userName;
		Password = password;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	 @Override
	    public String toString() {
	        final StringBuilder sb = new StringBuilder("User{");
	        sb.append("UserName =").append(UserName);
	        sb.append(", Password = '").append(Password).append('\'');
	        sb.append('}');
	        return sb.toString();
	    }
	
}
