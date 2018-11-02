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

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * @author Gábor Balanyi
 */
public abstract class AbstractQDSLJPADataSource<MT>
		extends AbstractSQLDataSource<Expression<?>, EntityPath<?>, Predicate, OrderSpecifier<?>, JPAQuery<?>, MT> {

	private void checkState() {

		if (this.getSelectExpressions().isEmpty()) {
			throw new IllegalStateException("The select expression is null");
		}
		if (this.getFromExpression() == null) {
			throw new IllegalStateException("The from expression is null");
		}

	}

	@Override
	protected JPAQuery<MT> buildQuery() {

		this.checkState();

		JPAQuery<MT> query = new JPAQuery<>();
		if (this.getSelectExpressions().size() == 1) {
			query.select(this.getSelectExpressions().get(0));
		} else {
			query.select(this.getSelectExpressions().toArray(new Expression[0]));
		}
		query.from(this.getFromExpression());
		query.where(this.getWhereExpressions().toArray(new Predicate[] {}));
		query.orderBy(this.getOrderExpressions().toArray(new OrderSpecifier<?>[] {}));
		query.limit(this.getMaxResults());
		query.offset(this.getOffset());

		return query;

	}

	@Override
	protected JPAQuery<Long> buildCountQuery() {

		this.checkState();

		JPAQuery<Long> query = new JPAQuery<>();
		query.select(Expressions.asNumber(0).count());
		query.from(this.getFromExpression());
		query.where(this.getWhereExpressions().toArray(new Predicate[] {}));

		return query;

	}

}
