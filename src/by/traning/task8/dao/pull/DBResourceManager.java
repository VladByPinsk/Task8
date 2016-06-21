package by.traning.task8.dao.pull;

import java.util.ResourceBundle;

public class DBResourceManager {
	public static final String DB_BUNDLE_PROPERTIES = "resource.db";
	private static DBResourceManager instance = new DBResourceManager();
	private ResourceBundle resourceBundle;

	private DBResourceManager() {
		resourceBundle = ResourceBundle.getBundle(DB_BUNDLE_PROPERTIES);
	}

	public static DBResourceManager getInstance() {
		return instance;
	}

	public String getProperty(String name) {
		return resourceBundle.getString(name);
	}
}
