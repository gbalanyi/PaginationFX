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

import java.util.function.Predicate;

/**
 * @author Gábor Balanyi
 */
public class ComparableFilter<T extends Comparable<T>> extends AbstractFilter<T, Predicate<T>> {

	private Predicate<T> between = v -> {
		boolean goe = false;
		boolean loe = false;
		if (v != null) {

			if ((this.getFilterValues()[0] != null && v.compareTo(this.getFilterValues()[0]) >= 0)
					|| this.getFilterValues()[0] == null) {
				goe = true;
			}
			if (this.getFilterValues().length >= 2 && this.getFilterValues()[1] != null
					&& v.compareTo(this.getFilterValues()[1]) <= 0) {
				loe = true;
			}

		} else {

			if (this.getFilterValues()[0] == null) {
				goe = true;
			}
			if (this.getFilterValues().length >= 2) {
				loe = true;
			}

		}
		return goe && loe;
	};

	private Predicate<T> equal = v -> {
		if (v != null) {
			if (this.getFilterValues()[0] != null) {
				return v.compareTo(this.getFilterValues()[0]) == 0;
			} else {
				return false;
			}
		} else if (this.getFilterValues()[0] != null) {
			return false;
		}
		return true;
	};

	private Predicate<T> greaterThan = v -> {
		if (v != null) {
			if (this.getFilterValues()[0] != null) {
				return v.compareTo(this.getFilterValues()[0]) > 0;
			} else {
				return true;
			}
		} else if (this.getFilterValues()[0] != null) {
			return false;
		}
		return false;
	};

	private Predicate<T> in = v -> {
		for (int i = 0; i < this.getFilterValues().length; i++) {
			if (v != null && this.getFilterValues()[i] != null) {

				if (v.compareTo(this.getFilterValues()[i]) == 0) {
					return true;
				}

			} else if (v == null && this.getFilterValues()[i] == null) {
				return true;
			}
		}
		return false;
	};

	@Override
	public FilterType[] getSupportedFilterTypes() {
		return new FilterType[] { FilterType.NONE, FilterType.BETWEEN, FilterType.EQUAL, FilterType.GREATER_THAN,
				FilterType.GREATER_THAN_OR_EQUAL, FilterType.IN, FilterType.LESS_THAN, FilterType.LESS_THAN_OR_EQUAL,
				FilterType.NOT_BETWEEN, FilterType.NOT_EQUAL, FilterType.NOT_IN };
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public Predicate<T> createPredicate() {

		if (this.getFilterValues() != null && this.getFilterValues().length > 0) {

			switch (this.getFilterType()) {

			case BETWEEN:
				return between;

			case EQUAL:
				return equal;

			case GREATER_THAN:
				return greaterThan;

			case GREATER_THAN_OR_EQUAL:
				return greaterThan.or(equal);

			case IN:
				return in;

			case LESS_THAN:
				return greaterThan.negate().and(equal.negate());

			case LESS_THAN_OR_EQUAL:
				return greaterThan.negate();

			case NOT_BETWEEN:
				return between.negate();

			case NOT_EQUAL:
				return equal.negate();

			case NOT_IN:
				return in.negate();

			}

		}

		return v -> true;

	}

}
