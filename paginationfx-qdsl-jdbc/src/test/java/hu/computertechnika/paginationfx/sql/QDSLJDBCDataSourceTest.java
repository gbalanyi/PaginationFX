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

/**
 * @author Gábor Balanyi
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QDSLJDBCDataSourceTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(JDBCDataSource.class);

	private static Connection connection;

	private static AbstractQDSLJDBCDataSource<Long> dataSource;

	private static AbstractQDSLJDBCDataSource<Tuple> tupleDataSource;

	@BeforeClass
	public static void init() {

		MemoryDataBase.create();
		connection = MemoryDataBase.getConnection();
		createDataSource();

	}

	private static void createDataSource() {

		dataSource = new QDSLJDBCDataSource<>(connection);
		
		tupleDataSource = new QDSLJDBCDataSource<>(connection);
		tupleDataSource.addSelectExpression(QPerson.person.id);
		tupleDataSource.addSelectExpression(QPerson.person.firstName);
		tupleDataSource.addSelectExpression(QPerson.person.lastName);
		tupleDataSource.setFromExpression(QPerson.person);
		tupleDataSource.setOffset(0);
		tupleDataSource.setMaxResults(100);

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

		dataSource.addSelectExpression(QPerson.person.id);
		try {
			dataSource.executeQuery();
		} catch (Exception e) {

			LOGGER.debug(e.getMessage());
			Assert.assertTrue(e instanceof IllegalStateException);

		}

	}

	@Test
	public void test02FirstPage() {

		dataSource.setFromExpression(QPerson.person);
		dataSource.setOffset(0);
		dataSource.setMaxResults(100);
		List<Long> result = dataSource.executeQuery();

		SQLQuery<Long> query = new SQLQuery<>(connection, SQLTemplates.DEFAULT);
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

		long rowNumber = dataSource.executeCountQuery();

		SQLQuery<Long> query = new SQLQuery<>(connection, SQLTemplates.DEFAULT);
		query.from(QPerson.person);
		query.select(QPerson.person.count());
		long rowNumber2 = query.fetchOne();

		Assert.assertTrue(rowNumber == rowNumber2);

	}

	@Test
	public void test04LastPage() {

		long rowNumber = dataSource.executeCountQuery();
		dataSource.setOffset(rowNumber - dataSource.getMaxResults());
		List<Long> result = dataSource.executeQuery();

		SQLQuery<Long> query = new SQLQuery<>(connection, SQLTemplates.DEFAULT);
		query.from(QPerson.person);
		query.select(QPerson.person.id);
		query.limit(100);
		query.offset(rowNumber - dataSource.getMaxResults());
		List<Long> result2 = query.fetch();

		Assert.assertTrue(result.get(0).equals(result2.get(0)));
		Assert.assertTrue(result.get(result.size() - 1).equals(result2.get(result2.size() - 1)));

	}

	@Test
	public void test05NoneFilter() {

		dataSource.setOffset(0);
		StringQDSLFilter filter = new StringQDSLFilter(QPerson.person.firstName);
		try {

			dataSource.addWhereExpression(filter.createPredicate());
			dataSource.executeQuery();

		} catch (Exception e) {
			Assert.assertTrue(e instanceof NullPointerException);
		}

	}

	@Test
	public void test06EqualFilterWithNull() {

		dataSource.getWhereExpressions().clear();
		StringQDSLFilter filter = new StringQDSLFilter(QPerson.person.firstName);
		filter.setFilterType(FilterType.EQUAL);
		filter.setFilterValues(new String[] { null });
		dataSource.addWhereExpression(filter.createPredicate());
		List<Long> result = dataSource.executeQuery();

		SQLQuery<Long> query = new SQLQuery<>(connection, SQLTemplates.DEFAULT);
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

		dataSource.getWhereExpressions().clear();
		StringQDSLFilter filter = new StringQDSLFilter(QPerson.person.firstName);
		filter.setFilterType(FilterType.EQUAL);
		filter.setFilterValues(new String[] { "John" });
		dataSource.addWhereExpression(filter.createPredicate());
		List<Long> result = dataSource.executeQuery();

		SQLQuery<Long> query = new SQLQuery<>(connection, SQLTemplates.DEFAULT);
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

		dataSource.getWhereExpressions().clear();
		StringQDSLFilter filter = new StringQDSLFilter(QPerson.person.firstName);
		filter.setFilterType(FilterType.STARTS_WITH);
		filter.setFilterValues(new String[] { "Jo" });
		dataSource.addWhereExpression(filter.createPredicate());
		List<Long> result = dataSource.executeQuery();

		SQLQuery<Long> query = new SQLQuery<>(connection, SQLTemplates.DEFAULT);
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

		dataSource.getWhereExpressions().clear();
		StringQDSLFilter filter = new StringQDSLFilter(QPerson.person.firstName);
		filter.setFilterType(FilterType.CONTAINS);
		filter.setFilterValues(new String[] { "oh" });
		dataSource.addWhereExpression(filter.createPredicate());
		List<Long> result = dataSource.executeQuery();

		SQLQuery<Long> query = new SQLQuery<>(connection, SQLTemplates.DEFAULT);
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

		dataSource.getWhereExpressions().clear();
		StringQDSLFilter filter = new StringQDSLFilter(QPerson.person.firstName);
		filter.setFilterType(FilterType.ENDS_WITH);
		filter.setFilterValues(new String[] { "hn" });
		dataSource.addWhereExpression(filter.createPredicate());
		List<Long> result = dataSource.executeQuery();

		SQLQuery<Long> query = new SQLQuery<>(connection, SQLTemplates.DEFAULT);
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

		dataSource.getWhereExpressions().clear();
		StringQDSLFilter filter = new StringQDSLFilter(QPerson.person.firstName);
		filter.setFilterType(FilterType.IN);
		filter.setFilterValues(new String[] { "John", "Jane" });
		dataSource.addWhereExpression(filter.createPredicate());
		List<Long> result = dataSource.executeQuery();

		SQLQuery<Long> query = new SQLQuery<>(connection, SQLTemplates.DEFAULT);
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

		dataSource.getWhereExpressions().clear();
		StringQDSLFilter filter = new StringQDSLFilter(QPerson.person.firstName);
		filter.setFilterType(FilterType.IN);
		filter.setFilterValues(new String[] { "John", "Jane", null });

		try {

			dataSource.addWhereExpression(filter.createPredicate());
			dataSource.executeQuery();

		} catch (Exception e) {
			Assert.assertTrue(e instanceof NullPointerException);
		}

	}

	@Test
	public void test13CountWithFilter() {

		dataSource.getWhereExpressions().clear();
		StringQDSLFilter filter = new StringQDSLFilter(QPerson.person.firstName);
		filter.setFilterType(FilterType.IN);
		filter.setFilterValues(new String[] { "John", "Jane" });
		dataSource.addWhereExpression(filter.createPredicate());

		long rowNumber = dataSource.executeCountQuery();

		SQLQuery<Long> query = new SQLQuery<>(connection, SQLTemplates.DEFAULT);
		query.from(QPerson.person);
		query.select(QPerson.person.count());
		query.where(QPerson.person.firstName.in("John", "Jane"));
		long rowNumber2 = query.fetchOne();

		Assert.assertTrue(rowNumber == rowNumber2);

	}

	@Test
	public void test14MultiFilter() {

		dataSource.getWhereExpressions().clear();
		StringQDSLFilter filter = new StringQDSLFilter(QPerson.person.firstName);
		filter.setFilterType(FilterType.IN);
		filter.setFilterValues(new String[] { "John", "Jane" });
		dataSource.addWhereExpression(filter.createPredicate());

		NumberQDSLFilter<Integer> filter2 = new NumberQDSLFilter<>(QPerson.person.age);
		filter2.setFilterType(FilterType.GREATER_THAN_OR_EQUAL);
		filter2.setFilterValues(new Integer[] { 18 });
		dataSource.addWhereExpression(filter2.createPredicate());
		List<Long> result = dataSource.executeQuery();

		SQLQuery<Long> query = new SQLQuery<>(connection, SQLTemplates.DEFAULT);
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

		dataSource.getWhereExpressions().remove(0);
		List<Long> result = dataSource.executeQuery();

		SQLQuery<Long> query = new SQLQuery<>(connection, SQLTemplates.DEFAULT);
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

		NumberQDSLFilter<Integer> filter = new NumberQDSLFilter<>(QPerson.person.age);
		filter.setFilterType(FilterType.GREATER_THAN_OR_EQUAL);
		filter.setFilterValues(new Integer[] { 18 });
		tupleDataSource.addWhereExpression(filter.createPredicate());

		QDSLSort sort = new QDSLSort(QPerson.person.lastName);
		sort.setSortType(SortType.DESCENDING);
		tupleDataSource.addOrderExpression(sort.createExpression());

		List<Tuple> result = tupleDataSource.executeQuery();

		SQLQuery<Tuple> query = new SQLQuery<>(connection, SQLTemplates.DEFAULT);
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
		tupleDataSource.addOrderExpression(sort.createExpression());

		List<Tuple> result = tupleDataSource.executeQuery();

		SQLQuery<Tuple> query = new SQLQuery<>(connection, SQLTemplates.DEFAULT);
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

		tupleDataSource.getOrderExpressions().remove(0);
		List<Tuple> result = tupleDataSource.executeQuery();

		SQLQuery<Tuple> query = new SQLQuery<>(connection, SQLTemplates.DEFAULT);
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

		dataSource.getOrderExpressions().clear();
		dataSource.getWhereExpressions().clear();
		TemporalQDSLFilter<Date> filter = new TemporalQDSLFilter<>(QPerson.person.dob, TemporalType.DATE);
		filter.setFilterType(FilterType.GREATER_THAN);
		java.sql.Date date = java.sql.Date.valueOf("1990-01-01");
		filter.setFilterValues(new Date[] { date });
		dataSource.addWhereExpression(filter.createPredicate());
		List<Long> result = dataSource.executeQuery();

		SQLQuery<Long> query = new SQLQuery<>(connection, SQLTemplates.DEFAULT);
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
