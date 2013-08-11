package matcher;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import message.EmailMessenger;

import com.example.lostfound.LostfoundApplication;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class MatchEngine {

	WordNetDatabase database; // / = WordNetDatabase.getFileInstance();

	Connection conn = null;
	String url = "jdbc:mysql://localhost:3306/lostfounddb";
	String user = "root";
	String password = "h2so4";

	int numOfRows = 0;
	int currentRowNum = -1;

	/**
	 * list of all unwanted words to be removed from a string
	 */
	public static String[] nonKeywords = { "on", "of", "a", "the", "about",
			"in", "my", "it", "its", "with", "has", "had", "for", "or", "and",
			"no", "yes", "am", "i", "ie", "your", "you", "which", "is", "me",
			"that", "there", "was" };

	/**
	 * every initialization call is made here
	 */
	public void init() { 
		System.out.println("MatchEngine -> init()"); 
		initDict();
		initDatabaseConn();
	}

	public void run() {
		System.out.println("MatchEngine -> run()"); 
		init(); // initialize the wordnet and other stuffs
		runMatch(); 
		System.out.println("MatchEngine -> run()->end"); 
	}

	public void runMatch() {
		for (int i = 0; i < numOfRows; i++) {
			Item item = getNextRequest(getNextRequetRowNum());

			if (item == null)
				continue;

			Item[] candidates = getCandidateMatches(item, true);
			if (matchCandidates(item, candidates) >= 60) {
				continue;
			} else {
				candidates = getCandidateMatches(item, false);
				matchCandidates(item, candidates);
			}
		}
		numOfRows = getNumRows(); 
		System.out.println("runMatch()"); 
	}

	public float matchCandidates(Item item, Item[] candidates) {
		float matchVal = 0.0f;

		for (Item t : candidates) {
			matchVal = match(item, t);
			if (matchVal >= 60) {
				System.out.println("Match found: " + matchVal + "%");
				System.out.println("Request Item:\n" + item
						+ "\nFound Item: \n" + t);
				markMarched(item, t);
				break;
			}
		}

		return matchVal;
	}

	public void markMarched(Item req, Item found) {
		String queryStr = "UPDATE tbl_request SET matched =" + found.id
				+ " WHERE id =" + req.id + "; ";
		Statement st;

		try {
			st = conn.createStatement();
			st.executeUpdate(queryStr);

			sendMessage(req, found);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * sends an email to client, telling that their item is found
	 */
	public void sendMessage(Item req, Item found) {
		final EmailMessenger messenger = new EmailMessenger();

		String name = "", address = "", city = "", fCode = "";

		String clientQuery = "SELECT * FROM tbl_client WHERE request = "
				+ req.id;

		String itemQuery = "SELECT tbl_item.FoundCode, tbl_city.Description  "
				+ "FROM tbl_item, tbl_city WHERE tbl_item.id = " + found.id + 
				" AND tbl_city.id = tbl_item.City"; 

		Statement clientST, itemST;

		try {
			clientST = conn.createStatement();
			itemST = conn.createStatement();

			ResultSet clientRS = clientST.executeQuery(clientQuery);
			ResultSet itemRS = itemST.executeQuery(itemQuery);

			if (clientRS.next()) {
				name = clientRS.getString("FirstNames");
				address = clientRS.getString("Email");
			}

			if (itemRS.next()) {
				city = itemRS.getString("Description");
				fCode = itemRS.getString("FoundCode");
			}

			// /set the email...
			messenger.setMessage(EmailMessenger.getFoundMessage(name, city, fCode));
			messenger.setName(name);
			messenger.setToAddress(address);

			LostfoundApplication.EmailAsyncSender.execute(new Runnable() {
				public void run() {
					messenger.sendMessage();
				}// run
			});
		} catch (RejectedExecutionException e) {
			// getWindow().showNotification(e.getMessage()); 
			e.printStackTrace(); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * helps print string values in arry for testing purposes
	 * 
	 * @param arr
	 *            the input array of string to be printed
	 */
	public void printArr(String[] arr) {
		for (String s : arr)
			System.out.print(s + " ");
		System.out.println();
	}

	/**
	 * extracts the keyswords from a description string
	 * 
	 * @param str
	 *            - the input/description string
	 * @return an array of keywords in the input string
	 */
	public String[] getKeywords(String str) {
		String[] strArr = removePunctuations(str).split("\\p{Space}");
		return removeNonKeywords(strArr, MatchEngine.nonKeywords);
	}

	/**
	 * converts the input string to all lower case and removes all punctions and
	 * new line char
	 * 
	 * @param str
	 *            the string to remove punctuations from
	 * @return a string without new line and punctuation characters
	 */
	public String removePunctuations(String str) {
		return str.toLowerCase().replaceAll("\\p{Punct}+", " ")
				.replaceAll(" +", " ").trim();
	}

	/**
	 * - removes unwanted words from the input param
	 * 
	 * @param arrStr
	 *            - the input array of strings
	 * @param words
	 *            - the array that contains the unwanted words that would be
	 *            removed
	 * @return
	 */
	public String[] removeNonKeywords(String[] arrStr, String[] words) {

		List<String> result = new ArrayList<String>(Arrays.asList(arrStr));

		String[] tmp = new String[] {};

		result.removeAll(Arrays.asList(words));
		tmp = result.toArray(tmp);

		return tmp;
	}

	/**
	 * give it a word and it would return an array of its synonyms
	 * 
	 * @param str
	 *            the word to look for the synonyms
	 * @return the array that contains the synonyms of the input word. If there
	 *         are no synonyms for the word, an arry containing just that word
	 *         is returned
	 */
	public String[] getSynonyms(String str) {
		Synset[] synsets = database.getSynsets(str, SynsetType.NOUN);
		if (synsets.length != 0) {
			return synsets[0].getWordForms();
		}
		return new String[] { str };
	}

	/**
	 * Initializes the wordnet dictionary
	 */
	public void initDict() {
		System.setProperty("wordnet.database.dir",
				"C:\\Program Files (x86)\\WordNet\\2.1\\dict");
		database = WordNetDatabase.getFileInstance(); 
		System.out.println("MatchEngine -> initDict()"); 
	}

	/**
	 * Initializes the database connection
	 */
	public void initDatabaseConn() {
		try {
			conn = DriverManager.getConnection(url, user, password);
			numOfRows = getNumRows();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * given an item name, this returns the name of the item with unwanted words
	 * removed
	 * 
	 * @param name
	 *            the name to work with
	 * @return a new name without the unwanted words. If name comprises more
	 *         than a word, then, the words are concatenated to form a single
	 *         string
	 */
	public String getNormalizedItemName(String name) {
		String[] strArr = getKeywords(name);
		String str = "";
		for (String s : strArr)
			str += s + " ";
		return str.trim();
	}

	/**
	 * 
	 * @param rowNum
	 * @return
	 */
	public Item getNextRequest(int rowNum) {

		String queryStr = "SELECT id, item_name, description, city FROM  tbl_request "
				+ "WHERE matched is NULL "
				+ "ORDER BY date_of_loss LIMIT "
				+ rowNum + ", " + rowNum + 1;
		Statement st;

		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(queryStr);

			if (rs.next()) {
				return new RequestItem(rs.getString("id"),
						getNormalizedItemName(rs.getString("item_name")),
						getKeywords(rs.getString("description")),
						rs.getString("city"));
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * the total number of rows in the request table that has yet to be serviced
	 * 
	 * @return the total number of rows not yet matched
	 */
	public int getNumRows() {
		String queryStr = "select count(*) from tbl_request where matched is NULL;";
		Statement st;
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(queryStr);
			if (rs.next())
				return Integer.parseInt(rs.getString("count(*)"));
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * - gets the next row number for the next request row to be dealt with
	 * 
	 * @return the next row number to attend to
	 */
	public int getNextRequetRowNum() {
		currentRowNum++;
		if (currentRowNum >= numOfRows) {
			numOfRows = getNumRows();
			currentRowNum = 0;
		}

		return currentRowNum;
	}

	/**
	 * takes an array of strings and concartenates them to form search criteria
	 * on the query to the database.
	 * 
	 * This is useful to limit the search for items that satisfy similarity with
	 * a name on the request item
	 * 
	 * @param arrStr
	 *            the array that contains the words to match in the itemname
	 *            field
	 * @return a query string for the arrStr arguement s
	 */
	public String makeQuery(String[] arrStr) {
		String str = "SELECT id, ItemName, Description, City FROM tbl_item WHERE ";
		for (int i = 0; i < arrStr.length; i++) {
			str += (i != arrStr.length - 1) ? "ItemName like '%" + arrStr[i]
					+ "%' OR " : ("ItemName like '%" + arrStr[i] + "%' ;");
		}
		return str;
	}

	public String makeQuery(String[] arrStr, String cityId) {
		String str = "SELECT id, ItemName, Description, City FROM tbl_item "
				+ "WHERE City = " + cityId + " AND ";
		for (int i = 0; i < arrStr.length; i++) {
			str += (i != arrStr.length - 1) ? "ItemName like '%" + arrStr[i]
					+ "%' OR " : ("ItemName like '%" + arrStr[i] + "%' ;");
		}
		return str;
	}

	/**
	 * given a request item, it returns an array of possible candidate found
	 * items based on the name field of those items
	 * 
	 * @param requItem
	 *            the name field of this item is used to search for possible
	 *            items with synonymous names
	 * @param bCity
	 *            if this is true, then query limits search to the city of the
	 *            the requItem. If false, then returned query include search
	 *            from all cities
	 * 
	 * @return an array of possible items that match the param item. If none is
	 *         found, then an empty array is returned
	 */
	public Item[] getCandidateMatches(Item requItem, boolean bCity) {
		// String queryStr = makeQuery(getSynonyms(requItem.name));
		String queryStr = bCity ? makeQuery(getSynonyms(requItem.name),
				requItem.cityCode) : makeQuery(getSynonyms(requItem.name));
		Statement st;

		ArrayList<Item> temp = new ArrayList<Item>();
		Item[] result = new Item[] {};

		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(queryStr);

			while (rs.next()) {
				temp.add(new FoundItem(rs.getString("id"),
						getNormalizedItemName(rs.getString("ItemName")),
						getKeywords(rs.getString("Description")), rs
								.getString("City")));
			}
			result = temp.toArray(result);
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return new Item[] {};
		}
	}

	/**
	 * given a request item and a found item, this tries to match their
	 * descriptions to see if they both refer to the same item
	 * 
	 * @param req
	 *            a
	 * @param found
	 */
	public float match(Item req, Item found) {
		// System.out.println("Request Item:\n" + req + "\nFound Item: \n" +
		// found);

		float matchPerc = 0.0f; // how much of the found item description
								// matches the request item
		ArrayList<String> fList = new ArrayList<String>(
				Arrays.asList(found.keywords));
		/**
		 * list of words that are not found in the found item list are added to
		 * / this list and would be checked for synonyms later
		 */
		ArrayList<String> otherList = new ArrayList<String>();
		for (String s : req.keywords) {
			if (fList.contains(s))
				matchPerc += (1.0f / req.keywords.length) * 100.0f;
			else {
				otherList.add(s);
			}
		}

		if (matchPerc >= 20 && matchPerc < 60) {
			for (String s : otherList) {
				matchPerc += matchSynonyms(Arrays.asList(getSynonyms(s)),
						fList, req.keywords.length);
			}
		}
		// System.out.println("match percent: " + matchPerc + "\n");

		return matchPerc;
	}

	/**
	 * matches the synonyms of words against the found item list of keywords if
	 * a match is found, only 8% is added to match value
	 * 
	 * @param reqList
	 * @param foundList
	 * @param l
	 *            the length of the original request keywords list
	 * @return
	 */
	public float matchSynonyms(List<String> reqList,
			ArrayList<String> foundList, int l) {
		for (String s : reqList) {
			if (foundList.contains(s))
				return (0.8f / l) * 100.0f;
		}

		return 0.0f;
	}

	/**
	 * ******************************MAIN****************************
	 * 
	 * @param args
	 */
//	public static void main(String[] args) {
//		new MatchEngine().run();
//	}

	/**
	 * needs to close database connection as a final step before class is
	 * destroyed
	 */
	@Override
	protected void finalize() {
		try {
			super.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Represents an item from the database, either a request from a client or a
	 * found item from an admin
	 * 
	 * @author Chinedu Eze
	 */
	private abstract class Item {
		public String name;
		public String[] keywords;
		public String id;
		public String cityCode;

		@Override
		public String toString() {
			String str = "ID: " + id + "\nName: " + name + "\nCity Code: "
					+ cityCode + "\nDesc: ";
			for (String s : keywords)
				str += s + " ";

			return str;
		}
	}

	private class RequestItem extends Item {
		public RequestItem(String n, String[] k) {
			name = n;
			keywords = k;
		}

		/*
		 * public RequestItem(String id, String n, String[] k) { this(n, k);
		 * this.id = id; }
		 */

		public RequestItem(String id, String n, String[] k, String ctCode) {
			this(n, k);
			this.id = id;
			this.cityCode = ctCode;
		}
	}

	private class FoundItem extends Item {
		public FoundItem(String n, String[] k) {
			name = n;
			keywords = k;
		}

		/*
		 * public FoundItem(String id, String n, String[] k) { this(n, k);
		 * this.id = id; }
		 */

		public FoundItem(String id, String n, String[] k, String ctCode) {
			this(n, k);
			this.id = id;
			this.cityCode = ctCode;
		}
	}

}
