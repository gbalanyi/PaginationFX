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

import hu.computertechnika.paginationfx.filter.AbstractJDBCFilter;
import hu.computertechnika.paginationfx.filter.FilterType;
import hu.computertechnika.paginationfx.sort.JDBCSort;
import hu.computertechnika.paginationfx.sort.SortType;
import hu.computertechnika.paginationfx.sql.AbstractJDBCDataSource;

/**
 * @author Gábor Balanyi
 */
public class JDBCDataProvider<UT> extends
		AbstractSQLDataProvider<AbstractJDBCDataSource<Object[]>, Object[], UT, AbstractJDBCFilter<?>, JDBCSort> {

	public JDBCDataProvider(AbstractJDBCDataSource<Object[]> datasource) {
		super(datasource);
	}

	@Override
	public AbstractJDBCDataSource<Object[]> applyFilters(AbstractJDBCDataSource<Object[]> dataSource) {

		super.applyFilters(dataSource);
		for (AbstractJDBCFilter<?> filter : this.getFilters().values()) {

			if (!FilterType.NONE.equals(filter.getFilterType())) {
				dataSource.addWhereExpression(filter);
			}

		}
		return dataSource;

	}

	@Override
	public AbstractJDBCDataSource<Object[]> applySorts(AbstractJDBCDataSource<Object[]> dataSource) {

		super.applySorts(dataSource);
		for (JDBCSort sort : this.getSorts().values()) {

			if (!SortType.NONE.equals(sort.getSortType())) {
				dataSource.addOrderExpression(sort.createExpression());
			}

		}
		return dataSource;

	}

}
