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
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.querydsl.core.Tuple;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLTemplates;

import hu.computertechnika.paginationfx.filter.FilterType;
import hu.computertechnika.paginationfx.filter.NumberQDSLFilter;
import hu.computertechnika.paginationfx.filter.StringQDSLFilter;
import hu.computertechnika.paginationfx.filter.TemporalQDSLFilter;
import hu.computertechnika.paginationfx.filter.TemporalType;
import hu.computertechnika.paginationfx.sort.QDSLSort;
import hu.computertechnika.paginationfx.sort.SortType;
import hu.computertechnika.paginationfx.sql.AbstractQDSLJDBCDataSource;
import hu.computertechnika.paginationfx.sql.MemoryDataBase;
import hu.computertechnika.paginationfx.sql.QDSLJDBCDataSource;
import hu.computertechnika.paginationfx.sql.QPerson;

/**
 * @author Gábor Balanyi
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QDSLJDBCDataProviderTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(QDSLJDBCDataProviderTest.class);

	private static Connection entityManager;

	private static QDSLJDBCDataProvider<Long, Long> dataProvider;

	private static QDSLJDBCDataProvider<Tuple, Tuple> tupleDataProvider;

	@BeforeClass
	public static void init() {

		MemoryDataBase.create();
		entityManager = MemoryDataBase.getConnection();
		createDataProvider();

	}

	private static void createDataProvider() {

		AbstractQDSLJDBCDataSource<Long> dataSource = new QDSLJDBCDataSource<>(entityManager);
		dataProvider = new QDSLJDBCDataProvider<>(dataSource);

		AbstractQDSLJDBCDataSource<Tuple> tupleDataSource = new QDSLJDBCDataSource<>(entityManager);
		tupleDataSource.addSelectExpression(QPerson.person.id);
		tupleDataSource.addSelectExpression(QPerson.person.firstName);
		tupleDataSource.addSelectExpression(QPerson.person.lastName);
		tupleDataSource.setFromExpression(QPerson.person);
		tupleDataSource.setOffset(0);
		tupleDataSource.setMaxResults(100);

		tupleDataProvider = new QDSLJDBCDataProvider<>(tupleDataSource);
		tupleDataProvider.firstPage();
		tupleDataProvider.setPageSize(100);

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

		dataProvider.getDataSource().addSelectExpression(QPerson.person.id);
		try {
			dataProvider.loadPage();
		} catch (Exception e) {

			LOGGER.debug(e.getMessage());
			Assert.assertTrue(e instanceof IllegalStateException);

		}

	}

	@Test
	public void test02FirstPage() {

		dataProvider.getDataSource().setFromExpression(QPerson.person);
		dataProvider.firstPage();
		dataProvider.setPageSize(100);
		List<Long> result = dataProvider.loadPage();

		SQLQuery<Long> query = new SQLQuery<>(entityManager, SQLTemplates.DEFAULT);
		query.from(QPerson.person);
		query.select(QPerson.person.id);
		query.limit(100);
		query.offset(0);
		List<Long> result2 = query.fetch();

		Assert.assertTrue(result.get(0).equals(result2.get(0)));
		Assert.assertTrue(result.get(result.size() - 1).equals(result2.get(result2.size() - 1)));

	}

	@Test
	public void test03Count() {

		long rowNumber = dataProvider.getPageSize() * dataProvider.getPageNumber();

		SQLQuery<Long> query = new SQLQuery<>(entityManager, SQLTemplates.DEFAULT);
		query.from(QPerson.person);
		query.select(QPerson.person.count());
		long row2Number = query.fetchOne();

		Assert.assertTrue(rowNumber == row2Number);

	}

	@Test
	public void test04LastPage() {

		long rowNumber = dataProvider.getPageSize() * dataProvider.getPageNumber();
		dataProvider.lastPage();
		List<Long> result = dataProvider.loadPage();

		SQLQuery<Long> query = new SQLQuery<>(entityManager, SQLTemplates.DEFAULT);
		query.from(QPerson.person);
		query.select(QPerson.person.id);
		query.limit(100);
		query.offset(rowNumber - dataProvider.getPageSize());
		List<Long> result2 = query.fetch();

		Assert.assertTrue(result.get(0).equals(result2.get(0)));
		Assert.assertTrue(result.get(result.size() - 1).equals(result2.get(result2.size() - 1)));

	}

	@Test
	public void test05NoneFilter() {

		StringQDSLFilter filter = new StringQDSLFilter(QPerson.person.firstName);
		dataProvider.addFilter("Filter", filter);
		dataProvider.firstPage();
		List<Long> result = dataProvider.loadPage();

		SQLQuery<Long> query = new SQLQuery<>(entityManager, SQLTemplates.DEFAULT);
		query.from(QPerson.person);
		query.select(QPerson.person.id);
		query.limit(100);
		query.offset(0);
		List<Long> result2 = query.fetch();

		Assert.assertTrue(result.get(0).equals(result2.get(0)));
		Assert.assertTrue(result.get(result.size() - 1).equals(result2.get(result2.size() - 1)));

	}

	@Test
	public void test06EqualFilterWithNull() {

		dataProvider.getFilters().get("Filter").setFilterType(FilterType.EQUAL);
		List<Long> result = dataProvider.loadPage();

		SQLQuery<Long> query = new SQLQuery<>(entityManager, SQLTemplates.DEFAULT);
		query.from(QPerson.person);
		query.select(QPerson.person.id);
		query.where(QPerson.person.firstName.isNull());
		query.limit(100);
		query.offset(0);
		List<Long> result2 = query.fetch();

		Assert.assertTrue(result.isEmpty() && result2.isEmpty());

	}

	@Test
	public void test07EqualFilter() {

		((StringQDSLFilter) dataProvider.getFilters().get("Filter")).setFilterValues(new String[] { "John" });
		List<Long> result = dataProvider.loadPage();

		SQLQuery<Long> query = new SQLQuery<>(entityManager, SQLTemplates.DEFAULT);
		query.from(QPerson.person);
		query.select(QPerson.person.id);
		query.where(QPerson.person.firstName.eq("John"));
		query.limit(100);
		query.offset(0);
		List<Long> result2 = query.fetch();

		Assert.assertTrue(result.get(0).equals(result2.get(0)));
		Assert.assertTrue(result.get(result.size() - 1).equals(result2.get(result2.size() - 1)));

	}

	@Test
	public void test08StartsWithFilter() {

		StringQDSLFilter filter = (StringQDSLFilter) dataProvider.getFilters().get("Filter");
		filter.setFilterType(FilterType.STARTS_WITH);
		filter.setFilterValues(new String[] { "Jo" });
		List<Long> result = dataProvider.loadPage();

		SQLQuery<Long> query = new SQLQuery<>(entityManager, SQLTemplates.DEFAULT);
		query.from(QPerson.person);
		query.select(QPerson.person.id);
		query.where(QPerson.person.firstName.startsWith("Jo"));
		query.limit(100);
		query.offset(0);
		List<Long> result2 = query.fetch();

		Assert.assertTrue(result.get(0).equals(result2.get(0)));
		Assert.assertTrue(result.get(result.size() - 1).equals(result2.get(result2.size() - 1)));

	}

	@Test
	public void test09ContainsFilter() {

		StringQDSLFilter filter = (StringQDSLFilter) dataProvider.getFilters().get("Filter");
		filter.setFilterType(FilterType.CONTAINS);
		filter.setFilterValues(new String[] { "oh" });
		List<Long> result = dataProvider.loadPage();

		SQLQuery<Long> query = new SQLQuery<>(entityManager, SQLTemplates.DEFAULT);
		query.from(QPerson.person);
		query.select(QPerson.person.id);
		query.where(QPerson.person.firstName.contains("oh"));
		query.limit(100);
		query.offset(0);
		List<Long> result2 = query.fetch();

		Assert.assertTrue(result.get(0).equals(result2.get(0)));
		Assert.assertTrue(result.get(result.size() - 1).equals(result2.get(result2.size() - 1)));

	}

	@Test
	public void test10EndsWithFilter() {

		StringQDSLFilter filter = (StringQDSLFilter) dataProvider.getFilters().get("Filter");
		filter.setFilterType(FilterType.ENDS_WITH);
		filter.setFilterValues(new String[] { "hn" });
		List<Long> result = dataProvider.loadPage();

		SQLQuery<Long> query = new SQLQuery<>(entityManager, SQLTemplates.DEFAULT);
		query.from(QPerson.person);
		query.select(QPerson.person.id);
		query.where(QPerson.person.firstName.endsWith("hn"));
		query.limit(100);
		query.offset(0);
		List<Long> result2 = query.fetch();

		Assert.assertTrue(result.get(0).equals(result2.get(0)));
		Assert.assertTrue(result.get(result.size() - 1).equals(result2.get(result2.size() - 1)));

	}

	@Test
	public void test11InFilter() {

		StringQDSLFilter filter = (StringQDSLFilter) dataProvider.getFilters().get("Filter");
		filter.setFilterType(FilterType.IN);
		filter.setFilterValues(new String[] { "John", "Jane" });
		List<Long> result = dataProvider.loadPage();

		SQLQuery<Long> query = new SQLQuery<>(entityManager, SQLTemplates.DEFAULT);
		query.from(QPerson.person);
		query.select(QPerson.person.id);
		query.where(QPerson.person.firstName.in("John", "Jane"));
		query.limit(100);
		query.offset(0);
		List<Long> result2 = query.fetch();

		Assert.assertTrue(result.get(0).equals(result2.get(0)));
		Assert.assertTrue(result.get(result.size() - 1).equals(result2.get(result2.size() - 1)));

	}

	@Test
	public void test12InWithNullFilter() {

		StringQDSLFilter filter = (StringQDSLFilter) dataProvider.getFilters().get("Filter");
		filter.setFilterType(FilterType.IN);
		filter.setFilterValues(new String[] { "John", "Jane", null });

		try {
			dataProvider.loadPage();
		} catch (Exception e) {
			Assert.assertTrue(e instanceof NullPointerException);
		}

	}

	@Test
	public void test13CountWithFilter() {

		StringQDSLFilter filter = (StringQDSLFilter) dataProvider.getFilters().get("Filter");
		filter.setFilterType(FilterType.IN);
		filter.setFilterValues(new String[] { "John", "Jane" });
		dataProvider.loadPage();
		long pageNumber = dataProvider.getPageNumber();

		SQLQuery<Long> query = new SQLQuery<>(entityManager, SQLTemplates.DEFAULT);
		query.from(QPerson.person);
		query.select(QPerson.person.count());
		query.where(QPerson.person.firstName.in("John", "Jane"));
		long rowNumber = query.fetchOne();

		Assert.assertTrue(pageNumber == Math.max(rowNumber / dataProvider.getPageSize(), 1));

	}

	@Test
	public void test14MultiFilter() {

		NumberQDSLFilter<Integer> filter2 = new NumberQDSLFilter<>(QPerson.person.age);
		filter2.setFilterType(FilterType.GREATER_THAN_OR_EQUAL);
		filter2.setFilterValues(new Integer[] { 18 });
		dataProvider.addFilter("Filter2", filter2);

		List<Long> result = dataProvider.loadPage();

		SQLQuery<Long> query = new SQLQuery<>(entityManager, SQLTemplates.DEFAULT);
		query.from(QPerson.person);
		query.select(QPerson.person.id);
		query.where(QPerson.person.firstName.in("John", "Jane").and(QPerson.person.age.goe(18)));
		query.limit(100);
		query.offset(0);
		List<Long> result2 = query.fetch();

		Assert.assertTrue(result.get(0).equals(result2.get(0)));
		Assert.assertTrue(result.get(result.size() - 1).equals(result2.get(result2.size() - 1)));

	}

	@Test
	public void test15RemoveFilter() {

		dataProvider.removeFilter("Filter");
		List<Long> result = dataProvider.loadPage();

		SQLQuery<Long> query = new SQLQuery<>(entityManager, SQLTemplates.DEFAULT);
		query.from(QPerson.person);
		query.select(QPerson.person.id);
		query.where(QPerson.person.age.goe(18));
		query.limit(100);
		query.offset(0);
		List<Long> result2 = query.fetch();

		Assert.assertTrue(result.get(0).equals(result2.get(0)));
		Assert.assertTrue(result.get(result.size() - 1).equals(result2.get(result2.size() - 1)));

	}

	@Test
	public void test16Sort() {

		NumberQDSLFilter<Integer> filter2 = new NumberQDSLFilter<>(QPerson.person.age);
		filter2.setFilterType(FilterType.GREATER_THAN_OR_EQUAL);
		filter2.setFilterValues(new Integer[] { 18 });
		tupleDataProvider.addFilter("Filter", filter2);

		QDSLSort sort = new QDSLSort(QPerson.person.lastName);
		sort.setSortType(SortType.DESCENDING);
		tupleDataProvider.addSort("Sort", sort);

		List<Tuple> result = tupleDataProvider.loadPage();

		SQLQuery<Tuple> query = new SQLQuery<>(entityManager, SQLTemplates.DEFAULT);
		query.from(QPerson.person);
		query.select(QPerson.person.id, QPerson.person.firstName, QPerson.person.lastName);
		query.where(QPerson.person.age.goe(18));
		query.orderBy(QPerson.person.lastName.desc());
		query.limit(100);
		query.offset(0);
		List<Tuple> result2 = query.fetch();

		Assert.assertTrue(
				result.get(0).get(QPerson.person.lastName).equals(result2.get(0).get(QPerson.person.lastName)));
		Assert.assertTrue(result.get(result.size() - 1).get(QPerson.person.lastName)
				.equals(result2.get(result2.size() - 1).get(QPerson.person.lastName)));

	}

	@Test
	public void test17MultiSort() {

		QDSLSort sort = new QDSLSort(QPerson.person.firstName);
		sort.setSortType(SortType.ASCENDING);
		tupleDataProvider.addSort("Sort2", sort);

		List<Tuple> result = tupleDataProvider.loadPage();

		SQLQuery<Tuple> query = new SQLQuery<>(entityManager, SQLTemplates.DEFAULT);
		query.from(QPerson.person);
		query.select(QPerson.person.id, QPerson.person.firstName, QPerson.person.lastName);
		query.where(QPerson.person.age.goe(18));
		query.orderBy(QPerson.person.lastName.desc(), QPerson.person.firstName.asc());
		query.limit(100);
		query.offset(0);
		List<Tuple> result2 = query.fetch();

		Assert.assertTrue(
				result.get(0).get(QPerson.person.lastName).equals(result2.get(0).get(QPerson.person.lastName)));
		Assert.assertTrue(result.get(result.size() - 1).get(QPerson.person.lastName)
				.equals(result2.get(result2.size() - 1).get(QPerson.person.lastName)));

	}

	@Test
	public void test18RemoveSort() {

		tupleDataProvider.removeSort("Sort");
		List<Tuple> result = tupleDataProvider.loadPage();

		SQLQuery<Tuple> query = new SQLQuery<>(entityManager, SQLTemplates.DEFAULT);
		query.from(QPerson.person);
		query.select(QPerson.person.id, QPerson.person.firstName, QPerson.person.lastName);
		query.where(QPerson.person.age.goe(18));
		query.orderBy(QPerson.person.firstName.asc());
		query.limit(100);
		query.offset(0);
		List<Tuple> result2 = query.fetch();

		Assert.assertTrue(
				result.get(0).get(QPerson.person.firstName).equals(result2.get(0).get(QPerson.person.firstName)));
		Assert.assertTrue(result.get(result.size() - 1).get(QPerson.person.firstName)
				.equals(result2.get(result2.size() - 1).get(QPerson.person.firstName)));

	}

	@Test
	public void test19TemporalFilter() {

		dataProvider.filtersProperty().clear();
		dataProvider.sortsProperty().clear();
		TemporalQDSLFilter<Date> filter = new TemporalQDSLFilter<>(QPerson.person.dob, TemporalType.DATE);
		filter.setFilterType(FilterType.GREATER_THAN);
		java.sql.Date date = java.sql.Date.valueOf("1990-01-01");
		filter.setFilterValues(new Date[] { date });
		dataProvider.addFilter("FILTER", filter);
		List<Long> result = dataProvider.loadPage();

		SQLQuery<Long> query = new SQLQuery<>(entityManager, SQLTemplates.DEFAULT);
		query.from(QPerson.person);
		query.select(QPerson.person.id);
		query.where(QPerson.person.dob.gt(date));
		query.limit(100);
		query.offset(0);
		List<Long> result2 = query.fetch();

		Assert.assertTrue(result.get(0).equals(result2.get(0)));
		Assert.assertTrue(result.get(result.size() - 1).equals(result2.get(result2.size() - 1)));

	}

}
