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

/**
 * @author Gábor Balanyi
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ComparableJDBCFilterTest {

	@Test
	public void test0Between() {

		ComparableJDBCFilter<Integer> filter = new ComparableJDBCFilter<>("column");
		filter.setFilterType(FilterType.BETWEEN);

		Assert.assertTrue("column BETWEEN ? AND ?".equals(filter.createPredicate()));

	}

	@Test
	public void test1GreaterThan() {

		ComparableJDBCFilter<Integer> filter = new ComparableJDBCFilter<>("column");
		filter.setFilterType(FilterType.GREATER_THAN);

		Assert.assertTrue("column > ?".equals(filter.createPredicate()));

	}

	@Test
	public void test2GreaterThanOrEqual() {

		ComparableJDBCFilter<Integer> filter = new ComparableJDBCFilter<>("column");
		filter.setFilterType(FilterType.GREATER_THAN_OR_EQUAL);

		Assert.assertTrue("column >= ?".equals(filter.createPredicate()));

	}

	@Test
	public void test3In() {

		ComparableJDBCFilter<Integer> filter = new ComparableJDBCFilter<>("column");
		filter.setFilterType(FilterType.IN);
		filter.setFilterValues(new Integer[] { 1, 2 });

		Assert.assertTrue("column IN (?, ?)".equals(filter.createPredicate()));

	}

	@Test
	public void test4LessThan() {

		ComparableJDBCFilter<Integer> filter = new ComparableJDBCFilter<>("column");
		filter.setFilterType(FilterType.LESS_THAN);

		Assert.assertTrue("column < ?".equals(filter.createPredicate()));

	}

	@Test
	public void test5LessThanOrEqual() {

		ComparableJDBCFilter<Integer> filter = new ComparableJDBCFilter<>("column");
		filter.setFilterType(FilterType.LESS_THAN_OR_EQUAL);

		Assert.assertTrue("column <= ?".equals(filter.createPredicate()));

	}

	@Test
	public void test6NotBetween() {

		ComparableJDBCFilter<Integer> filter = new ComparableJDBCFilter<>("column");
		filter.setFilterType(FilterType.NOT_BETWEEN);

		Assert.assertTrue("column NOT BETWEEN ? AND ?".equals(filter.createPredicate()));

	}

	@Test
	public void test7NotIn() {

		ComparableJDBCFilter<Integer> filter = new ComparableJDBCFilter<>("column");
		filter.setFilterType(FilterType.NOT_IN);
		filter.setFilterValues(new Integer[] { 1, 2 });

		Assert.assertTrue("column NOT IN (?, ?)".equals(filter.createPredicate()));

	}

}
