package shoppingList;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import ch.qos.logback.core.net.SyslogOutputStream;
import shoppingList.Food;

public class DAObject {

	public static final String DRIVER = "com.mysql.jdbc.Driver";
	public static final String JDBC_URL = "jdbc:mysql://localhost:3306/shopdb";
	public static final String USERNAME = "root";
	public static final String PASSWORD = "Mysql19!";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired 
	Environment env;

	public DAObject() {
		DataSource source = getDataSource();
		this.jdbcTemplate = new JdbcTemplate(source);
	}	
	
	public User getUser(String username) {
		System.out.println("in getUser in dao");
		RowMapper<User> rowMapper = (rs, index) -> {
			User theUser = new User();
			theUser.setFirstname(rs.getString("Firstname"));
			theUser.setLastname(rs.getString("Lastname"));
			theUser.setUserId(rs.getInt("UserId"));
			theUser.setUsername(rs.getString("Username"));
			return theUser;	
		};

		String sql = "select * from users where username = ?";

		User user = (User) jdbcTemplate.queryForObject(sql, new Object[] {username}, rowMapper);
		System.out.println("we have the user! : " + user.getFirstname());

		return user;
	}
	
	public void addUser(User user) {
		System.out.println("in DAO addUser");
		jdbcTemplate.update(
				"insert into users (userid, firstname, lastname, username) values (?,?,?,?)",
				null, user.getFirstname(),user.getLastname(), user.getUsername()
				);
	}

	public boolean addList(ShopList list) {
		System.out.println("in dao in addshoplist");
		System.out.println("user id: " + list.getUserId());
		System.out.println("In dao, before update query, the list title is : " + list.getListTitle());
		
		jdbcTemplate.update(
				"insert into lists (listid, listtitle, categ, userid) values (?,?,?,?)",
				null, list.getListTitle(), list.getCateg(), list.getUserId());

		System.out.println("added a list!");
		return true;
	}
	
	
	
	public void deleteList(Long listId) {
		System.out.println("in dao delete list");
		//bec of foreign key, first delete from listItem table
		jdbcTemplate.update("delete from listitem where listId = ?", listId);
		jdbcTemplate.update("delete from lists where listId = ?", listId);
		System.out.println("deleted list");
	}
//-----------------------------------------------------------------------------------------------------------

	
	
	
	/*  this is the real one, for when the cookies work.
	 * 
	 * public List<ShopList> getLists(User user) {
		System.out.println("in getLists in dao");
		RowMapper<ShopList> rowMapper = (rs, index) -> {
			ShopList sl = new ShopList();
			sl.setListId(rs.getInt("ListId"));
			sl.setListTitle(rs.getString("ListTitle"));
			sl.setUserId(rs.getInt("UserId"));
			sl.setCateg(rs.getString("Categ"));
			return sl;
		};
		String sql = "select * from lists where userid = ?";
		System.out.println("user id : " + user.getUserId());
		List<ShopList> records = jdbcTemplate.query(sql, new Object[] {user.getUserId()}, rowMapper);
		System.out.println("In dao.getLists(), here are the lists for " + user.getUsername());
		for (int i = 0; i < records.size(); i++) {
			System.out.println(records.get(i).getListTitle());
		}
		return records;
	}
	*/
	
	//trial one
	/*public List<ShopList> getLists(User user) {
		System.out.println("in getLists in dao");
		RowMapper<ShopList> rowMapper = (rs, index) -> {
			ShopList sl = new ShopList();
			sl.setListId(rs.getInt("ListId"));
			sl.setListTitle(rs.getString("ListTitle"));
			sl.setUserId(rs.getInt("UserId"));
			sl.setCateg(rs.getString("Categ"));
			return sl;
		};
		String sql = "select * from lists where userid = ?";
		System.out.println("user id : " + user.getUserId());
		List<ShopList> records = jdbcTemplate.query(sql, new Object[] {user.getUserId()}, rowMapper);
		System.out.println("In dao.getLists(), here are the lists for " + user.getUsername());
		for (int i = 0; i < records.size(); i++) {
			System.out.println(records.get(i).getListTitle());
		}
		return records;
	}*/
	
	
	//this is the fake one, for before the cookies work
	public List<ShopList> getLists() {
		System.out.println("in getLists in dao");
		RowMapper<ShopList> rowMapper = (rs, index) -> {
			ShopList sl = new ShopList();
			sl.setListId(rs.getInt("ListId"));
			sl.setListTitle(rs.getString("ListTitle"));
			sl.setUserId(rs.getInt("UserId"));
			sl.setCateg(rs.getString("Categ"));
			return sl;
		};
		String sql = "select * from lists";
		//System.out.println("user id : " + user.getUserId());
		List<ShopList> records = jdbcTemplate.query(sql,  rowMapper);
		System.out.println("In dao.getLists(), here are the lists");
		for (int i = 0; i < records.size(); i++) {
			System.out.print(records.get(i).getListTitle()  + " ");
		}
		return records;
	}
	
	public List<Food> getFoodList(){
		System.out.println("in dao.getfoodlist()");
		
		RowMapper<Food> rowMapper = (rs, index) -> {
			Food f = new Food();
			f.setFoodId(rs.getInt("FoodId"));
			f.setFoodName(rs.getString("FoodName"));
			f.setCateg(rs.getString("Categ"));
			return f;
		};
		String sql = "select * from food";
		//System.out.println("user id : " + user.getUserId());
		List<Food> records = jdbcTemplate.query(sql,  rowMapper);
		System.out.println("In dao.getFoodList(), here are the lists");
		for (int i = 0; i < records.size(); i++) {
			System.out.print(records.get(i).getFoodName()  + " ");
		}
		return records;
		
		
	}

	public List<ListItem> getListItems(Long listId){
		System.out.println("in dao getListItems");
		
		RowMapper<ListItem> rowMapper = (rs, index) -> {
			ListItem item = new ListItem();
			item.setItemId(rs.getInt("ListItemId"));
			item.setFoodId(rs.getInt("FoodId"));
			item.setListId(rs.getInt("ListId"));
			item.setFoodname(rs.getString("Foodname"));
			item.setQuantity(rs.getInt("Quantity"));
			item.setNotes(rs.getString("Notes"));
			item.setSize(rs.getString("Size"));
			return item;
		};
		
		String sql = "select food.foodname, listitem.listid, listitem.size, listitem.listitemid, listitem.notes, listitem.foodid, listitem.quantity " +
				 " from listitem inner join food " +
				 " on food.foodid = listitem.foodid" +
				 " where listid = " + listId;
		System.out.println(sql);
		List<ListItem> records = jdbcTemplate.query(sql, rowMapper );
		if (records.size() == 0) {
			System.out.println("no elements in the list");
		}
		for (int i =0; i<records.size(); i++) {
			System.out.println(records.get(i).getItemId());
		}
		return records;
	}
	
	public void addItem(ListItem item){
		System.out.println("in DAO addItem");
		jdbcTemplate.update(
				"INSERT INTO ListItem (ListItemId, FoodId, ListId, Quantity, Notes, Size) "
						+ "VALUES (?,?,?,?,?,?)",
						item.getItemId(), item.getFoodId(), item.getListId(), item.getQuantity(),
						item.getNotes(), item.getSize());
	}
	
	public List<String> selectItemList() {
		RowMapper<ListItem> rowMapper = (rs, index) -> {
			ListItem item = new ListItem();
			item.setItemId(rs.getInt("ItemId"));
			item.setFoodId(rs.getInt("FoodId"));
			item.setListId(rs.getInt("ListId"));
			item.setCateg(rs.getString("Categ"));
			item.setQuantity(rs.getInt("Quantity"));

			return item;
		};
		List<ListItem> records = jdbcTemplate.query
				("select * FROM food inner join "
						+ "listitem on listitem.foodid = food.foodid", rowMapper);

		List<String> itemNames = new ArrayList<String>();
		for (int i = 0; i < records.size(); i++) {
			itemNames.add(records.get(i).getFoodName());
		}
		return itemNames;
	}
	

	private static DataSource getDataSource() {
		// Creates a new instance of DriverManagerDataSource and sets
		// the required parameters such as the Jdbc Driver class,
		// Jdbc URL, database user name and password.
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(DAObject.DRIVER);
		dataSource.setUrl(DAObject.JDBC_URL);
		dataSource.setUsername(DAObject.USERNAME);
		dataSource.setPassword(DAObject.PASSWORD);
		return dataSource;
	}
}
