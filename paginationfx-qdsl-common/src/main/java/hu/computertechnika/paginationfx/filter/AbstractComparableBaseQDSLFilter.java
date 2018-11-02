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

import com.querydsl.core.types.dsl.ComparableExpressionBase;

/**
 * @author Gábor Balanyi
 */
public abstract class AbstractComparableBaseQDSLFilter<T extends Comparable<?>, C extends ComparableExpressionBase<T>>
		extends AbstractQDSLFilter<T, C> {

	public AbstractComparableBaseQDSLFilter(C column) {
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

}
