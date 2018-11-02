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

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.computertechnika.paginationfx.filter.CalendarJDBCFilter;
import hu.computertechnika.paginationfx.filter.ComparableJDBCFilter;
import hu.computertechnika.paginationfx.filter.DateJDBCFilter;
import hu.computertechnika.paginationfx.filter.FilterType;
import hu.computertechnika.paginationfx.filter.LocalDateJDBCFilter;
import hu.computertechnika.paginationfx.filter.StringJDBCFilter;
import hu.computertechnika.paginationfx.filter.TemporalType;
import hu.computertechnika.paginationfx.sort.JDBCSort;
import hu.computertechnika.paginationfx.sort.SortType;

/**
 * @author Gábor Balanyi
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JDBCDataSourceTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(JDBCDataSource.class);

	private static Connection connection;

	private static AbstractJDBCDataSource<Object[]> dataSource;

	@BeforeClass
	public static void init() {

		MemoryDataBase.create();
		connection = MemoryDataBase.getConnection();
		createDataSource();

	}

	private static void createDataSource() {

		dataSource = new JDBCDataSource<>(connection);
		// dataSource.setSelectExpression("*");
		// dataSource.setFromExpression("person");

	}

	@Test
	public void test00CheckSelect() {

		try {
			dataSource.executeQuery();
		} catch (Exception e) {

			LOGGER.debug(e.getMessage());
			Assert.assertTrue(e instanceof IllegalStateException);

		}

	}

	@Test
	public void test01CheckFrom() {

		dataSource.addSelectExpression("*");
		try {
			dataSource.executeQuery();
		} catch (Exception e) {

			LOGGER.debug(e.getMessage());
			Assert.assertTrue(e instanceof IllegalStateException);

		}

	}

	@Test
	public void test02FirstPage() {

		dataSource.setFromExpression("person");
		dataSource.setOffset(0);
		dataSource.setMaxResults(100);
		List<Object[]> result = dataSource.executeQuery();

		try {

			PreparedStatement prepareStatement = connection
					.prepareStatement("SELECT * FROM person LIMIT 100 OFFSET 0;");
			ResultSet resultSet = prepareStatement.executeQuery();

			resultSet.next();
			Assert.assertTrue(result.get(0)[0].equals(resultSet.getLong(1)));

			resultSet.last();
			Assert.assertTrue(result.get(result.size() - 1)[0].equals(resultSet.getLong(1)));

			resultSet.close();
			prepareStatement.close();

		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	@Test
	public void test03Count() {

		long rowNumber = dataSource.executeCountQuery();
		try {

			PreparedStatement prepareStatement = connection.prepareStatement("SELECT COUNT(*) FROM person;");
			ResultSet resultSet = prepareStatement.executeQuery();

			resultSet.next();
			Assert.assertTrue(rowNumber == resultSet.getLong(1));

			resultSet.close();
			prepareStatement.close();

		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	@Test
	public void test04LastPage() {

		long rowNumber = dataSource.executeCountQuery();
		dataSource.setOffset(rowNumber - dataSource.getMaxResults());
		List<Object[]> result = dataSource.executeQuery();

		try {

			PreparedStatement prepareStatement = connection.prepareStatement(
					"SELECT * FROM person LIMIT 100 OFFSET " + (rowNumber - dataSource.getMaxResults()) + ";");
			ResultSet resultSet = prepareStatement.executeQuery();

			resultSet.next();
			Assert.assertTrue(result.get(0)[0].equals(resultSet.getLong(1)));

			resultSet.last();
			Assert.assertTrue(result.get(result.size() - 1)[0].equals(resultSet.getLong(1)));

			resultSet.close();
			prepareStatement.close();

		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	@Test
	public void test05NoneFilter() {

		StringJDBCFilter filter = new StringJDBCFilter("first_name");
		dataSource.addWhereExpression(filter);
		dataSource.setOffset(0);
		List<Object[]> result = dataSource.executeQuery();

		try {

			PreparedStatement prepareStatement = connection
					.prepareStatement("SELECT * FROM person LIMIT 100 OFFSET 0;");
			ResultSet resultSet = prepareStatement.executeQuery();

			resultSet.next();
			Assert.assertTrue(result.get(0)[0].equals(resultSet.getLong(1)));

			resultSet.last();
			Assert.assertTrue(result.get(result.size() - 1)[0].equals(resultSet.getLong(1)));

			resultSet.close();
			prepareStatement.close();

		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	@Test
	public void test06EqualFilterWithNull() {

		dataSource.getWhereExpressions().get(0).setFilterType(FilterType.EQUAL);
		List<Object[]> result = dataSource.executeQuery();

		try {

			PreparedStatement prepareStatement = connection
					.prepareStatement("SELECT * FROM person WHERE first_name is NULL LIMIT 100 OFFSET 0;");
			ResultSet resultSet = prepareStatement.executeQuery();

			resultSet.next();
			Assert.assertTrue(result.isEmpty() && !resultSet.next());

			resultSet.close();
			prepareStatement.close();

		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	@Test
	public void test07EqualFilter() {

		((StringJDBCFilter) dataSource.getWhereExpressions().get(0)).setFilterValues(new String[] { "John" });
		List<Object[]> result = dataSource.executeQuery();

		try {

			PreparedStatement prepareStatement = connection
					.prepareStatement("SELECT * FROM person WHERE first_name = 'John' LIMIT 100 OFFSET 0;");
			ResultSet resultSet = prepareStatement.executeQuery();

			resultSet.next();
			Assert.assertTrue(result.get(0)[0].equals(resultSet.getLong(1)));

			resultSet.last();
			Assert.assertTrue(result.get(result.size() - 1)[0].equals(resultSet.getLong(1)));

			resultSet.close();
			prepareStatement.close();

		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	@Test
	public void test08StartsWithFilter() {

		StringJDBCFilter filter = (StringJDBCFilter) dataSource.getWhereExpressions().get(0);
		filter.setFilterType(FilterType.STARTS_WITH);
		filter.setFilterValues(new String[] { "Jo" });
		List<Object[]> result = dataSource.executeQuery();

		try {

			PreparedStatement prepareStatement = connection
					.prepareStatement("SELECT * FROM person WHERE first_name LIKE 'Jo%' LIMIT 100 OFFSET 0;");
			ResultSet resultSet = prepareStatement.executeQuery();

			resultSet.next();
			Assert.assertTrue(result.get(0)[0].equals(resultSet.getLong(1)));

			resultSet.last();
			Assert.assertTrue(result.get(result.size() - 1)[0].equals(resultSet.getLong(1)));

			resultSet.close();
			prepareStatement.close();

		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	@Test
	public void test09ContainsFilter() {

		StringJDBCFilter filter = (StringJDBCFilter) dataSource.getWhereExpressions().get(0);
		filter.setFilterType(FilterType.CONTAINS);
		filter.setFilterValues(new String[] { "oh" });
		List<Object[]> result = dataSource.executeQuery();

		try {

			PreparedStatement prepareStatement = connection
					.prepareStatement("SELECT * FROM person WHERE first_name LIKE '%oh%' LIMIT 100 OFFSET 0;");
			ResultSet resultSet = prepareStatement.executeQuery();

			resultSet.next();
			Assert.assertTrue(result.get(0)[0].equals(resultSet.getLong(1)));

			resultSet.last();
			Assert.assertTrue(result.get(result.size() - 1)[0].equals(resultSet.getLong(1)));

			resultSet.close();
			prepareStatement.close();

		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	@Test
	public void test10EndsWithFilter() {

		StringJDBCFilter filter = (StringJDBCFilter) dataSource.getWhereExpressions().get(0);
		filter.setFilterType(FilterType.ENDS_WITH);
		filter.setFilterValues(new String[] { "hn" });
		List<Object[]> result = dataSource.executeQuery();

		try {

			PreparedStatement prepareStatement = connection
					.prepareStatement("SELECT * FROM person WHERE first_name LIKE '%hn' LIMIT 100 OFFSET 0;");
			ResultSet resultSet = prepareStatement.executeQuery();

			resultSet.next();
			Assert.assertTrue(result.get(0)[0].equals(resultSet.getLong(1)));

			resultSet.last();
			Assert.assertTrue(result.get(result.size() - 1)[0].equals(resultSet.getLong(1)));

			resultSet.close();
			prepareStatement.close();

		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	@Test
	public void test11InFilter() {

		StringJDBCFilter filter = (StringJDBCFilter) dataSource.getWhereExpressions().get(0);
		filter.setFilterType(FilterType.IN);
		filter.setFilterValues(new String[] { "John", "Jane" });
		List<Object[]> result = dataSource.executeQuery();

		try {

			PreparedStatement prepareStatement = connection
					.prepareStatement("SELECT * FROM person WHERE first_name IN ('John', 'Jane') LIMIT 100 OFFSET 0;");
			ResultSet resultSet = prepareStatement.executeQuery();

			resultSet.next();
			Assert.assertTrue(result.get(0)[0].equals(resultSet.getLong(1)));

			resultSet.last();
			Assert.assertTrue(result.get(result.size() - 1)[0].equals(resultSet.getLong(1)));

			resultSet.close();
			prepareStatement.close();

		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	@Test
	public void test12InWithNullFilter() {

		StringJDBCFilter filter = (StringJDBCFilter) dataSource.getWhereExpressions().get(0);
		filter.setFilterType(FilterType.IN);
		filter.setFilterValues(new String[] { "John", "Jane", null });
		List<Object[]> result = dataSource.executeQuery();

		try {

			PreparedStatement prepareStatement = connection.prepareStatement(
					"SELECT * FROM person WHERE first_name IN ('John', 'Jane', NULL) LIMIT 100 OFFSET 0;");
			ResultSet resultSet = prepareStatement.executeQuery();

			resultSet.next();
			Assert.assertTrue(result.get(0)[0].equals(resultSet.getLong(1)));

			resultSet.last();
			Assert.assertTrue(result.get(result.size() - 1)[0].equals(resultSet.getLong(1)));

			resultSet.close();
			prepareStatement.close();

		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	@Test
	public void test13CountWithFilter() {

		long rowNumber = dataSource.executeCountQuery();
		try {

			PreparedStatement prepareStatement = connection
					.prepareStatement("SELECT COUNT(*) FROM person WHERE first_name IN ('John', 'Jane', NULL);");
			ResultSet resultSet = prepareStatement.executeQuery();

			resultSet.next();
			Assert.assertTrue(rowNumber == resultSet.getLong(1));

			resultSet.close();
			prepareStatement.close();

		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	@Test
	public void test14MultiFilter() {

		StringJDBCFilter filter = (StringJDBCFilter) dataSource.getWhereExpressions().get(0);
		filter.setFilterType(FilterType.IN);
		filter.setFilterValues(new String[] { "John", "Jane" });

		ComparableJDBCFilter<Integer> filter2 = new ComparableJDBCFilter<>("age");
		filter2.setFilterType(FilterType.GREATER_THAN_OR_EQUAL);
		filter2.setFilterValues(new Integer[] { 18 });
		dataSource.addWhereExpression(filter2);

		List<Object[]> result = dataSource.executeQuery();

		try {

			PreparedStatement prepareStatement = connection.prepareStatement(
					"SELECT * FROM person WHERE first_name IN ('John', 'Jane') AND age >= 18 LIMIT 100 OFFSET 0;");
			ResultSet resultSet = prepareStatement.executeQuery();

			resultSet.next();
			Assert.assertTrue(result.get(0)[0].equals(resultSet.getLong(1)));

			resultSet.last();
			Assert.assertTrue(result.get(result.size() - 1)[0].equals(resultSet.getLong(1)));

			resultSet.close();
			prepareStatement.close();

		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	@Test
	public void test15RemoveFilter() {

		dataSource.removeWhereExpression(dataSource.getWhereExpressions().get(0));
		List<Object[]> result = dataSource.executeQuery();

		try {

			PreparedStatement prepareStatement = connection
					.prepareStatement("SELECT * FROM person WHERE age >= 18 LIMIT 100 OFFSET 0;");
			ResultSet resultSet = prepareStatement.executeQuery();

			resultSet.next();
			Assert.assertTrue(result.get(0)[0].equals(resultSet.getLong(1)));

			resultSet.last();
			Assert.assertTrue(result.get(result.size() - 1)[0].equals(resultSet.getLong(1)));

			resultSet.close();
			prepareStatement.close();

		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	@Test
	public void test16Sort() {

		JDBCSort sort = new JDBCSort("last_name");
		sort.setSortType(SortType.DESCENDING);
		dataSource.addOrderExpression(sort.createExpression());

		List<Object[]> result = dataSource.executeQuery();

		try {

			PreparedStatement prepareStatement = connection.prepareStatement(
					"SELECT * FROM person WHERE age >= 18 ORDER BY last_name DESC { LIMIT 100 OFFSET 0 };");
			ResultSet resultSet = prepareStatement.executeQuery();

			resultSet.next();
			Assert.assertTrue(result.get(0)[2].equals(resultSet.getString(3)));

			resultSet.last();
			Assert.assertTrue(result.get(result.size() - 1)[2].equals(resultSet.getString(3)));

			resultSet.close();
			prepareStatement.close();

		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	@Test
	public void test17MultiSort() {

		JDBCSort sort = new JDBCSort("first_name");
		sort.setSortType(SortType.ASCENDING);
		dataSource.addOrderExpression(sort.createExpression());

		List<Object[]> result = dataSource.executeQuery();

		try {

			PreparedStatement prepareStatement = connection.prepareStatement(
					"SELECT * FROM person WHERE age >= 18 ORDER BY last_name DESC, first_name ASC { LIMIT 100 OFFSET 0 };");
			ResultSet resultSet = prepareStatement.executeQuery();

			resultSet.next();
			Assert.assertTrue(result.get(0)[1].equals(resultSet.getString(2)));

			resultSet.last();
			Assert.assertTrue(result.get(result.size() - 1)[1].equals(resultSet.getString(2)));

			resultSet.close();
			prepareStatement.close();

		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	@Test
	public void test18RemoveSort() {

		dataSource.removeOrderExpression("last_name DESC");
		List<Object[]> result = dataSource.executeQuery();

		try {

			PreparedStatement prepareStatement = connection.prepareStatement(
					"SELECT * FROM person WHERE age >= 18 ORDER BY first_name ASC { LIMIT 100 OFFSET 0 };");
			ResultSet resultSet = prepareStatement.executeQuery();

			resultSet.next();
			Assert.assertTrue(result.get(0)[1].equals(resultSet.getString(2)));

			resultSet.last();
			Assert.assertTrue(result.get(result.size() - 1)[1].equals(resultSet.getString(2)));

			resultSet.close();
			prepareStatement.close();

		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	@Test
	public void test19DateFilter() {

		dataSource.getOrderExpressions().clear();
		dataSource.getWhereExpressions().clear();
		DateJDBCFilter filter = new DateJDBCFilter("dob", TemporalType.DATE);
		filter.setFilterType(FilterType.GREATER_THAN);
		Date date = Date.valueOf("1990-01-01");
		filter.setFilterValues(new Date[] { date });
		dataSource.addWhereExpression(filter);
		List<Object[]> result = dataSource.executeQuery();

		try {

			PreparedStatement prepareStatement = connection
					.prepareStatement("SELECT * FROM person WHERE dob > '1990-01-01' { LIMIT 100 OFFSET 0 };");
			ResultSet resultSet = prepareStatement.executeQuery();

			resultSet.next();
			Assert.assertTrue(result.get(0)[0].equals(resultSet.getLong(1)));

			resultSet.last();
			Assert.assertTrue(result.get(result.size() - 1)[0].equals(resultSet.getLong(1)));

			resultSet.close();
			prepareStatement.close();

		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	@Test
	public void test20CalendarFilter() {

		dataSource.getWhereExpressions().clear();
		CalendarJDBCFilter filter = new CalendarJDBCFilter("dob", TemporalType.DATE);
		filter.setFilterType(FilterType.BETWEEN);
		Date date = Date.valueOf("1990-01-01");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Date date2 = Date.valueOf("2000-01-01");
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(date2);
		filter.setFilterValues(new Calendar[] { calendar, calendar2 });
		dataSource.addWhereExpression(filter);
		List<Object[]> result = dataSource.executeQuery();

		try {

			PreparedStatement prepareStatement = connection.prepareStatement(
					"SELECT * FROM person WHERE dob BETWEEN '1990-01-01' AND '2000-01-01' { LIMIT 100 OFFSET 0 };");
			ResultSet resultSet = prepareStatement.executeQuery();

			resultSet.next();
			Assert.assertTrue(result.get(0)[0].equals(resultSet.getLong(1)));

			resultSet.last();
			Assert.assertTrue(result.get(result.size() - 1)[0].equals(resultSet.getLong(1)));

			resultSet.close();
			prepareStatement.close();

		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	@Test
	public void test21LocalDateFilter() {

		dataSource.getWhereExpressions().clear();
		LocalDateJDBCFilter filter = new LocalDateJDBCFilter("dob");
		filter.setFilterType(FilterType.LESS_THAN_OR_EQUAL);
		LocalDate date = Date.valueOf("2018-01-01").toLocalDate();
		filter.setFilterValues(new LocalDate[] { date });
		dataSource.addWhereExpression(filter);
		List<Object[]> result = dataSource.executeQuery();

		try {

			PreparedStatement prepareStatement = connection
					.prepareStatement("SELECT * FROM person WHERE dob <= '2018-01-01' { LIMIT 100 OFFSET 0 };");
			ResultSet resultSet = prepareStatement.executeQuery();

			resultSet.next();
			Assert.assertTrue(result.get(0)[0].equals(resultSet.getLong(1)));

			resultSet.last();
			Assert.assertTrue(result.get(result.size() - 1)[0].equals(resultSet.getLong(1)));

			resultSet.close();
			prepareStatement.close();

		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}

	}
}
