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

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import hu.computertechnika.paginationfx.sql.QPerson;

/**
 * @author Gábor Balanyi
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ComparableQDSLFilterTest {

	@Test
	public void test0Between() {

		ComparableQDSLFilter<String> filter = new ComparableQDSLFilter<>(QPerson.person.firstName);
		filter.setFilterType(FilterType.BETWEEN);
		filter.setFilterValues(new String[] { "a", "b" });

		Assert.assertTrue(QPerson.person.firstName.between("a", "b").equals(filter.createPredicate()));

	}

	@Test
	public void test1GreaterThan() {

		ComparableQDSLFilter<String> filter = new ComparableQDSLFilter<>(QPerson.person.firstName);
		filter.setFilterType(FilterType.GREATER_THAN);
		filter.setFilterValues(new String[] { "a" });

		Assert.assertTrue(QPerson.person.firstName.gt("a").equals(filter.createPredicate()));

	}

	@Test
	public void test2GreaterThanOrEqual() {

		ComparableQDSLFilter<String> filter = new ComparableQDSLFilter<>(QPerson.person.firstName);
		filter.setFilterType(FilterType.GREATER_THAN_OR_EQUAL);
		filter.setFilterValues(new String[] { "a" });

		Assert.assertTrue(QPerson.person.firstName.goe("a").equals(filter.createPredicate()));

	}

	@Test
	public void test3In() {

		ComparableQDSLFilter<String> filter = new ComparableQDSLFilter<>(QPerson.person.firstName);
		filter.setFilterType(FilterType.IN);
		filter.setFilterValues(new String[] { "a", "b" });

		Assert.assertTrue(QPerson.person.firstName.in("a", "b").equals(filter.createPredicate()));

	}

	@Test
	public void test4LessThan() {

		ComparableQDSLFilter<String> filter = new ComparableQDSLFilter<>(QPerson.person.firstName);
		filter.setFilterType(FilterType.LESS_THAN);
		filter.setFilterValues(new String[] { "z" });

		Assert.assertTrue(QPerson.person.firstName.lt("z").equals(filter.createPredicate()));

	}

	@Test
	public void test5LessThanOrEqual() {

		ComparableQDSLFilter<String> filter = new ComparableQDSLFilter<>(QPerson.person.firstName);
		filter.setFilterType(FilterType.LESS_THAN_OR_EQUAL);
		filter.setFilterValues(new String[] { "z" });

		Assert.assertTrue(QPerson.person.firstName.loe("z").equals(filter.createPredicate()));

	}

	@Test
	public void test6NotBetween() {

		ComparableQDSLFilter<String> filter = new ComparableQDSLFilter<>(QPerson.person.firstName);
		filter.setFilterType(FilterType.NOT_BETWEEN);
		filter.setFilterValues(new String[] { "a", "c" });

		Assert.assertTrue(QPerson.person.firstName.notBetween("a", "c").equals(filter.createPredicate()));

	}

	@Test
	public void test7NotIn() {

		ComparableQDSLFilter<String> filter = new ComparableQDSLFilter<>(QPerson.person.firstName);
		filter.setFilterType(FilterType.NOT_IN);
		filter.setFilterValues(new String[] { "a", "b" });

		Assert.assertTrue(QPerson.person.firstName.notIn("a", "b").equals(filter.createPredicate()));

	}

}
