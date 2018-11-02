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

import hu.computertechnika.paginationfx.filter.AbstractQDSLFilter;
import hu.computertechnika.paginationfx.filter.FilterType;
import hu.computertechnika.paginationfx.sort.QDSLSort;
import hu.computertechnika.paginationfx.sort.SortType;
import hu.computertechnika.paginationfx.sql.AbstractQDSLJDBCDataSource;

/**
 * @author Gábor Balanyi
 */
public class QDSLJDBCDataProvider<MT, UT> extends
		AbstractSQLDataProvider<AbstractQDSLJDBCDataSource<MT>, MT, UT, AbstractQDSLFilter<?, ?>, QDSLSort> {

	public QDSLJDBCDataProvider(AbstractQDSLJDBCDataSource<MT> datasource) {
		super(datasource);
	}

	@Override
	public AbstractQDSLJDBCDataSource<MT> applyFilters(AbstractQDSLJDBCDataSource<MT> dataSource) {

		super.applyFilters(dataSource);
		for (AbstractQDSLFilter<?, ?> filter : this.getFilters().values()) {

			if (!FilterType.NONE.equals(filter.getFilterType())) {
				dataSource.addWhereExpression(filter.createPredicate());
			}

		}
		return dataSource;

	}

	@Override
	public AbstractQDSLJDBCDataSource<MT> applySorts(AbstractQDSLJDBCDataSource<MT> dataSource) {

		super.applySorts(dataSource);
		for (QDSLSort sort : this.getSorts().values()) {

			if (!SortType.NONE.equals(sort.getSortType())) {
				dataSource.addOrderExpression(sort.createExpression());
			}

		}
		return dataSource;

	}

}
