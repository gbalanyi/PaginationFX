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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Gábor Balanyi
 */
public abstract class AbstractComparableJDBCFilter<T extends Comparable<?>> extends AbstractJDBCFilter<T> {

	public AbstractComparableJDBCFilter(String column) {
		super(column);
	}

	@Override
	public FilterType[] getSupportedFilterTypes() {

		List<FilterType> supportedFilters = new ArrayList<FilterType>(Arrays.asList(super.getSupportedFilterTypes()));
		supportedFilters.addAll(Arrays.asList(new FilterType[] { FilterType.BETWEEN, FilterType.GREATER_THAN,
				FilterType.GREATER_THAN_OR_EQUAL, FilterType.IN, FilterType.LESS_THAN, FilterType.LESS_THAN_OR_EQUAL,
				FilterType.NOT_BETWEEN, FilterType.NOT_IN }));
		return supportedFilters.toArray(new FilterType[] {});

	}

	private String createINPredicate(boolean not) {

		StringBuilder builder = new StringBuilder();
		builder.append(this.getColumn());
		if (not) {
			builder.append(" NOT");
		}
		builder.append(" IN (");
		for (int i = 0; i < this.getFilterValues().length; i++) {

			builder.append("?");
			if (i < (this.getFilterValues().length - 1)) {
				builder.append(", ");
			}

		}
		builder.append(")");
		return builder.toString();

	}

	@Override
	public String createPredicate() {

		if (FilterType.BETWEEN.equals(this.getFilterType())) {
			return this.getColumn() + " BETWEEN ? AND ?";
		}
		if (FilterType.GREATER_THAN.equals(this.getFilterType())) {
			return this.getColumn() + " > ?";
		}
		if (FilterType.GREATER_THAN_OR_EQUAL.equals(this.getFilterType())) {
			return this.getColumn() + " >= ?";
		}
		if (FilterType.IN.equals(this.getFilterType())) {
			return this.createINPredicate(false);
		}
		if (FilterType.LESS_THAN.equals(this.getFilterType())) {
			return this.getColumn() + " < ?";
		}
		if (FilterType.LESS_THAN_OR_EQUAL.equals(this.getFilterType())) {
			return this.getColumn() + " <= ?";
		}
		if (FilterType.NOT_BETWEEN.equals(this.getFilterType())) {
			return this.getColumn() + " NOT BETWEEN ? AND ?";
		}
		if (FilterType.NOT_IN.equals(this.getFilterType())) {
			return this.createINPredicate(true);
		}
		return super.createPredicate();

	}

}
