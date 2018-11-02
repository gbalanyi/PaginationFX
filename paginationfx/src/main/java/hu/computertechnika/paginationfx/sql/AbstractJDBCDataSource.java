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
package hu.computertechnika.paginationfx.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.computertechnika.paginationfx.filter.AbstractJDBCFilter;

/**
 * @author Gábor Balanyi
 */
public abstract class AbstractJDBCDataSource<MT>
		extends AbstractSQLDataSource<String, String, AbstractJDBCFilter<?>, String, String, MT> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractJDBCDataSource.class);

	private void buildWhere(StringBuilder stringBuilder) {

		if (!this.getWhereExpressions().isEmpty()) {

			StringBuilder whereBuilder = new StringBuilder();
			for (int i = 0; i < this.getWhereExpressions().size(); i++) {

				String predicate = this.getWhereExpressions().get(i).createPredicate();
				if (predicate != null) {

					whereBuilder.append(predicate);
					if (i < (this.getWhereExpressions().size() - 1)) {
						whereBuilder.append(" AND ");
					}

				}

			}

			if (whereBuilder.length() > 0) {

				stringBuilder.append(" WHERE ");
				stringBuilder.append(whereBuilder.toString());

			}

		}

	}

	protected void buildLimit(StringBuilder stringBuilder) {

		stringBuilder.append(" { LIMIT " + this.getMaxResults());
		stringBuilder.append(" OFFSET " + this.getOffset());
		stringBuilder.append(" }");

	}

	@Override
	protected String buildQuery() {

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT ");
		for (int i = 0; i < this.getSelectExpressions().size(); i++) {

			stringBuilder.append(this.getSelectExpressions().get(i));
			if (i < (this.getSelectExpressions().size() - 1)) {
				stringBuilder.append(", ");
			}

		}
		stringBuilder.append(" FROM ");
		stringBuilder.append(this.getFromExpression());
		this.buildWhere(stringBuilder);
		if (!this.getOrderExpressions().isEmpty()) {

			stringBuilder.append(" ORDER BY ");
			for (int i = 0; i < this.getOrderExpressions().size(); i++) {

				stringBuilder.append(this.getOrderExpressions().get(i));
				if (i < (this.getOrderExpressions().size() - 1)) {
					stringBuilder.append(", ");
				}

			}

		}
		this.buildLimit(stringBuilder);
		stringBuilder.append(";");

		LOGGER.debug(stringBuilder.toString());
		return stringBuilder.toString();

	}

	@Override
	protected String buildCountQuery() {

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT ");
		stringBuilder.append("COUNT(*)");
		stringBuilder.append(" FROM ");
		stringBuilder.append(this.getFromExpression());
		this.buildWhere(stringBuilder);
		stringBuilder.append(";");

		LOGGER.debug(stringBuilder.toString());
		return stringBuilder.toString();

	}

}
