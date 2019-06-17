package shoppingList;

//controller = handles a REST /HTTP request that comes in. 

import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.Map.Entry;
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


	//**Login method
	@RequestMapping(value="/processlogin", method=RequestMethod.POST)
	public ResponseEntity<Boolean> processLogin(@RequestBody String username){

		System.out.println("in controller processLogin");
		User theUser = dao.retrieveUser(username);
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
			return null;
		}

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.SET_COOKIE, cookie.toString());

		//headers.set("HeaderName", "Headervalue"); - this we had in class. But since we have cookies we should need it
		return new ResponseEntity<>(true, headers, HttpStatus.ACCEPTED);	}
	
	//**Logout Method
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public void logout() {
		System.out.println("\nin ctler logout");
		this.currentUser = null;	
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

	//**ShowLists Method
	@RequestMapping(path = "/showlists", method = RequestMethod.GET)
	public List<ShopList> retrieveLists(@CookieValue (value = "sessionID", defaultValue="NoCookie") String sessionID) {
		System.out.println("in controller getLists");
		if (sessionID.equals("NoCookie")) {
			System.out.println("went to default, no cookie");
			return null;
		}
		else {
			for(Map.Entry<String, User> entry : sessions.entrySet()) {
				System.out.println("key: " + entry.getKey());
				System.out.println("value  " + entry.getValue().getUsername());
			}
			
			System.out.println("This Session id " + sessionID);
			System.out.println("This Username: " + sessions.get(sessionID).getUsername());
			String username = sessions.get(sessionID).getUsername();

			List<ShopList> lists = dao.retrieveLists(username);
			return lists;
		}	
	}

	//**Retrieve FoodItems Method
	@RequestMapping(path ="/fooditems", method = RequestMethod.GET)
	public List<Food> retrieveFoodItems(){
		System.out.println("in controller getFoodItems()");

		List<Food> foodList = dao.retrieveFoodList();
		return foodList;
	}

	//**Method to get individal list
	@RequestMapping(value ="/lists/{listId}", method = RequestMethod.GET)
	public List<ListItem> showItemList(@PathVariable("listId") String listId){
		Long lListId = Long.valueOf(listId);
		this.currListId = lListId;
		System.out.println("in controller showItemList lists.");
		System.out.println("ShopList id: " + listId);
		List<ListItem> items = dao.retrieveListItems(lListId);
		return items;
	}
	
	//**Add item method
	@RequestMapping(path ="/additem", method = RequestMethod.POST)
	public void addItem(@RequestBody ListItem item) {
		System.out.println("in controller addItem");
		System.out.println(item);
		item.setListId(this.currListId);
		dao.addItem(item);
	}

	//**Delete list method
	@RequestMapping(path ="/lists/{listId}", method = RequestMethod.POST)
	public void deleteList(@PathVariable("listId") String listId) {
		Long lListId = Long.valueOf(listId);
		System.out.println("in controller delete list");
		dao.deleteList(lListId);
	}
	
//----------------------------------------------------------------------------------

}
