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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * @author Gábor Balanyi
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ComparableFilterTest {

	@Test
	public void test000UnsupportedFilterType() {

		ComparableFilter<Integer> filter = new ComparableFilter<>();
		try {
			filter.setFilterType(FilterType.CONTAINS);
		} catch (Exception exception) {
			Assert.assertTrue(exception instanceof IllegalArgumentException);
		}

	}

	@Test
	public void test001None() {

		List<Integer> list = Arrays.asList(1, 2, 3, null);
		ComparableFilter<Integer> filter = new ComparableFilter<>();
		filter.setFilterType(FilterType.NONE);
		filter.setFilterValues(null);

		list = list.stream().filter(filter.createPredicate()).collect(Collectors.toList());
		Assert.assertTrue(list.size() == 4);

	}

	@Test
	public void test010Equal() {

		List<Integer> list = Arrays.asList(1, 2, 3, null);
		ComparableFilter<Integer> filter = new ComparableFilter<>();
		filter.setFilterType(FilterType.EQUAL);
		filter.setFilterValues(new Integer[] { 2 });

		list = list.stream().filter(filter.createPredicate()).collect(Collectors.toList());
		Assert.assertTrue(list.size() == 1 && list.contains(2));

	}

	@Test
	public void test011EqualNull() {

		List<Integer> list = Arrays.asList(1, 2, 3, null);
		ComparableFilter<Integer> filter = new ComparableFilter<>();
		filter.setFilterType(FilterType.EQUAL);
		filter.setFilterValues(new Integer[] { null });

		list = list.stream().filter(filter.createPredicate()).collect(Collectors.toList());
		Assert.assertTrue(list.size() == 1 && list.contains(null));

	}

	@Test
	public void test020NotEqual() {

		List<Integer> list = Arrays.asList(1, 2, 3, null);
		ComparableFilter<Integer> filter = new ComparableFilter<>();
		filter.setFilterType(FilterType.NOT_EQUAL);
		filter.setFilterValues(new Integer[] { 2 });

		list = list.stream().filter(filter.createPredicate()).collect(Collectors.toList());
		Assert.assertTrue(list.size() == 3 && !list.contains(2));

	}

	@Test
	public void test021NotEqualNull() {

		List<Integer> list = Arrays.asList(1, 2, 3, null);
		ComparableFilter<Integer> filter = new ComparableFilter<>();
		filter.setFilterType(FilterType.NOT_EQUAL);
		filter.setFilterValues(new Integer[] { null });

		list = list.stream().filter(filter.createPredicate()).collect(Collectors.toList());
		Assert.assertTrue(list.size() == 3 && !list.contains(null));

	}

	@Test
	public void test030GreaterThan() {

		List<Integer> list = Arrays.asList(1, 2, 3, null);
		ComparableFilter<Integer> filter = new ComparableFilter<>();
		filter.setFilterType(FilterType.GREATER_THAN);
		filter.setFilterValues(new Integer[] { 2 });

		list = list.stream().filter(filter.createPredicate()).collect(Collectors.toList());
		Assert.assertTrue(list.size() == 1 && list.contains(3));

	}

	@Test
	public void test031GreaterThanNull() {

		List<Integer> list = Arrays.asList(1, 2, 3, null);
		ComparableFilter<Integer> filter = new ComparableFilter<>();
		filter.setFilterType(FilterType.GREATER_THAN);
		filter.setFilterValues(new Integer[] { null });

		list = list.stream().filter(filter.createPredicate()).collect(Collectors.toList());
		Assert.assertTrue(list.size() == 3 && !list.contains(null));

	}

	@Test
	public void test040GreaterThanOrEqual() {

		List<Integer> list = Arrays.asList(1, 2, 3, null);
		ComparableFilter<Integer> filter = new ComparableFilter<>();
		filter.setFilterType(FilterType.GREATER_THAN_OR_EQUAL);
		filter.setFilterValues(new Integer[] { 2 });

		list = list.stream().filter(filter.createPredicate()).collect(Collectors.toList());
		Assert.assertTrue(list.size() == 2 && list.contains(2));

	}

	@Test
	public void test041GreaterThanOrEqualNull() {

		List<Integer> list = Arrays.asList(1, 2, 3, null);
		ComparableFilter<Integer> filter = new ComparableFilter<>();
		filter.setFilterType(FilterType.GREATER_THAN_OR_EQUAL);
		filter.setFilterValues(new Integer[] { null });

		list = list.stream().filter(filter.createPredicate()).collect(Collectors.toList());
		Assert.assertTrue(list.size() == 4 && list.contains(null));

	}

	@Test
	public void test050LessThan() {

		List<Integer> list = Arrays.asList(1, 2, 3, null);
		ComparableFilter<Integer> filter = new ComparableFilter<>();
		filter.setFilterType(FilterType.LESS_THAN);
		filter.setFilterValues(new Integer[] { 2 });

		list = list.stream().filter(filter.createPredicate()).collect(Collectors.toList());
		Assert.assertTrue(list.size() == 2 && list.contains(1));

	}

	@Test
	public void test051LessThanNull() {

		List<Integer> list = Arrays.asList(1, 2, 3, null);
		ComparableFilter<Integer> filter = new ComparableFilter<>();
		filter.setFilterType(FilterType.LESS_THAN);
		filter.setFilterValues(new Integer[] { null });

		list = list.stream().filter(filter.createPredicate()).collect(Collectors.toList());
		Assert.assertTrue(list.isEmpty());

	}

	@Test
	public void test060LessThanOrEqual() {

		List<Integer> list = Arrays.asList(1, 2, 3, null);
		ComparableFilter<Integer> filter = new ComparableFilter<>();
		filter.setFilterType(FilterType.LESS_THAN_OR_EQUAL);
		filter.setFilterValues(new Integer[] { 2 });

		list = list.stream().filter(filter.createPredicate()).collect(Collectors.toList());
		Assert.assertTrue(list.size() == 3 && list.contains(2));

	}

	@Test
	public void test061LessThanOrEqualNull() {

		List<Integer> list = Arrays.asList(1, 2, 3, null);
		ComparableFilter<Integer> filter = new ComparableFilter<>();
		filter.setFilterType(FilterType.LESS_THAN_OR_EQUAL);
		filter.setFilterValues(new Integer[] { null });

		list = list.stream().filter(filter.createPredicate()).collect(Collectors.toList());
		Assert.assertTrue(list.size() == 1 && list.contains(null));

	}

	@Test
	public void test070Between() {

		List<Integer> list = Arrays.asList(1, 2, 3, null);
		ComparableFilter<Integer> filter = new ComparableFilter<>();
		filter.setFilterType(FilterType.BETWEEN);
		filter.setFilterValues(new Integer[] { 2, 3 });

		list = list.stream().filter(filter.createPredicate()).collect(Collectors.toList());
		Assert.assertTrue(list.size() == 2 && list.contains(3));

	}

	@Test
	public void test071BetweenNull() {

		List<Integer> list = Arrays.asList(1, 2, 3, null);
		ComparableFilter<Integer> filter = new ComparableFilter<>();
		filter.setFilterType(FilterType.BETWEEN);
		filter.setFilterValues(new Integer[] { null, 2 });

		list = list.stream().filter(filter.createPredicate()).collect(Collectors.toList());
		Assert.assertTrue(list.size() == 3 && list.contains(null));

	}

	@Test
	public void test072BetweenNullRange() {

		List<Integer> list = Arrays.asList(1, 2, 3, null);
		ComparableFilter<Integer> filter = new ComparableFilter<>();
		filter.setFilterType(FilterType.BETWEEN);
		filter.setFilterValues(new Integer[] { null, null });

		list = list.stream().filter(filter.createPredicate()).collect(Collectors.toList());
		Assert.assertTrue(list.size() == 1 && list.contains(null));

	}
	
	@Test
	public void test080NotBetween() {

		List<Integer> list = Arrays.asList(1, 2, 3, null);
		ComparableFilter<Integer> filter = new ComparableFilter<>();
		filter.setFilterType(FilterType.NOT_BETWEEN);
		filter.setFilterValues(new Integer[] { 2, 3 });

		list = list.stream().filter(filter.createPredicate()).collect(Collectors.toList());
		Assert.assertTrue(list.size() == 2 && list.contains(1));

	}

	@Test
	public void test081NotBetweenNull() {

		List<Integer> list = Arrays.asList(1, 2, 3, null);
		ComparableFilter<Integer> filter = new ComparableFilter<>();
		filter.setFilterType(FilterType.NOT_BETWEEN);
		filter.setFilterValues(new Integer[] { null, 2 });

		list = list.stream().filter(filter.createPredicate()).collect(Collectors.toList());
		Assert.assertTrue(list.size() == 1 && list.contains(3));

	}
	
	@Test
	public void test082NotBetweenNullRange() {

		List<Integer> list = Arrays.asList(1, 2, 3, null);
		ComparableFilter<Integer> filter = new ComparableFilter<>();
		filter.setFilterType(FilterType.NOT_BETWEEN);
		filter.setFilterValues(new Integer[] { null, null });

		list = list.stream().filter(filter.createPredicate()).collect(Collectors.toList());
		Assert.assertTrue(list.size() == 3 && !list.contains(null));

	}
	
	@Test
	public void test090In() {

		List<Integer> list = Arrays.asList(1, 2, 3, null);
		ComparableFilter<Integer> filter = new ComparableFilter<>();
		filter.setFilterType(FilterType.IN);
		filter.setFilterValues(new Integer[] { 1, 2, 3 });

		list = list.stream().filter(filter.createPredicate()).collect(Collectors.toList());
		Assert.assertTrue(list.size() == 3 && !list.contains(null));

	}
	
	@Test
	public void test091InNull() {

		List<Integer> list = Arrays.asList(1, 2, 3, null);
		ComparableFilter<Integer> filter = new ComparableFilter<>();
		filter.setFilterType(FilterType.IN);
		filter.setFilterValues(new Integer[] { null, 1, 2 });

		list = list.stream().filter(filter.createPredicate()).collect(Collectors.toList());
		Assert.assertTrue(list.size() == 3 && !list.contains(3));

	}
	
	@Test
	public void test100NotIn() {

		List<Integer> list = Arrays.asList(1, 2, 3, null);
		ComparableFilter<Integer> filter = new ComparableFilter<>();
		filter.setFilterType(FilterType.NOT_IN);
		filter.setFilterValues(new Integer[] { 1, 2, 3 });

		list = list.stream().filter(filter.createPredicate()).collect(Collectors.toList());
		Assert.assertTrue(list.size() == 1 && list.contains(null));

	}
	
	@Test
	public void test101NotInNull() {

		List<Integer> list = Arrays.asList(1, 2, 3, null);
		ComparableFilter<Integer> filter = new ComparableFilter<>();
		filter.setFilterType(FilterType.NOT_IN);
		filter.setFilterValues(new Integer[] { null, 1, 2 });

		list = list.stream().filter(filter.createPredicate()).collect(Collectors.toList());
		Assert.assertTrue(list.size() == 1 && list.contains(3));

	}
	
}
