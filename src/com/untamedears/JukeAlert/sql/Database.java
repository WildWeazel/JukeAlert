package com.untamedears.JukeAlert.sql;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.avaje.ebeaninternal.server.lib.sql.DataSourceException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static com.untamedears.JukeAlert.JukeAlert.*;

/**
 * MySql database
 *
 * @author Dylan
 */
public class Database {

	private String host;
	private String db;
	private String user;
	private String password;
	private String prefix;
	private Connection connection;
	private Logger logger;

	/**
	 * Constructor
	 * @param host - host of the database
	 * @param db - name of the database
	 * @param user - user to login as in the db
	 * @param password - password for the user
	 * @param prefix - the prefix added to the table names to help avoid name conflicts i presume
	 * @param logger - the JukeAlert logger instance so we can log messages/errors
	 */
	public Database(String host, String db, String user, String password, String prefix, Logger logger) {
		this.host = host;
		this.db = db;
		this.user = user;
		this.password = password;
		this.prefix = prefix;
		this.logger = logger;
	}

	public String getDb() {
		return db;
	}

	public String getHost() {
		return host;
	}

	public String getPassword() {
		return password;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getUser() {
		return user;
	}

	
	/**
	 * Connects to the database.
	 *
	 * @since 0.1
	 */
	public boolean connect() {
		String jdbc = "jdbc:mysql://" + host + "/" + db + "?user=" + user + "&password=" + password;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			throw new DataSourceException("Failed to initialize JDBC driver.");
		}
		try {
			connection = DriverManager.getConnection(jdbc);
			this.logger.log(Level.INFO, "Connected to database!");
			return true;
		} catch (SQLException ex) { //Error handling below:
			this.logger.log(Level.SEVERE, "Could not connnect to the database!", ex);
			return false;
		}
	}

	/**
	 * Closes the database connection.
	 *
	 * @since 0.1
	 */
	public void close() {
		try {
			connection.close();
		} catch (SQLException ex) {
			this.logger.log(Level.SEVERE, "An error occured while closing the connection.", ex);
		}
	}

	/**
	 * Are we connected to the database?
	 *
	 * @return Connected
	 * @throws SQLException
	 */
	public boolean isConnected() {
		try {
			return connection.isValid(5);
		} catch (SQLException ex) {
			this.logger.log(Level.SEVERE, "isConnected error!", ex);
		}
		return false;
	}

	/**
	 * Executes an SQL query. (No output)
	 *
	 * @param sql The SQL query as a string.
	 */
	public void execute(String sql) {
		try {
			if (isConnected()) {
				connection.prepareStatement(sql).executeUpdate();
			} else {
				connect();
				execute(sql);
			}
		} catch (SQLException ex) {
			this.logger.log(Level.SEVERE, "Could not execute SQL statement!", ex);
		}
	}

	/**
	 * Gets a result set returned from an SQL query.
	 *
	 * @param sql
	 * @return ResultSet
	 */
	public ResultSet getResultSet(String sql) {
		try {
			if (isConnected()) {
				return connection.createStatement().executeQuery(sql);
			} else {
				connect();
				return getResultSet(sql);
			}
		} catch (SQLException ex) {
			this.logger.log(Level.SEVERE, "Could not execute SQL statement!", ex);
			return null;
		}
	}

	/**
	 * Gets a string from the database.
	 *
	 * @param sql
	 * @return String
	 *
	 * @since 0.1
	 */
	public String getString(String sql) {
		try {
			ResultSet result = getResultSet(sql);
			result.next();
			return result.getString(1);
		} catch (SQLException ex) {
			this.logger.log(Level.SEVERE, "Could not execute SQL statement!", ex);
		}
		return null;
	}

	/**
	 * Gets an integer from the database.
	 *
	 * @param sql
	 * @return int
	 */
	public int getInteger(String sql) {
		try {
			ResultSet result = getResultSet(sql);
			result.next();
			return result.getInt(1);
		} catch (SQLException ex) {
			this.logger.log(Level.SEVERE, "Could not execute SQL statement!", ex);
		}
		return 0;
	}

	/**
	 * Gets a double from the database.
	 *
	 * @param sql
	 * @return double
	 */
	public double getDouble(String sql) {
		try {
			ResultSet result = getResultSet(sql);
			result.next();
			return result.getDouble(1);
		} catch (SQLException ex) {
			this.logger.log(Level.SEVERE, "Could not execute SQL statement!", ex);
		}
		return 0;
	}

	/**
	 * Gets a boolean from the database.
	 *
	 * @param sql
	 * @return boolean
	 */
	public boolean getBoolean(String sql) {
		try {
			ResultSet result = getResultSet(sql);
			result.next();
			boolean returnValue = result.getBoolean(1);
			return returnValue;
		} catch (SQLException ex) {
			this.logger.log(Level.SEVERE, "Could not execute SQL statement!", ex);
		}
		return false;
	}

	/**
	 * Gets all of the values in a specified column as a list.
	 *
	 * @param sql
	 * @return List of column values
	 */
	public List<String> getColumn(String sql) {
		List<String> coldata = new ArrayList<String>();
		try {
			ResultSet result = getResultSet(sql);
			while (result.next()) {
				coldata.add(result.getString(1));
			}
			return coldata;
		} catch (SQLException ex) {
			this.logger.log(Level.SEVERE, "Could not execute SQL statement!", ex);
		}
		return null;
	}
}