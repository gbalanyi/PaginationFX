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
public class ObjectJDBCFilterTest {

	@Test
	public void test0None() {

		ObjectJDBCFilter filter = new ObjectJDBCFilter("column");
		Assert.assertTrue(filter.createPredicate() == null);

	}

	@Test
	public void test1Equal() {

		ObjectJDBCFilter filter = new ObjectJDBCFilter("column");
		filter.setFilterType(FilterType.EQUAL);
		filter.setFilterValues(new Integer[] { 1 });

		Assert.assertTrue("column = ?".equals(filter.createPredicate()));

	}

	@Test
	public void test2EqualNull() {

		ObjectJDBCFilter filter = new ObjectJDBCFilter("column");
		filter.setFilterType(FilterType.EQUAL);
		filter.setFilterValues(new Integer[] { null });

		Assert.assertTrue("column IS NULL".equals(filter.createPredicate()));

	}

	@Test
	public void test3NotEqual() {

		ObjectJDBCFilter filter = new ObjectJDBCFilter("column");
		filter.setFilterType(FilterType.NOT_EQUAL);
		filter.setFilterValues(new Integer[] { 1 });

		Assert.assertTrue("column <> ?".equals(filter.createPredicate()));

	}

	@Test
	public void test4NotEqualNull() {

		ObjectJDBCFilter filter = new ObjectJDBCFilter("column");
		filter.setFilterType(FilterType.NOT_EQUAL);
		filter.setFilterValues(new Integer[] { null });

		Assert.assertTrue("column IS NOT NULL".equals(filter.createPredicate()));

	}
}
