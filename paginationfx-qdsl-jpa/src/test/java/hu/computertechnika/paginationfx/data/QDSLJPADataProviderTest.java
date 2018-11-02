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

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;

import hu.computertechnika.paginationfx.filter.FilterType;
import hu.computertechnika.paginationfx.filter.NumberQDSLFilter;
import hu.computertechnika.paginationfx.filter.StringQDSLFilter;
import hu.computertechnika.paginationfx.filter.TemporalQDSLFilter;
import hu.computertechnika.paginationfx.filter.TemporalType;
import hu.computertechnika.paginationfx.sort.QDSLSort;
import hu.computertechnika.paginationfx.sort.SortType;
import hu.computertechnika.paginationfx.sql.AbstractQDSLJPADataSource;
import hu.computertechnika.paginationfx.sql.MemoryDataBase;
import hu.computertechnika.paginationfx.sql.Person;
import hu.computertechnika.paginationfx.sql.QDSLJPADataSource;
import hu.computertechnika.paginationfx.sql.QPerson;

/**
 * @author Gábor Balanyi
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QDSLJPADataProviderTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(QDSLJPADataProviderTest.class);

	private static EntityManager entityManager;

	private static QDSLJPADataProvider<Person, Person> dataProvider;

	@BeforeClass
	public static void init() {

		MemoryDataBase.create();
		entityManager = MemoryDataBase.getEntityManager();
		createDataProvider();

	}

	private static void createDataProvider() {

		QDSLJPADataSource<Person> dataSource = new QDSLJPADataSource<>(entityManager);
		dataProvider = new QDSLJPADataProvider<>(dataSource);

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

		dataProvider.getDataSource().addSelectExpression(QPerson.person);
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
		List<Person> result = dataProvider.loadPage();

		JPAQuery<Person> query = new JPAQuery<>(entityManager);
		query.from(QPerson.person);
		query.select(QPerson.person);
		query.limit(100);
		query.offset(0);
		List<Person> result2 = query.fetch();

		Assert.assertTrue(result.get(0).getId().equals(result2.get(0).getId()));
		Assert.assertTrue(result.get(result.size() - 1).getId().equals(result2.get(result2.size() - 1).getId()));

	}

	@Test
	public void test03Count() {

		long rowNumber = dataProvider.getPageSize() * dataProvider.getPageNumber();

		JPAQuery<Long> query = new JPAQuery<>(entityManager);
		query.from(QPerson.person);
		query.select(QPerson.person.count());
		long row2Number = query.fetchOne();

		Assert.assertTrue(rowNumber == row2Number);

	}

	@Test
	public void test04LastPage() {

		long rowNumber = dataProvider.getPageSize() * dataProvider.getPageNumber();
		dataProvider.lastPage();
		List<Person> result = dataProvider.loadPage();

		JPAQuery<Person> query = new JPAQuery<>(entityManager);
		query.from(QPerson.person);
		query.select(QPerson.person);
		query.limit(100);
		query.offset(rowNumber - dataProvider.getPageSize());
		List<Person> result2 = query.fetch();

		Assert.assertTrue(result.get(0).getId().equals(result2.get(0).getId()));
		Assert.assertTrue(result.get(result.size() - 1).getId().equals(result2.get(result2.size() - 1).getId()));

	}

	@Test
	public void test05NoneFilter() {

		StringQDSLFilter filter = new StringQDSLFilter(QPerson.person.firstName);
		dataProvider.addFilter("Filter", filter);
		dataProvider.firstPage();
		List<Person> result = dataProvider.loadPage();

		JPAQuery<Person> query = new JPAQuery<>(entityManager);
		query.from(QPerson.person);
		query.select(QPerson.person);
		query.limit(100);
		query.offset(0);
		List<Person> result2 = query.fetch();

		Assert.assertTrue(result.get(0).getId().equals(result2.get(0).getId()));
		Assert.assertTrue(result.get(result.size() - 1).getId().equals(result2.get(result2.size() - 1).getId()));

	}

	@Test
	public void test06EqualFilterWithNull() {

		dataProvider.getFilters().get("Filter").setFilterType(FilterType.EQUAL);
		List<Person> result = dataProvider.loadPage();

		JPAQuery<Person> query = new JPAQuery<>(entityManager);
		query.from(QPerson.person);
		query.select(QPerson.person);
		query.where(QPerson.person.firstName.isNull());
		query.limit(100);
		query.offset(0);
		List<Person> result2 = query.fetch();

		Assert.assertTrue(result.isEmpty() && result2.isEmpty());

	}

	@Test
	public void test07EqualFilter() {

		((StringQDSLFilter) dataProvider.getFilters().get("Filter")).setFilterValues(new String[] { "John" });
		List<Person> result = dataProvider.loadPage();

		JPAQuery<Person> query = new JPAQuery<>(entityManager);
		query.from(QPerson.person);
		query.select(QPerson.person);
		query.where(QPerson.person.firstName.eq("John"));
		query.limit(100);
		query.offset(0);
		List<Person> result2 = query.fetch();

		Assert.assertTrue(result.get(0).getId().equals(result2.get(0).getId()));
		Assert.assertTrue(result.get(result.size() - 1).getId().equals(result2.get(result2.size() - 1).getId()));

	}

	@Test
	public void test08StartsWithFilter() {

		StringQDSLFilter filter = (StringQDSLFilter) dataProvider.getFilters().get("Filter");
		filter.setFilterType(FilterType.STARTS_WITH);
		filter.setFilterValues(new String[] { "Jo" });
		List<Person> result = dataProvider.loadPage();

		JPAQuery<Person> query = new JPAQuery<>(entityManager);
		query.from(QPerson.person);
		query.select(QPerson.person);
		query.where(QPerson.person.firstName.startsWith("Jo"));
		query.limit(100);
		query.offset(0);
		List<Person> result2 = query.fetch();

		Assert.assertTrue(result.get(0).getId().equals(result2.get(0).getId()));
		Assert.assertTrue(result.get(result.size() - 1).getId().equals(result2.get(result2.size() - 1).getId()));

	}

	@Test
	public void test09ContainsFilter() {

		StringQDSLFilter filter = (StringQDSLFilter) dataProvider.getFilters().get("Filter");
		filter.setFilterType(FilterType.CONTAINS);
		filter.setFilterValues(new String[] { "oh" });
		List<Person> result = dataProvider.loadPage();

		JPAQuery<Person> query = new JPAQuery<>(entityManager);
		query.from(QPerson.person);
		query.select(QPerson.person);
		query.where(QPerson.person.firstName.contains("oh"));
		query.limit(100);
		query.offset(0);
		List<Person> result2 = query.fetch();

		Assert.assertTrue(result.get(0).getId().equals(result2.get(0).getId()));
		Assert.assertTrue(result.get(result.size() - 1).getId().equals(result2.get(result2.size() - 1).getId()));

	}

	@Test
	public void test10EndsWithFilter() {

		StringQDSLFilter filter = (StringQDSLFilter) dataProvider.getFilters().get("Filter");
		filter.setFilterType(FilterType.ENDS_WITH);
		filter.setFilterValues(new String[] { "hn" });
		List<Person> result = dataProvider.loadPage();

		JPAQuery<Person> query = new JPAQuery<>(entityManager);
		query.from(QPerson.person);
		query.select(QPerson.person);
		query.where(QPerson.person.firstName.endsWith("hn"));
		query.limit(100);
		query.offset(0);
		List<Person> result2 = query.fetch();

		Assert.assertTrue(result.get(0).getId().equals(result2.get(0).getId()));
		Assert.assertTrue(result.get(result.size() - 1).getId().equals(result2.get(result2.size() - 1).getId()));

	}

	@Test
	public void test11InFilter() {

		StringQDSLFilter filter = (StringQDSLFilter) dataProvider.getFilters().get("Filter");
		filter.setFilterType(FilterType.IN);
		filter.setFilterValues(new String[] { "John", "Jane" });
		List<Person> result = dataProvider.loadPage();

		JPAQuery<Person> query = new JPAQuery<>(entityManager);
		query.from(QPerson.person);
		query.select(QPerson.person);
		query.where(QPerson.person.firstName.in("John", "Jane"));
		query.limit(100);
		query.offset(0);
		List<Person> result2 = query.fetch();

		Assert.assertTrue(result.get(0).getId().equals(result2.get(0).getId()));
		Assert.assertTrue(result.get(result.size() - 1).getId().equals(result2.get(result2.size() - 1).getId()));

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

		JPAQuery<Long> query = new JPAQuery<>(entityManager);
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

		List<Person> result = dataProvider.loadPage();

		JPAQuery<Person> query = new JPAQuery<>(entityManager);
		query.from(QPerson.person);
		query.select(QPerson.person);
		query.where(QPerson.person.firstName.in("John", "Jane").and(QPerson.person.age.goe(18)));
		query.limit(100);
		query.offset(0);
		List<Person> result2 = query.fetch();

		Assert.assertTrue(result.get(0).getId().equals(result2.get(0).getId()));
		Assert.assertTrue(result.get(result.size() - 1).getId().equals(result2.get(result2.size() - 1).getId()));

	}

	@Test
	public void test15RemoveFilter() {

		dataProvider.removeFilter("Filter");
		List<Person> result = dataProvider.loadPage();

		JPAQuery<Person> query = new JPAQuery<>(entityManager);
		query.from(QPerson.person);
		query.select(QPerson.person);
		query.where(QPerson.person.age.goe(18));
		query.limit(100);
		query.offset(0);
		List<Person> result2 = query.fetch();

		Assert.assertTrue(result.get(0).getId().equals(result2.get(0).getId()));
		Assert.assertTrue(result.get(result.size() - 1).getId().equals(result2.get(result2.size() - 1).getId()));

	}

	@Test
	public void test16Sort() {

		QDSLSort sort = new QDSLSort(QPerson.person.lastName);
		sort.setSortType(SortType.DESCENDING);
		dataProvider.addSort("Sort", sort);

		List<Person> result = dataProvider.loadPage();

		JPAQuery<Person> query = new JPAQuery<>(entityManager);
		query.from(QPerson.person);
		query.select(QPerson.person);
		query.where(QPerson.person.age.goe(18));
		query.orderBy(QPerson.person.lastName.desc());
		query.limit(100);
		query.offset(0);
		List<Person> result2 = query.fetch();

		Assert.assertTrue(result.get(0).getLastName().equals(result2.get(0).getLastName()));
		Assert.assertTrue(
				result.get(result.size() - 1).getLastName().equals(result2.get(result2.size() - 1).getLastName()));

	}

	@Test
	public void test17MultiSort() {

		QDSLSort sort = new QDSLSort(QPerson.person.firstName);
		sort.setSortType(SortType.ASCENDING);
		dataProvider.addSort("Sort2", sort);

		List<Person> result = dataProvider.loadPage();

		JPAQuery<Person> query = new JPAQuery<>(entityManager);
		query.from(QPerson.person);
		query.select(QPerson.person);
		query.where(QPerson.person.age.goe(18));
		query.orderBy(QPerson.person.lastName.desc(), QPerson.person.firstName.asc());
		query.limit(100);
		query.offset(0);
		List<Person> result2 = query.fetch();

		Assert.assertTrue(result.get(0).getLastName().equals(result2.get(0).getLastName()));
		Assert.assertTrue(
				result.get(result.size() - 1).getLastName().equals(result2.get(result2.size() - 1).getLastName()));

	}

	@Test
	public void test18RemoveSort() {

		dataProvider.removeSort("Sort");
		List<Person> result = dataProvider.loadPage();

		JPAQuery<Person> query = new JPAQuery<>(entityManager);
		query.from(QPerson.person);
		query.select(QPerson.person);
		query.where(QPerson.person.age.goe(18));
		query.orderBy(QPerson.person.firstName.asc());
		query.limit(100);
		query.offset(0);
		List<Person> result2 = query.fetch();

		Assert.assertTrue(result.get(0).getFirstName().equals(result2.get(0).getFirstName()));
		Assert.assertTrue(
				result.get(result.size() - 1).getFirstName().equals(result2.get(result2.size() - 1).getFirstName()));

	}

	@Test
	public void test19TemporalFilter() {

		dataProvider.filtersProperty().clear();
		dataProvider.sortsProperty().clear();
		TemporalQDSLFilter<Date> filter = new TemporalQDSLFilter<>(QPerson.person.dateOfBirth, TemporalType.DATE);
		filter.setFilterType(FilterType.GREATER_THAN);
		java.sql.Date date = java.sql.Date.valueOf("1990-01-01");
		filter.setFilterValues(new Date[] { date });
		dataProvider.addFilter("FILTER", filter);
		List<Person> result = dataProvider.loadPage();

		JPAQuery<Person> query = new JPAQuery<>(entityManager);
		query.from(QPerson.person);
		query.select(QPerson.person);
		query.where(QPerson.person.dateOfBirth.gt(date));
		query.limit(100);
		query.offset(0);
		List<Person> result2 = query.fetch();

		Assert.assertTrue(result.get(0).getId().equals(result2.get(0).getId()));
		Assert.assertTrue(result.get(result.size() - 1).getId().equals(result2.get(result2.size() - 1).getId()));

	}

	@Test
	public void test20Tuple() {

		AbstractQDSLJPADataSource<Tuple> tupleDataSource = new QDSLJPADataSource<>(entityManager);
		tupleDataSource.addSelectExpression(QPerson.person.id);
		tupleDataSource.addSelectExpression(QPerson.person.dateOfBirth);
		tupleDataSource.setFromExpression(QPerson.person);

		QDSLJPADataProvider<Tuple, Tuple> tupleDataProvider = new QDSLJPADataProvider<>(tupleDataSource);
		tupleDataProvider.setPageSize(100);
		tupleDataProvider.firstPage();

		List<Tuple> result = tupleDataProvider.loadPage();

		JPAQuery<Tuple> query = new JPAQuery<>(entityManager);
		query.from(QPerson.person);
		query.select(QPerson.person.id, QPerson.person.dateOfBirth);
		query.limit(100);
		query.offset(0);
		List<Tuple> result2 = query.fetch();

		Assert.assertTrue(result.get(0).get(QPerson.person.id).equals(result2.get(0).get(QPerson.person.id)) && result
				.get(0).get(QPerson.person.dateOfBirth).equals(result2.get(0).get(QPerson.person.dateOfBirth)));
		Assert.assertTrue(result.get(result.size() - 1).get(QPerson.person.id)
				.equals(result2.get(result2.size() - 1).get(QPerson.person.id))
				&& result.get(result.size() - 1).get(QPerson.person.dateOfBirth)
						.equals(result2.get(result2.size() - 1).get(QPerson.person.dateOfBirth)));

	}

}
