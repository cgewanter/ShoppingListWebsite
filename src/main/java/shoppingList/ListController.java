package shoppingList;

//controller = handles a REST /http request that comes in. 

import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@CrossOrigin
@RestController
public class ListController {
	
	private DAObject dao = new DAObject();
	private String username;
	protected User user;
	
    @RequestMapping(path ="/items", method = RequestMethod.GET)
    public List<String> retrieveLists(){
    	System.out.println("Received request for items");
    	List<String> list = dao.selectItemList();
    	return list;
     }
    
    @RequestMapping(path ="/showlists", method = RequestMethod.GET)
    public List<ShopList> getLists(){
    	System.out.println("Received request for lists");
    	List<ShopList> list = dao.getLists();
    	return list;
     }
    
    @RequestMapping(path="/items", method = RequestMethod.POST)
	public void createItem(@RequestBody ListItem newItem) {
    	System.out.println("Current user: " + this.username);
		System.out.println("Received:" + newItem.getFoodId());
		System.out.println("Sending to database");
		dao.addItem(newItem);
	}
    
    @RequestMapping(path="/login", method = RequestMethod.POST)
    public void login(@RequestBody User user) {
    	System.out.println("in login");
    	this.user = user;
    	System.out.println("The user is: " + user.getUsername());
    }
    
    @RequestMapping(path="/newuser", method = RequestMethod.POST)
   	public void createNewUser(@RequestBody User newUser) {
   		System.out.println("Received:" + newUser.getUsername());
   		this.username = newUser.getUsername();
   		this.user = newUser;
   		System.out.println("Sending to database");
   		dao.addUser(newUser);
   		System.out.println("Current user: " + this.username);
   	}
    
    @RequestMapping(path="/addlist", method = RequestMethod.POST)
   	public void createNewList(@RequestBody ShopList list) {
   		System.out.println("Received:" + list.getListTitle());
   		System.out.println("User:" + this.username);
   		System.out.println("userid" + this.user.getUserId());
   		System.out.println("Sending to database");
   		User user = this.user;
   		dao.addList(list, user);
   	}
       
    @RequestMapping("/a/{id}") //will pick up this id from the path
    public String indexA(@PathVariable(name="id")String id) {
        return "Greetings from Spring Boot! Hello from A. The id you passed is " + id;
    }
    
    @RequestMapping("/a/{id}/{level}") //will pick up this id from the path
    public String indexB(@PathVariable(name="id")String id, @PathVariable(name="level")String level) {
        return "Greetings from Spring Boot! Hello from B. The id you passed is " + id 
        				+" and the level is " + level;
    }
}