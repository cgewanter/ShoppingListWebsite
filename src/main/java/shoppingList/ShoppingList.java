package shoppingList;
import java.util.HashMap;

public class ShoppingList {
	
	private static int lastListId =0;
	
	private int listId;
	private String listName;
	private int userId;
	private HashMap<String, ListItem> itemsList;
	
	
	/*public ShoppingList(String name, int userId) {
		this.listId = ++lastListId;
		this.listName = name;
		this.userId = userId;
	}
*/

	public static int getLastListId() {
		return lastListId;
	}

	public int getListId() {
		return listId;
	}

	public void setListId(int listId) {
		this.listId = listId;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public HashMap<String, ListItem> getItemsList() {
		return itemsList;
	}

	public void addItem (ListItem item) {
		itemsList.put(item.getItemId(), item);
	}
}
