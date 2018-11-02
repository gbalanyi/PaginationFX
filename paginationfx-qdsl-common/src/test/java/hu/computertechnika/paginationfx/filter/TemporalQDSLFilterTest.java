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
package hu.computertechnika.paginationfx.filter;

import java.util.Date;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import hu.computertechnika.paginationfx.sql.QPerson;

/**
 * @author Gábor Balanyi
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TemporalQDSLFilterTest {

	@Test
	public void test0Between() {

		TemporalQDSLFilter<Date> filter = new TemporalQDSLFilter<>(QPerson.person.dateOfBirth, TemporalType.DATE);
		filter.setFilterType(FilterType.BETWEEN);
		Date from = java.sql.Date.valueOf("1990-01-01");
		Date to = java.sql.Date.valueOf("2000-01-01");
		filter.setFilterValues(new Date[] { from, to });

		Assert.assertTrue(QPerson.person.dateOfBirth.between(from, to).equals(filter.createPredicate()));

	}

	@Test
	public void test1GreaterThan() {

		TemporalQDSLFilter<Date> filter = new TemporalQDSLFilter<>(QPerson.person.dateOfBirth, TemporalType.DATE);
		filter.setFilterType(FilterType.GREATER_THAN);
		Date date = java.sql.Date.valueOf("1990-01-01");
		filter.setFilterValues(new Date[] { date });

		Assert.assertTrue(QPerson.person.dateOfBirth.gt(date).equals(filter.createPredicate()));

	}

	@Test
	public void test2GreaterThanOrEqual() {

		TemporalQDSLFilter<Date> filter = new TemporalQDSLFilter<>(QPerson.person.dateOfBirth, TemporalType.DATE);
		filter.setFilterType(FilterType.GREATER_THAN_OR_EQUAL);
		Date date = java.sql.Date.valueOf("1990-01-01");
		filter.setFilterValues(new Date[] { date });

		Assert.assertTrue(QPerson.person.dateOfBirth.goe(date).equals(filter.createPredicate()));

	}

	@Test
	public void test3In() {

		TemporalQDSLFilter<Date> filter = new TemporalQDSLFilter<>(QPerson.person.dateOfBirth, TemporalType.DATE);
		filter.setFilterType(FilterType.IN);
		Date date1 = java.sql.Date.valueOf("1990-01-01");
		Date date2 = java.sql.Date.valueOf("2000-01-01");
		filter.setFilterValues(new Date[] { date1, date2 });

		Assert.assertTrue(QPerson.person.dateOfBirth.in(date1, date2).equals(filter.createPredicate()));

	}

	@Test
	public void test4LessThan() {

		TemporalQDSLFilter<Date> filter = new TemporalQDSLFilter<>(QPerson.person.dateOfBirth, TemporalType.DATE);
		filter.setFilterType(FilterType.LESS_THAN);
		Date date = java.sql.Date.valueOf("1990-01-01");
		filter.setFilterValues(new Date[] { date });

		Assert.assertTrue(QPerson.person.dateOfBirth.lt(date).equals(filter.createPredicate()));

	}

	@Test
	public void test5LessThanOrEqual() {

		TemporalQDSLFilter<Date> filter = new TemporalQDSLFilter<>(QPerson.person.dateOfBirth, TemporalType.DATE);
		filter.setFilterType(FilterType.LESS_THAN_OR_EQUAL);
		Date date = java.sql.Date.valueOf("1990-01-01");
		filter.setFilterValues(new Date[] { date });

		Assert.assertTrue(QPerson.person.dateOfBirth.loe(date).equals(filter.createPredicate()));

	}

	@Test
	public void test6NotBetween() {

		TemporalQDSLFilter<Date> filter = new TemporalQDSLFilter<>(QPerson.person.dateOfBirth, TemporalType.DATE);
		filter.setFilterType(FilterType.NOT_BETWEEN);
		Date from = java.sql.Date.valueOf("1990-01-01");
		Date to = java.sql.Date.valueOf("2000-01-01");
		filter.setFilterValues(new Date[] { from, to });

		Assert.assertTrue(QPerson.person.dateOfBirth.notBetween(from, to).equals(filter.createPredicate()));

	}

	@Test
	public void test7NotIn() {

		TemporalQDSLFilter<Date> filter = new TemporalQDSLFilter<>(QPerson.person.dateOfBirth, TemporalType.DATE);
		filter.setFilterType(FilterType.NOT_IN);
		Date date1 = java.sql.Date.valueOf("1990-01-01");
		Date date2 = java.sql.Date.valueOf("2000-01-01");
		filter.setFilterValues(new Date[] { date1, date2 });

		Assert.assertTrue(QPerson.person.dateOfBirth.notIn(date1, date2).equals(filter.createPredicate()));

	}

}
