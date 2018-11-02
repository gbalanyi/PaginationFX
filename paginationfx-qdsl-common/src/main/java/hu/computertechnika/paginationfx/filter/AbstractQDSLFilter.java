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

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.SimpleExpression;

/**
 * @author Gábor Balanyi
 */
public abstract class AbstractQDSLFilter<T, C extends SimpleExpression<T>>
		extends AbstractSQLFilter<T, Predicate, C> {

	public AbstractQDSLFilter(C column) {
		super(column);
	}

	@Override
	public FilterType[] getSupportedFilterTypes() {
		return new FilterType[] { FilterType.NONE, FilterType.EQUAL, FilterType.NOT_EQUAL };
	}

	@Override
	public Predicate createPredicate() {

		if (this.getFilterValues() != null && this.getFilterValues().length > 0 && this.getFilterValues()[0] != null) {

			if (FilterType.EQUAL.equals(this.getFilterType())) {
				return this.getColumn().eq(this.getFilterValues()[0]);
			}
			if (FilterType.NOT_EQUAL.equals(this.getFilterType())) {
				return this.getColumn().ne(this.getFilterValues()[0]);
			}

		} else {

			if (FilterType.EQUAL.equals(this.getFilterType())) {
				return this.getColumn().isNull();
			}
			if (FilterType.NOT_EQUAL.equals(this.getFilterType())) {
				return this.getColumn().isNotNull();
			}

		}

		return null;

	}

}
