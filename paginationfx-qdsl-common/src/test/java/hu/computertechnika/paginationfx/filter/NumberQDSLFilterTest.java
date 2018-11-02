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
public class NumberQDSLFilterTest {

	@Test
	public void test0Between() {

		NumberQDSLFilter<Integer> filter = new NumberQDSLFilter<>(QPerson.person.age);
		filter.setFilterType(FilterType.BETWEEN);
		filter.setFilterValues(new Integer[] { 0, 10 });

		Assert.assertTrue(QPerson.person.age.between(0, 10).equals(filter.createPredicate()));

	}

	@Test
	public void test1GreaterThan() {

		NumberQDSLFilter<Integer> filter = new NumberQDSLFilter<>(QPerson.person.age);
		filter.setFilterType(FilterType.GREATER_THAN);
		filter.setFilterValues(new Integer[] { 10 });

		Assert.assertTrue(QPerson.person.age.gt(10).equals(filter.createPredicate()));

	}

	@Test
	public void test2GreaterThanOrEqual() {

		NumberQDSLFilter<Integer> filter = new NumberQDSLFilter<>(QPerson.person.age);
		filter.setFilterType(FilterType.GREATER_THAN_OR_EQUAL);
		filter.setFilterValues(new Integer[] { 10 });

		Assert.assertTrue(QPerson.person.age.goe(10).equals(filter.createPredicate()));

	}

	@Test
	public void test3In() {

		NumberQDSLFilter<Integer> filter = new NumberQDSLFilter<>(QPerson.person.age);
		filter.setFilterType(FilterType.IN);
		filter.setFilterValues(new Integer[] { 10, 20 });

		Assert.assertTrue(QPerson.person.age.in(10, 20).equals(filter.createPredicate()));

	}

	@Test
	public void test4LessThan() {

		NumberQDSLFilter<Integer> filter = new NumberQDSLFilter<>(QPerson.person.age);
		filter.setFilterType(FilterType.LESS_THAN);
		filter.setFilterValues(new Integer[] { 100 });

		Assert.assertTrue(QPerson.person.age.lt(100).equals(filter.createPredicate()));

	}

	@Test
	public void test5LessThanOrEqual() {

		NumberQDSLFilter<Integer> filter = new NumberQDSLFilter<>(QPerson.person.age);
		filter.setFilterType(FilterType.LESS_THAN_OR_EQUAL);
		filter.setFilterValues(new Integer[] { 50 });

		Assert.assertTrue(QPerson.person.age.loe(50).equals(filter.createPredicate()));

	}

	@Test
	public void test6NotBetween() {

		NumberQDSLFilter<Integer> filter = new NumberQDSLFilter<>(QPerson.person.age);
		filter.setFilterType(FilterType.NOT_BETWEEN);
		filter.setFilterValues(new Integer[] { 20, 40 });

		Assert.assertTrue(QPerson.person.age.notBetween(20, 40).equals(filter.createPredicate()));

	}

	@Test
	public void test7NotIn() {

		NumberQDSLFilter<Integer> filter = new NumberQDSLFilter<>(QPerson.person.age);
		filter.setFilterType(FilterType.NOT_IN);
		filter.setFilterValues(new Integer[] { 20, 40 });

		Assert.assertTrue(QPerson.person.age.notIn(20, 40).equals(filter.createPredicate()));

	}

}
