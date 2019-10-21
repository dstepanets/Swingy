package unit.swingy.model.characters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class DataBase {

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "org.h2.Driver";
	static final String DB_URL = "jdbc:h2:./src/main/resources/HeroesDB/HeroesH2";
	//  Database credentials
	static final String USER = "user";
	static final String PASS = "";

	private Connection connection = null;
	private Statement statement = null;

	public void connectToDB() {


		try {
			// STEP 1: Register JDBC driver
			Class.forName(JDBC_DRIVER);

			//STEP 2: Open a connection
			System.out.println(">> Connecting to database...");
			connection = DriverManager.getConnection(DB_URL + ";IFEXISTS=TRUE", USER, PASS);

			//test
			printTable();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException esql) {
			System.out.println(esql.getMessage());
//  			esql.printStackTrace();
			resetDB();
		}
	}

	private void resetDB() {
		System.out.println(">> Making new DB");

		try {

			//read sql script to a String Buffer
			FileReader fr = new FileReader(new File("./src/main/resources/HeroesDB/createTable.sql"));
			BufferedReader br = new BufferedReader(fr);
			StringBuffer sb = new StringBuffer();
			String str;
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			br.close();

			//split script to separate sql statements
			String[] commands = sb.toString().split(";");

			//create database
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = connection.createStatement();

			//execute statements, ignoring possible empty ones
			for (String s : commands) {
				if (s.trim().length() > 0) {
					statement.execute(s);
				}
			}

		} catch (SQLException esql) {
			esql.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Hero> getHeroesList(HeroBuilder builder) {

		ArrayList<Hero> heroesList = new ArrayList<>();

		try {
			ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM HEROES");
			rs.next();
			int rows = rs.getInt("count(*)");
			System.out.println(">> Table contains " + rows + " rows");

			rs = statement.executeQuery("SELECT * FROM HEROES");

			while (rs.next()) {
				builder.setName(rs.getString("name"));
				builder.setClas(rs.getString("class"));
				builder.setLevel(rs.getInt("level"));
				builder.setExp(rs.getInt("exp"));
				builder.setHp(rs.getInt("hp"));
				builder.setAttack(rs.getInt("attack"));
				builder.setDefence(rs.getInt("defence"));
				builder.setWeapon(rs.getString("weapon"));
				builder.setArmor(rs.getString("armor"));
				builder.setHelm(rs.getString("helm"));
				heroesList.add(builder.getHero());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}



		return heroesList;
	}

	public void closeConnection() {

		try {
			if (statement != null)
				statement.close();
			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//for debugging
	private void printTable() {

		try {
			statement = connection.createStatement();

			ResultSet rs = statement.executeQuery("SHOW TABLES;");
			while (rs.next()) {
				for (int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++) {
					System.out.print(" " + rs.getMetaData().getColumnName(i) + "=" + rs.getObject(i));
					System.out.println();
				}
			}

			rs = statement.executeQuery("SELECT * FROM heroes");
			while (rs.next()) {
				for (int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++) {
					System.out.print(" " + rs.getMetaData().getColumnName(i) + "=" + rs.getObject(i));
					System.out.println();
				}
			}

		} catch (SQLException esql) {
			esql.printStackTrace();
		}

	}


}
