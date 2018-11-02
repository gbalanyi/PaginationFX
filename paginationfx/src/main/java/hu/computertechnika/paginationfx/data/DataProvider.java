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

import java.util.List;

import hu.computertechnika.paginationfx.PaginationSupport;
import hu.computertechnika.paginationfx.filter.AbstractFilter;
import hu.computertechnika.paginationfx.filter.FilterSupport;
import hu.computertechnika.paginationfx.sort.AbstractSort;
import hu.computertechnika.paginationfx.sort.SortSupport;
import javafx.beans.property.ObjectProperty;
import javafx.util.Callback;

/**
 * @author Gábor Balanyi
 */
public interface DataProvider<DS, MT, UT, FT extends AbstractFilter<?, ?>, ST extends AbstractSort<?>>
		extends PaginationSupport, FilterSupport<FT>, SortSupport<ST> {

	void setDataSource(DS dataSource);

	DS getDataSource();

	ObjectProperty<DS> dataSourceProperty();

	void setPageSize(int pageSize);

	int getPageSize();

	ObjectProperty<Integer> pageSizeProperty();

	void setTableViewValueFactory(Callback<MT, UT> valueFactory);

	Callback<MT, UT> getTableViewValueFactory();

	ObjectProperty<Callback<MT, UT>> tableViewValueFactoryProperty();

	List<UT> loadPage();

	DS applyFilters(DS dataSource);

	DS applySorts(DS dataSource);

}
