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
public class StringJDBCFilter extends AbstractComparableJDBCFilter<String> {

	public StringJDBCFilter(String column) {
		super(column);
	}

	@Override
	public FilterType[] getSupportedFilterTypes() {

		List<FilterType> supportedFilters = new ArrayList<FilterType>(Arrays.asList(super.getSupportedFilterTypes()));
		supportedFilters.addAll(Arrays.asList(new FilterType[] { FilterType.CONTAINS, FilterType.NOT_CONTAINS,
				FilterType.STARTS_WITH, FilterType.NOT_STARTS_WITH, FilterType.ENDS_WITH, FilterType.NOT_ENDS_WITH }));
		return supportedFilters.toArray(new FilterType[] {});

	}

	@Override
	public String createPredicate() {

		if (FilterType.CONTAINS.equals(this.getFilterType()) || FilterType.STARTS_WITH.equals(this.getFilterType())
				|| FilterType.ENDS_WITH.equals(this.getFilterType())) {
			return this.getColumn() + " LIKE ?";
		}
		if (FilterType.NOT_CONTAINS.equals(this.getFilterType())
				|| FilterType.NOT_STARTS_WITH.equals(this.getFilterType())
				|| FilterType.NOT_ENDS_WITH.equals(this.getFilterType())) {
			return this.getColumn() + " NOT LIKE ?";
		}
		return super.createPredicate();

	}

}
