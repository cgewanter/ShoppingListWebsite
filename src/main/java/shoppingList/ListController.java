package shoppingList;

//controller = handles a REST /http request that comes in. 

import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin
@RestController
public class ListController {

	private DAObject dao = new DAObject();
	private String username;
	protected User currentUser;
	private int userId;
	private long currListId;

	private static HashMap<String, User> usernames = new HashMap<String, User>();

	private static final ConcurrentHashMap<String, User> sessions = new ConcurrentHashMap<String, User>();

	//COMPLETED AND WORKING METHODS:

	//**Login method
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public ResponseEntity<Boolean> processLogin(@RequestBody String username){

		System.out.println("in controller processLogin");
		User theUser = dao.getUser(username);
		this.currentUser = theUser;
		System.out.println("The user is: " + theUser.getUsername() + " " + theUser.getFirstname() + " " 
				+ theUser.getLastname() );
		UUID sessionId = UUID.randomUUID();
		HttpCookie cookie;

		if (theUser != null){
			//add them to the session
			sessions.put(sessionId.toString(), theUser);

			//add to username hashmap
			usernames.put(theUser.getUsername(), theUser);

			System.out.println("we put this user in sessions hashmap");
			//create their cookie
			cookie = ResponseCookie.from("sessionID", sessionId.toString()).path("/").build();

			System.out.println("We have our cookie: " + ((ResponseCookie) cookie).toString());

		}

		else {
			cookie = ResponseCookie.from("loggedIn", "false").path("/").build();
		}

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.SET_COOKIE, cookie.toString());

		//headers.set("HeaderName", "Headervalue"); - this we had in class. But since we have cookies we should need it
		return new ResponseEntity<>(true, headers, HttpStatus.ACCEPTED);


		//return null;
	}

	//**Add List Method:
	@RequestMapping(path="/addlist", method = RequestMethod.POST)
	public boolean createNewList(@RequestBody String listname) {


		System.out.println("Received in controller:" + listname);
		ShopList list = new ShopList();
		list.setListTitle(listname);
		list.setUserId(currentUser.getUserId());
		System.out.println("Sending to database");
		if (this.currentUser != null) {
			boolean addedList = dao.addList(list);
			return addedList;
		}
		return false;
	}

	//**New User Method:
	@RequestMapping(path="/newuser", method = RequestMethod.POST)
	public void createNewUser(@RequestBody User newUser) {
		System.out.println("Received:" + newUser.getUsername());
		this.username = newUser.getUsername();
		this.currentUser = newUser;
		System.out.println("Sending to database");
		dao.addUser(newUser);
		System.out.println("Current user: " + this.username);
	}


	@RequestMapping(path = "/showlists", method = RequestMethod.GET)
	public List<ShopList> getLists(@CookieValue (value = "sessionID", defaultValue="NoCookie") String sessionID) {
		System.out.println("in controller getLists");
		if (sessionID.equals("NoCookie")) {
			System.out.println("went to default, no cookie");
			return null;
		}
		else {
			String username = sessions.get(sessionID).getUsername();

			List<ShopList> lists = dao.getLists(username);
			return lists;
		}
		
	}

	@RequestMapping(path ="/fooditems", method = RequestMethod.GET)
	public List<Food> getFoodItems(){
		System.out.println("in controller getFoodItems()");

		List<Food> foodList = dao.getFoodList();
		return foodList;
	}

	@RequestMapping(value ="/lists/{listId}", method = RequestMethod.GET)
	public List<ListItem> showItemList(@PathVariable("listId") String listId){
		Long lListId = Long.valueOf(listId);
		this.currListId = lListId;
		System.out.println("in controller showItemList lists.");
		System.out.println("ShopList id: " + listId);
		List<ListItem> items = dao.getListItems(lListId);
		return items;
	}

	@RequestMapping(path ="/additem", method = RequestMethod.POST)
	public void addItem(@RequestBody ListItem item) {
		System.out.println("in controller addItem");
		System.out.println(item);
		item.setListId(this.currListId);
		dao.addItem(item);
	}

	@RequestMapping(path ="/lists/{listId}", method = RequestMethod.POST)
	public void deleteList(@PathVariable("listId") String listId) {
		Long lListId = Long.valueOf(listId);
		System.out.println("in controller delete list");
		dao.deleteList(lListId);
	}

	//----------------------------------------------------------------------------------

	
	//Methods Not currently used, to be eventually deleted:

	@RequestMapping(path ="/items", method = RequestMethod.GET)
	public List<String> retrieveLists(){
		System.out.println("Received request for items");
		List<String> list = dao.selectItemList();
		return list;
	}

	@RequestMapping(path="/items", method = RequestMethod.POST)
	public void createItem(@RequestBody ListItem newItem) {
		System.out.println("Current user: " + this.username);
		System.out.println("Received:" + newItem.getFoodId());
		System.out.println("Sending to database");
		dao.addItem(newItem);
	}

/*

	@RequestMapping("/a/{id}") //will pick up this id from the path
	public String indexA(@PathVariable(name="id")String id) {
		return "Greetings from Spring Boot! Hello from A. The id you passed is " + id;
	}

	@RequestMapping("/a/{id}/{level}") //will pick up this id from the path
	public String indexB(@PathVariable(name="id")String id, @PathVariable(name="level")String level) {
		return "Greetings from Spring Boot! Hello from B. The id you passed is " + id 
				+" and the level is " + level;
	}*/
}