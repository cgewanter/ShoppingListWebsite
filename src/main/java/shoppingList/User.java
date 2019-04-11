package shoppingList;

public class User {
		
	private int userId;
	private String firstname;
	private String lastname;
	private String username;
	
	
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getUserId() {
		return this.userId;
	}
	public String getFirstname() {
		return this.firstname;
	}
	
	public String getLastname() {
		return this.lastname;
	}
	
	public String getUsername() {
		return this.username;
	}
}
