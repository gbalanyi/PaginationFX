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
import com.querydsl.core.types.dsl.NumberExpression;

/**
 * @author Gábor Balanyi
 */
public class NumberQDSLFilter<T extends Number & Comparable<T>>
		extends AbstractComparableBaseQDSLFilter<T, NumberExpression<T>> {

	public NumberQDSLFilter(NumberExpression<T> column) {
		super(column);
	}

	@Override
	public Predicate createPredicate() {

		if (this.getFilterValues() != null && this.getFilterValues().length > 0) {

			if (FilterType.BETWEEN.equals(this.getFilterType()) && this.getFilterValues().length >= 2) {
				return this.getColumn().between(this.getFilterValues()[0], this.getFilterValues()[1]);
			}
			if (FilterType.GREATER_THAN.equals(this.getFilterType())) {
				return this.getColumn().gt(this.getFilterValues()[0]);
			}
			if (FilterType.GREATER_THAN_OR_EQUAL.equals(this.getFilterType())) {
				return this.getColumn().goe(this.getFilterValues()[0]);
			}
			if (FilterType.IN.equals(this.getFilterType())) {
				return this.getColumn().in(this.getFilterValues());
			}
			if (FilterType.LESS_THAN.equals(this.getFilterType())) {
				return this.getColumn().lt(this.getFilterValues()[0]);
			}
			if (FilterType.LESS_THAN_OR_EQUAL.equals(this.getFilterType())) {
				return this.getColumn().loe(this.getFilterValues()[0]);
			}
			if (FilterType.NOT_BETWEEN.equals(this.getFilterType()) && this.getFilterValues().length >= 2) {
				return this.getColumn().notBetween(this.getFilterValues()[0], this.getFilterValues()[1]);
			}
			if (FilterType.NOT_IN.equals(this.getFilterType())) {
				return this.getColumn().notIn(this.getFilterValues());
			}

		}

		return super.createPredicate();

	}

}
