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
package hu.computertechnika.paginationfx.data;

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
import hu.computertechnika.paginationfx.sql.JDBCDataSource;
import hu.computertechnika.paginationfx.sql.MemoryDataBase;

/**
 * @author Gábor Balanyi
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JDBCDataProviderTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(JDBCDataProviderTest.class);

	private static Connection connection;

	private static JDBCDataProvider<Object[]> dataProvider;

	@BeforeClass
	public static void init() {

		MemoryDataBase.create();
		connection = MemoryDataBase.getConnection();
		createDataProvider();

	}

	private static void createDataProvider() {

		JDBCDataSource<Object[]> dataSource = new JDBCDataSource<>(connection);
		dataProvider = new JDBCDataProvider<>(dataSource);

	}

	@Test
	public void test00CheckSelect() {

		try {
			dataProvider.loadPage();
		} catch (Exception e) {

			LOGGER.debug(e.getMessage());
			Assert.assertTrue(e instanceof IllegalStateException);

		}

	}

	@Test
	public void test01CheckFrom() {

		dataProvider.getDataSource().addSelectExpression("*");
		try {
			dataProvider.loadPage();
		} catch (Exception e) {

			LOGGER.debug(e.getMessage());
			Assert.assertTrue(e instanceof IllegalStateException);

		}

	}

	@Test
	public void test02FirstPage() {

		dataProvider.getDataSource().setFromExpression("person");
		dataProvider.firstPage();
		dataProvider.setPageSize(100);
		List<Object[]> result = dataProvider.loadPage();

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

		long rowNumber = dataProvider.getPageSize() * dataProvider.getPageNumber();
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

		long rowNumber = dataProvider.getPageSize() * dataProvider.getPageNumber();
		dataProvider.lastPage();
		List<Object[]> result = dataProvider.loadPage();

		try {

			PreparedStatement prepareStatement = connection.prepareStatement(
					"SELECT * FROM person LIMIT 100 OFFSET " + (rowNumber - dataProvider.getPageSize()) + ";");
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
		dataProvider.addFilter("Filter", filter);
		dataProvider.firstPage();
		List<Object[]> result = dataProvider.loadPage();

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

		dataProvider.getFilters().get("Filter").setFilterType(FilterType.EQUAL);
		List<Object[]> result = dataProvider.loadPage();

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

		((StringJDBCFilter) dataProvider.getFilters().get("Filter")).setFilterValues(new String[] { "John" });
		List<Object[]> result = dataProvider.loadPage();

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

		StringJDBCFilter filter = (StringJDBCFilter) dataProvider.getFilters().get("Filter");
		filter.setFilterType(FilterType.STARTS_WITH);
		filter.setFilterValues(new String[] { "Jo" });
		List<Object[]> result = dataProvider.loadPage();

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

		StringJDBCFilter filter = (StringJDBCFilter) dataProvider.getFilters().get("Filter");
		filter.setFilterType(FilterType.CONTAINS);
		filter.setFilterValues(new String[] { "oh" });
		List<Object[]> result = dataProvider.loadPage();

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

		StringJDBCFilter filter = (StringJDBCFilter) dataProvider.getFilters().get("Filter");
		filter.setFilterType(FilterType.ENDS_WITH);
		filter.setFilterValues(new String[] { "hn" });
		List<Object[]> result = dataProvider.loadPage();

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

		StringJDBCFilter filter = (StringJDBCFilter) dataProvider.getFilters().get("Filter");
		filter.setFilterType(FilterType.IN);
		filter.setFilterValues(new String[] { "John", "Jane" });
		List<Object[]> result = dataProvider.loadPage();

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

		StringJDBCFilter filter = (StringJDBCFilter) dataProvider.getFilters().get("Filter");
		filter.setFilterType(FilterType.IN);
		filter.setFilterValues(new String[] { "John", "Jane", null });
		List<Object[]> result = dataProvider.loadPage();

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

		long pageNumber = dataProvider.getPageNumber();
		try {

			PreparedStatement prepareStatement = connection
					.prepareStatement("SELECT COUNT(*) FROM person WHERE first_name IN ('John', 'Jane', NULL);");
			ResultSet resultSet = prepareStatement.executeQuery();

			resultSet.next();
			Assert.assertTrue(pageNumber == Math.max(resultSet.getLong(1) / dataProvider.getPageSize(), 1));

			resultSet.close();
			prepareStatement.close();

		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	@Test
	public void test14MultiFilter() {

		StringJDBCFilter filter = (StringJDBCFilter) dataProvider.getFilters().get("Filter");
		filter.setFilterType(FilterType.IN);
		filter.setFilterValues(new String[] { "John", "Jane" });

		ComparableJDBCFilter<Integer> filter2 = new ComparableJDBCFilter<>("age");
		filter2.setFilterType(FilterType.GREATER_THAN_OR_EQUAL);
		filter2.setFilterValues(new Integer[] { 18 });
		dataProvider.addFilter("Filter2", filter2);

		List<Object[]> result = dataProvider.loadPage();

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

		dataProvider.removeFilter("Filter");
		List<Object[]> result = dataProvider.loadPage();

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
		dataProvider.addSort("Sort", sort);

		List<Object[]> result = dataProvider.loadPage();

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
		dataProvider.addSort("Sort2", sort);

		List<Object[]> result = dataProvider.loadPage();

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

		dataProvider.removeSort("Sort");
		List<Object[]> result = dataProvider.loadPage();

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

		dataProvider.filtersProperty().clear();
		dataProvider.sortsProperty().clear();
		DateJDBCFilter filter = new DateJDBCFilter("dob", TemporalType.DATE);
		filter.setFilterType(FilterType.GREATER_THAN);
		Date date = Date.valueOf("1990-01-01");
		filter.setFilterValues(new Date[] { date });
		dataProvider.addFilter("FILTER", filter);
		List<Object[]> result = dataProvider.loadPage();

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

		dataProvider.filtersProperty().clear();
		CalendarJDBCFilter filter = new CalendarJDBCFilter("dob", TemporalType.DATE);
		filter.setFilterType(FilterType.BETWEEN);
		Date date = Date.valueOf("1990-01-01");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Date date2 = Date.valueOf("2000-01-01");
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(date2);
		filter.setFilterValues(new Calendar[] { calendar, calendar2 });
		dataProvider.addFilter("FILTER", filter);
		List<Object[]> result = dataProvider.loadPage();

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

		dataProvider.filtersProperty().clear();
		LocalDateJDBCFilter filter = new LocalDateJDBCFilter("dob");
		filter.setFilterType(FilterType.LESS_THAN_OR_EQUAL);
		LocalDate date = Date.valueOf("2018-01-01").toLocalDate();
		filter.setFilterValues(new LocalDate[] { date });
		dataProvider.addFilter("FILTER", filter);
		List<Object[]> result = dataProvider.loadPage();

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
