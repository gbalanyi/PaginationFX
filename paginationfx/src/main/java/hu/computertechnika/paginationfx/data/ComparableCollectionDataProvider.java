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

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import hu.computertechnika.paginationfx.filter.ComparableFilter;
import hu.computertechnika.paginationfx.filter.FilterType;
import hu.computertechnika.paginationfx.sort.ComparableSort;
import hu.computertechnika.paginationfx.sort.SortType;

/**
 * @author Gábor Balanyi
 */
public class ComparableCollectionDataProvider<MT extends Comparable<MT>, UT>
		extends AbstractCollectionDataProvider<MT, UT, ComparableFilter<MT>, ComparableSort<MT>> {

	public ComparableCollectionDataProvider(Collection<MT> collection) {
		super(collection);
	}

	@Override
	public Collection<MT> applyFilters(Collection<MT> dataSource) {

		if (!this.getFilters().isEmpty()) {

			Predicate<MT> predicate = null;
			for (ComparableFilter<MT> filter : this.getFilters().values()) {

				if (!FilterType.NONE.equals(filter.getFilterType())) {

					if (predicate == null) {
						predicate = filter.createPredicate();
					} else {
						predicate = predicate.and(filter.createPredicate());
					}

				}

			}

			if (predicate != null) {

				// TODO Add Paralell stream support
				try (Stream<MT> stream = dataSource.stream()) {
					return stream.filter(predicate).collect(Collectors.toList());
				}

			}

		}

		return dataSource;

	}

	@Override
	public Collection<MT> applySorts(Collection<MT> dataSource) {

		if (this.getSorts().size() > 1) {
			throw new UnsupportedOperationException("This data provider does not support multi sort.");
		}

		if (!this.getSorts().isEmpty()) {

			ComparableSort<MT> sort = this.getSorts().values().iterator().next();
			if (!SortType.NONE.equals(sort.getSortType())) {

				// TODO Add Paralell stream support
				try (Stream<MT> stream = dataSource.stream()) {
					return stream.sorted(sort.createExpression()).collect(Collectors.toList());
				}

			}

		}

		return dataSource;

	}

}
