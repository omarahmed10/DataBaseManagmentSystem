package jdbc;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;
import java.sql.Statement;

public class CLI {
	private static Scanner input = new Scanner(System.in);

	public static void main(String[] args) throws SQLException {
		String tmp = System.getProperty("java.io.tmpdir");
		String protocol = "";
		String command = "";
		Driver driver = new Driver();
		Properties info = new Properties();
		File dbDir = new File(tmp + "/jdbc/" + Math.round((((float) Math.random()) * 100000)));
		info.put("path", dbDir.getAbsoluteFile());

		while (!(protocol.equals("xmldb") || protocol.equals("altdb"))) {
			System.out.println("Enter protocol type , the only supported ones are \"xmldb\" & \"altdb\"");
			protocol = input.nextLine();
		}

		Connection connection = driver.connect("jdbc:" + protocol + "://localhost", info);
		Statement statement = connection.createStatement();
		System.out.println("You have a driver , an established connection and an Statement to execute sql commands");
		
		while (!command.equals(-1)) {
			System.out.println("\nEnter sql command to execute :");
			command = input.nextLine();

			try {
				if (statement.execute(command) == true) {
					System.out.println(statement.getResultSet());
				} else {
					System.out.println(statement.getUpdateCount());
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
