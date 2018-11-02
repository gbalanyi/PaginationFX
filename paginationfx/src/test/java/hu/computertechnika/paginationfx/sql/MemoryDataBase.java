/**
 * Copyright 2018 PaginationFX
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software 
 * and associated documentation files (the "Software"), to deal in the Software without 
 * restriction, including without limitation the rights to use, copy, modify, merge, publish, 
 * distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or 
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR 
 * ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
 * SOFTWARE. 
*/
package hu.computertechnika.paginationfx.sql;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;

/**
 * @author Gábor Balanyi
 */
public class MemoryDataBase {

	public static int DEFAULT_PERSON_NUMBER = 10000;

	private static final Logger LOGGER = LoggerFactory.getLogger(MemoryDataBase.class);

	@Getter
	private static Connection connection;

	private static int personNumber = DEFAULT_PERSON_NUMBER;

	public static void create(int personNumber) {

		MemoryDataBase.personNumber = personNumber;
		create();

	}

	public static void create() {

		try {

			if (connection != null && !connection.isClosed()) {
				return;
			}

			connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
			Statement statment = connection.createStatement();
			statment.addBatch(
					"CREATE TABLE person (ID BIGINT AUTO_INCREMENT PRIMARY KEY, first_name VARCHAR(50), last_name VARCHAR(50), age INT, gender VARCHAR(10), dob DATE);");
			statment.addBatch("CREATE INDEX idx_first_name ON person(first_name);");
			statment.addBatch("CREATE INDEX idx_last_name ON person(last_name);");
			statment.addBatch("CREATE INDEX idx_age ON person(age);");
			statment.addBatch("CREATE INDEX idx_gender ON person(gender);");
			statment.addBatch("CREATE INDEX idx_dob ON person(dob);");

			statment.executeBatch();
			statment.close();

			createDummyPersons();

		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	private static void createDummyPersons() {

		BufferedReader reader = null;
		try {

			List<String> male = new ArrayList<>();
			reader = new BufferedReader(new FileReader("src/test/resources/male.txt"));
			String line;
			while ((line = reader.readLine()) != null) {
				male.add(line);
			}
			reader.close();

			List<String> female = new ArrayList<>();
			reader = new BufferedReader(new FileReader("src/test/resources/female.txt"));
			while ((line = reader.readLine()) != null) {
				female.add(line);
			}
			reader.close();

			List<String> lastnames = new ArrayList<>();
			reader = new BufferedReader(new FileReader("src/test/resources/lastnames.txt"));
			while ((line = reader.readLine()) != null) {
				lastnames.add(line);
			}
			reader.close();

			Statement statement = connection.createStatement();
			Random random = new Random();
			for (int i = 0; i < personNumber; i++) {

				boolean isMale = random.nextInt() % 2 == 0;
				String gender;
				String firstName;
				if (isMale) {

					firstName = male.get(random.nextInt(male.size()));
					gender = "MALE";

				} else {

					firstName = female.get(random.nextInt(female.size()));
					gender = "FEMALE";

				}
				String lastName = lastnames.get(random.nextInt(lastnames.size()));
				int age = random.nextInt(99) + 1;

				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.YEAR, -age);
				calendar.set(Calendar.MONTH, Calendar.JANUARY);
				calendar.set(Calendar.DATE, 1);
				calendar.add(Calendar.DATE, random.nextInt(365));

				statement.addBatch("INSERT INTO person(first_name, last_name, age, gender, dob) VALUES('" + firstName
						+ "', '" + lastName + "', " + age + ", '" + gender + "', '"
						+ new Date(calendar.getTimeInMillis()).toString() + "');");

			}
			statement.executeBatch();
			statement.close();

		} catch (IOException | SQLException e) {
			LOGGER.error(e.getMessage(), e);
		} finally {

			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}

		}

	}

}
