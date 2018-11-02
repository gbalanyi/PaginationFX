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

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.computertechnika.paginationfx.filter.ComparableFilter;
import hu.computertechnika.paginationfx.filter.FilterType;
import hu.computertechnika.paginationfx.sort.ComparableSort;
import hu.computertechnika.paginationfx.sort.SortType;

/**
 * @author Gábor Balanyi
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ComparableCollectionDataProviderTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(ComparableCollectionDataProviderTest.class);

	private static ComparableCollectionDataProvider<Integer, Integer> provider;

	@BeforeClass
	public static void init() {

		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < 94; i++) {
			list.add(i);
		}
		provider = new ComparableCollectionDataProvider<>(list);

	}

	@Test
	public void test00PagCount() {

		provider.setPageSize(10);
		List<Integer> loadPage = provider.loadPage();
		LOGGER.debug("LoadPage: " + loadPage);

		Assert.assertTrue(loadPage.size() == 10);

	}

	@Test
	public void test01LastPage() {

		provider.lastPage();
		List<Integer> loadPage = provider.loadPage();
		LOGGER.debug("LastPage: " + loadPage);

		Assert.assertTrue(loadPage.size() == 4 && loadPage.get(0) == 90 && loadPage.get(loadPage.size() - 1) == 93);

	}

	@Test
	public void test02PreviousPage() {

		provider.previousPage();
		List<Integer> loadPage = provider.loadPage();
		LOGGER.debug("PreviousPage: " + loadPage);

		Assert.assertTrue(loadPage.size() == provider.getPageSize() && loadPage.get(0) == 80
				&& loadPage.get(loadPage.size() - 1) == 89);

	}

	@Test
	public void test03FirstPage() {

		provider.firstPage();
		List<Integer> loadPage = provider.loadPage();
		LOGGER.debug("FirstPage: " + loadPage);

		Assert.assertTrue(loadPage.size() == provider.getPageSize() && loadPage.get(0) == 0
				&& loadPage.get(loadPage.size() - 1) == 9);

	}

	@Test
	public void test04NextPage() {

		provider.nextPage();
		List<Integer> loadPage = provider.loadPage();
		LOGGER.debug("NextPage: " + loadPage);

		Assert.assertTrue(loadPage.size() == provider.getPageSize() && loadPage.get(0) == 10
				&& loadPage.get(loadPage.size() - 1) == 19);

	}

	@Test
	public void test05JumpForward() {

		provider.jumpPage(4);
		List<Integer> loadPage = provider.loadPage();
		LOGGER.debug("JumpForward: " + loadPage);

		Assert.assertTrue(loadPage.size() == provider.getPageSize() && loadPage.get(0) == 50
				&& loadPage.get(loadPage.size() - 1) == 59);

	}

	@Test
	public void test06JumpBackward() {

		provider.jumpPage(-5);
		List<Integer> loadPage = provider.loadPage();
		LOGGER.debug("JumpBackward: " + loadPage);

		Assert.assertTrue(loadPage.size() == provider.getPageSize() && loadPage.get(0) == 0
				&& loadPage.get(loadPage.size() - 1) == 9);

	}

	@Test
	public void test07MaxPageIndex() {

		provider.setCurrentPageIndex(100);
		List<Integer> loadPage = provider.loadPage();
		LOGGER.debug("MaxPageIndex: " + loadPage);

		Assert.assertTrue(loadPage.size() == 4 && loadPage.get(0) == 90 && loadPage.get(loadPage.size() - 1) == 93);

	}

	@Test
	public void test08NegativePageIndex() {

		provider.setCurrentPageIndex(-10);
		List<Integer> loadPage = provider.loadPage();
		LOGGER.debug("NegativePageIndex: " + loadPage);

		Assert.assertTrue(loadPage.size() == provider.getPageSize() && loadPage.get(0) == 0
				&& loadPage.get(loadPage.size() - 1) == 9);

	}

	@Test
	public void test09NegativePageSize() {

		provider.setPageSize(-10);
		List<Integer> loadPage = provider.loadPage();
		LOGGER.debug("NegativePageSize: " + loadPage);

		Assert.assertTrue(loadPage.size() == 1 && loadPage.get(0) == 0);

	}

	@Test
	public void test10MaxPageSize() {

		provider.setPageSize(100);
		List<Integer> loadPage = provider.loadPage();
		LOGGER.debug("MaxPageSize: " + loadPage);

		Assert.assertTrue(
				provider.getPageNumber() == 1 && loadPage.get(0) == 0 && loadPage.get(loadPage.size() - 1) == 93);

		provider.setPageSize(10);
		provider.loadPage();

	}

	@Test
	public void test11SingleFilter() {

		provider.lastPage();
		ComparableFilter<Integer> filter = new ComparableFilter<>();
		filter.setFilterType(FilterType.BETWEEN);
		filter.setFilterValues(new Integer[] { 10, 20 });
		provider.addFilter("FILTER1", filter);
		List<Integer> loadPage = provider.loadPage();
		LOGGER.debug("SingleFilter: " + loadPage);

		Assert.assertTrue(provider.getPageNumber() == 2 && loadPage.size() == 1 && loadPage.get(0) == 20);

	}

	@Test
	public void test12SingleFilterFirstPage() {

		provider.firstPage();
		List<Integer> loadPage = provider.loadPage();
		LOGGER.debug("SingleFilterFirstPage: " + loadPage);

		Assert.assertTrue(loadPage.size() == provider.getPageSize() && loadPage.get(0) == 10
				&& loadPage.get(loadPage.size() - 1) == 19);

	}

	@Test
	public void test13MultiFilter() {

		ComparableFilter<Integer> filter = new ComparableFilter<>();
		filter.setFilterType(FilterType.GREATER_THAN);
		filter.setFilterValues(new Integer[] { 15 });
		provider.addFilter("FILTER2", filter);
		List<Integer> loadPage = provider.loadPage();
		LOGGER.debug("MultiFilter: " + loadPage);

		Assert.assertTrue(provider.getPageNumber() == 1 && loadPage.size() == 5 && loadPage.get(0) == 16);

	}

	@Test
	public void test14RemoveFilter() {

		provider.removeFilter("FILTER1");
		List<Integer> loadPage = provider.loadPage();
		LOGGER.debug("RemoveFilter: " + loadPage);

		Assert.assertTrue(
				provider.getPageNumber() == 8 && loadPage.size() == provider.getPageSize() && loadPage.get(0) == 16);

	}

	@Test
	public void test15FilterEmptyResult() {

		ComparableFilter<Integer> filter = new ComparableFilter<>();
		filter.setFilterType(FilterType.GREATER_THAN);
		filter.setFilterValues(new Integer[] { 100 });
		provider.addFilter("EMPTY_FILTER", filter);
		List<Integer> loadPage = provider.loadPage();
		provider.removeFilter("EMPTY_FILTER");
		LOGGER.debug("FilterEmptyResult: " + loadPage);

		Assert.assertTrue(provider.getPageNumber() == 0 && loadPage.isEmpty());

	}

	@Test
	public void test16SingleSort() {

		ComparableSort<Integer> sort = new ComparableSort<>();
		sort.setSortType(SortType.DESCENDING);
		provider.addSort("SORT1", sort);
		List<Integer> loadPage = provider.loadPage();
		LOGGER.debug("SingleSort: " + loadPage);

		Assert.assertTrue(loadPage.get(0) == 93 && loadPage.get(loadPage.size() - 1) == 84);

	}

	@Test
	public void test17MultiSort() {

		ComparableSort<Integer> sort = new ComparableSort<>();
		provider.addSort("SORT2", sort);
		try {
			provider.loadPage();
		} catch (Exception e) {
			Assert.assertTrue(e instanceof UnsupportedOperationException);
		}

	}

	@Test
	public void test18RemoveSort() {

		provider.removeSort("SORT2");
		provider.lastPage();
		List<Integer> loadPage = provider.loadPage();
		LOGGER.debug("RemoveSort: " + loadPage);

		Assert.assertTrue(loadPage.get(0) == 23 && loadPage.get(loadPage.size() - 1) == 16);

	}

}
