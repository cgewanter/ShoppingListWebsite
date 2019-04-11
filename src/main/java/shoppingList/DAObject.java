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
	
	public void addItem(ListItem item){
		System.out.println("in DAO addItem");
		jdbcTemplate.update(
				"INSERT INTO ListItem (ListItemId, FoodId, ListId, Quantity, Notes, Size) "
			  + "VALUES (?,?,?,?,?,?)",
				item.getItemId(), item.getFoodId(), item.getListId(), item.getQuantity(),
				item.getNotes(), item.getSize());
	}
	
	public void addUser(User user) {
		System.out.println("in DAO addUser");
		jdbcTemplate.update(
				"insert into users (userid, firstname, lastname, username) values (?,?,?,?)",
				null, user.getFirstname(),user.getLastname(), user.getUsername()
				);
	}
	
	public void addList(ShopList list, User user) {
		System.out.println("in addshoplist");
		System.out.println("user id: " + list.getUserId());
		jdbcTemplate.update(
				"insert into lists (listid, listtitle, categ, userid) values (?,?,?,?)",
				 null, list.getListTitle(), list.getCateg(), list.getUserId());
		System.out.println("added a list!");
	}
	
	public List<ShopList> getLists() {
		System.out.println("in get list in dao");
		RowMapper<ShopList> rowMapper = (rs, index) -> {
			ShopList sl = new ShopList();
			sl.setListId(rs.getInt("ListId"));
			sl.setListTitle(rs.getString("ListTitle"));
			sl.setUserId(rs.getInt("UserId"));
			return sl;
		};
		List<ShopList> records = jdbcTemplate.query
				("select * FROM lists", rowMapper);
        for (int i = 0; i < records.size(); i++) {
             System.out.println(records.get(i).getListTitle());
        }
        return records;
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
