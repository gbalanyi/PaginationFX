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
package hu.computertechnika.paginationfx.sort;

import java.util.Comparator;

/**
 * @author Gábor Balanyi
 */
public class ComparableSort<T> extends AbstractSort<Comparator<Comparable<T>>> {

	@SuppressWarnings("unchecked")
	private Comparator<Comparable<T>> comparator = (o1, o2) -> {
		if (o1 != null) {
			if (o2 != null) {
				return o1.compareTo((T) o2);
			} else {
				return 1;
			}
		} else if (o2 != null) {
			return -1;
		}
		return 0;
	};

	@Override
	public Comparator<Comparable<T>> createExpression() {

		if (SortType.ASCENDING.equals(this.getSortType())) {
			return comparator;
		}
		if (SortType.DESCENDING.equals(this.getSortType())) {
			return comparator.reversed();
		}
		return (o1, o2) -> 0;

	}

}
