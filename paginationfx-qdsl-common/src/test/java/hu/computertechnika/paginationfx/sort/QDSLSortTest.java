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
package hu.computertechnika.paginationfx.sort;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import hu.computertechnika.paginationfx.sql.QPerson;

/**
 * @author Gábor Balanyi
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QDSLSortTest {

	@Test
	public void test0None() {

		QDSLSort sort = new QDSLSort(QPerson.person.firstName);
		Assert.assertTrue(sort.createExpression() == null);

	}

	@Test
	public void test1Asc() {

		QDSLSort sort = new QDSLSort(QPerson.person.firstName);
		sort.setSortType(SortType.ASCENDING);
		Assert.assertTrue(QPerson.person.firstName.asc().equals(sort.createExpression()));

	}

	@Test
	public void test2Desc() {

		QDSLSort sort = new QDSLSort(QPerson.person.firstName);
		sort.setSortType(SortType.DESCENDING);
		Assert.assertTrue(QPerson.person.firstName.desc().equals(sort.createExpression()));

	}

}
