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
public class StringQDSLFilterTest {

	@Test
	public void test0Contains() {

		StringQDSLFilter filter = new StringQDSLFilter(QPerson.person.lastName);
		filter.setFilterType(FilterType.CONTAINS);
		filter.setFilterValues(new String[] { "a" });

		Assert.assertTrue(QPerson.person.lastName.contains("a").equals(filter.createPredicate()));

	}

	@Test
	public void test1StartsWith() {

		StringQDSLFilter filter = new StringQDSLFilter(QPerson.person.lastName);
		filter.setFilterType(FilterType.STARTS_WITH);
		filter.setFilterValues(new String[] { "a" });

		Assert.assertTrue(QPerson.person.lastName.startsWith("a").equals(filter.createPredicate()));

	}

	@Test
	public void test2EndsWith() {

		StringQDSLFilter filter = new StringQDSLFilter(QPerson.person.lastName);
		filter.setFilterType(FilterType.ENDS_WITH);
		filter.setFilterValues(new String[] { "a" });

		Assert.assertTrue(QPerson.person.lastName.endsWith("a").equals(filter.createPredicate()));

	}

	@Test
	public void test3NotContains() {

		StringQDSLFilter filter = new StringQDSLFilter(QPerson.person.lastName);
		filter.setFilterType(FilterType.NOT_CONTAINS);
		filter.setFilterValues(new String[] { "a" });

		Assert.assertTrue(QPerson.person.lastName.notLike("%a%").equals(filter.createPredicate()));

	}

	@Test
	public void test4NotStartsWith() {

		StringQDSLFilter filter = new StringQDSLFilter(QPerson.person.lastName);
		filter.setFilterType(FilterType.NOT_STARTS_WITH);
		filter.setFilterValues(new String[] { "a" });

		Assert.assertTrue(QPerson.person.lastName.notLike("a%").equals(filter.createPredicate()));

	}

	@Test
	public void test5NotEndsWith() {

		StringQDSLFilter filter = new StringQDSLFilter(QPerson.person.lastName);
		filter.setFilterType(FilterType.NOT_ENDS_WITH);
		filter.setFilterValues(new String[] { "a" });

		Assert.assertTrue(QPerson.person.lastName.notLike("%a").equals(filter.createPredicate()));

	}

}
