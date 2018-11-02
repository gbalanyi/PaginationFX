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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import hu.computertechnika.paginationfx.filter.AbstractFilter;
import hu.computertechnika.paginationfx.sort.AbstractSort;

/**
 * @author Gábor Balanyi
 */
public abstract class AbstractCollectionDataProvider<MT, UT, FT extends AbstractFilter<?, ?>, ST extends AbstractSort<?>>
		extends AbstractDataProvider<Collection<MT>, MT, UT, FT, ST> {

	public AbstractCollectionDataProvider(Collection<MT> collection) {
		super(collection);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UT> loadPage() {

		Collection<MT> list = new ArrayList<MT>(this.getDataSource());
		list = this.applyFilters(list);
		list = this.applySorts(list);

		this.calculatePageNumber(list.size());

		if (!list.isEmpty()) {

			int fromIndex = this.getCurrentPageIndex() * this.getPageSize();
			int toIndex = Math.min(fromIndex + this.getPageSize(), list.size());
			list = ((List<MT>) list).subList(fromIndex, toIndex);

			if (this.getTableViewValueFactory() != null) {

				List<UT> result = new ArrayList<>();
				for (MT model : list) {
					result.add(this.getTableViewValueFactory().call(model));
				}
				return result;

			}

		}

		return (List<UT>) list;

	}

}
