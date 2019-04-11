package shoppingList;
public enum StoreCategory {
		
		GROCERY("Grocery"),
		HARDWARE("Hardware"),
		PHARMACY("Pharmacy"),
		BAKERY("Bakery");
		
		private StoreCategory categ;
		private String categStr;
		
		private  StoreCategory() {}
		
		private StoreCategory(String category) {
			categ = StoreCategory.valueOf(category);
			categStr = category;
		}
		
		public String getCategString() {
			return categStr;
		}
		
		public StoreCategory getCateg() {
			return categ;
		}
	}

