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

import hu.computertechnika.paginationfx.filter.AbstractSQLFilter;
import hu.computertechnika.paginationfx.sort.AbstractSQLSort;
import hu.computertechnika.paginationfx.sql.AbstractSQLDataSource;

/**
 * @author Gábor Balanyi
 */
public abstract class AbstractSQLDataProvider<DS extends AbstractSQLDataSource<?, ?, ?, ?, ?, MT>, MT, UT, FT extends AbstractSQLFilter<?, ?, ?>, ST extends AbstractSQLSort<?, ?>>
		extends AbstractDataProvider<DS, MT, UT, FT, ST> {

	public AbstractSQLDataProvider(DS datasource) {
		super(datasource);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UT> loadPage() {

		this.getDataSource().setMaxResults(this.getPageSize());
		this.getDataSource().setOffset(this.getCurrentPageIndex() * this.getPageSize());

		this.applyFilters(this.getDataSource());
		this.applySorts(this.getDataSource());

		List<MT> list = this.getDataSource().executeQuery();

		this.calculatePageNumber((int) this.getDataSource().executeCountQuery());

		if (!list.isEmpty()) {

			if (this.getTableViewValueFactory() != null) {

				List<UT> result = new ArrayList<>();
				for (MT model : list) {
					result.add(this.getTableViewValueFactory().call(model));
				}
				return result;

			}

		}

		return (List<UT>) list;

	}

	@Override
	public DS applyFilters(DS dataSource) {

		dataSource.getWhereExpressions().clear();
		return dataSource;

	}

	@Override
	public DS applySorts(DS dataSource) {

		dataSource.getOrderExpressions().clear();
		return dataSource;

	}

}
