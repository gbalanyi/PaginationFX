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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import hu.computertechnika.paginationfx.filter.AbstractFilter;
import hu.computertechnika.paginationfx.sort.AbstractSort;
import javafx.application.Platform;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.util.Callback;

/**
 * @author Gábor Balanyi
 */
public abstract class AbstractDataProvider<DS, MT, UT, FT extends AbstractFilter<?, ?>, ST extends AbstractSort<?>>
		implements DataProvider<DS, MT, UT, FT, ST> {

	protected void runLater(Runnable runnable) {

		if (Platform.isFxApplicationThread()) {
			Platform.runLater(runnable);
		} else {
			runnable.run();
		}

	}

	/*
	 * PaginationSupport methods
	 */

	private ObjectProperty<Integer> currentPageIndex;

	public AbstractDataProvider(DS datasource) {
		this.dataSourceProperty().set(Objects.requireNonNull(datasource));
	}

	@Override
	public void setCurrentPageIndex(int currentPageIndex) {
		this.currentPageIndexProperty().set(currentPageIndex);
	}

	@Override
	public int getCurrentPageIndex() {
		return this.currentPageIndexProperty().get();
	}

	@Override
	public ObjectProperty<Integer> currentPageIndexProperty() {

		if (currentPageIndex == null) {
			currentPageIndex = new SimpleObjectProperty<Integer>(this, "currentPageIndex", 0) {

				@Override
				public void set(Integer newValue) {

					super.set(newValue);

					if (newValue == null || newValue < 0) {
						AbstractDataProvider.this.runLater(() -> super.set(0));
					} else if (newValue >= getPageNumber()) {
						AbstractDataProvider.this.runLater(() -> super.set(Math.max(getPageNumber() - 1, 0)));
					}

				}

			};

		}
		return currentPageIndex;

	}

	@Override
	public void nextPage() {
		this.setCurrentPageIndex(this.getCurrentPageIndex() + 1);
	}

	@Override
	public void jumpPage(int pageNumber) {
		this.setCurrentPageIndex(this.getCurrentPageIndex() + pageNumber);
	}

	@Override
	public void previousPage() {
		this.setCurrentPageIndex(this.getCurrentPageIndex() - 1);
	}

	@Override
	public void firstPage() {
		this.setCurrentPageIndex(0);
	}

	@Override
	public void lastPage() {
		this.setCurrentPageIndex(this.getPageNumber());
	}

	private ReadOnlyObjectWrapper<Integer> pageNumber = new ReadOnlyObjectWrapper<Integer>(this, "pageNumber", 0) {

		@Override
		public void set(Integer newValue) {

			super.set(newValue);

			if (newValue == null || newValue < 0) {

				newValue = 0;
				AbstractDataProvider.this.runLater(() -> super.set(0));

			}
			if (getCurrentPageIndex() >= newValue) {
				// Fire a change event. The correct value will be set in the currentPageIndex
				// property.
				AbstractDataProvider.this.runLater(() -> setCurrentPageIndex(getPageNumber()));
			}

		}

	};

	protected void calculatePageNumber(int rowNumber) {

		int newPageNumber;
		if (rowNumber == 0) {
			newPageNumber = 0;
		} else if (rowNumber <= this.getPageSize()) {
			newPageNumber = 1;
		} else {
			newPageNumber = (int) Math.ceil((double) rowNumber / (double) this.getPageSize());
		}

		this.pageNumber.set(newPageNumber);

	}

	@Override
	public int getPageNumber() {
		return this.pageNumberProperty().get();
	}

	@Override
	public ReadOnlyObjectProperty<Integer> pageNumberProperty() {
		return pageNumber.getReadOnlyProperty();
	}

	/*
	 * DataProvider methods
	 */

	private ObjectProperty<DS> dataSource;

	@Override
	public void setDataSource(DS dataSource) {
		this.dataSourceProperty().set(Objects.requireNonNull(dataSource));
	}

	@Override
	public DS getDataSource() {
		return dataSourceProperty().get();
	}

	@Override
	public ObjectProperty<DS> dataSourceProperty() {

		if (dataSource == null) {
			dataSource = new SimpleObjectProperty<>(this, "dataSource");
		}
		return dataSource;

	}

	private ObjectProperty<Integer> pageSize;

	@Override
	public void setPageSize(int pageSize) {
		this.pageSizeProperty().set(pageSize);
	}

	@Override
	public int getPageSize() {
		return this.pageSizeProperty().get();
	}

	@Override
	public ObjectProperty<Integer> pageSizeProperty() {

		if (pageSize == null) {
			pageSize = new SimpleObjectProperty<Integer>(this, "pageSize", 1) {

				@Override
				public void set(Integer newValue) {

					super.set(newValue);

					if (newValue == null || newValue < 1) {
						AbstractDataProvider.this.runLater(() -> super.set(1));
					}

				}

			};
		}
		return pageSize;

	}

	private ObjectProperty<Callback<MT, UT>> tableViewValueFactory;

	@Override
	public void setTableViewValueFactory(Callback<MT, UT> tableViewValueFactory) {
		this.tableViewValueFactoryProperty().set(tableViewValueFactory);
	}

	@Override
	public Callback<MT, UT> getTableViewValueFactory() {
		return this.tableViewValueFactoryProperty().get();
	}

	@Override
	public ObjectProperty<Callback<MT, UT>> tableViewValueFactoryProperty() {

		if (tableViewValueFactory == null) {
			tableViewValueFactory = new SimpleObjectProperty<>(this, "tableViewValueFactory");
		}
		return tableViewValueFactory;

	}

	/*
	 * FilterSupport methods
	 */

	private MapProperty<Object, FT> filters;

	@Override
	public void addFilter(Object key, FT filter) {
		this.filtersProperty().put(key, filter);
	}

	@Override
	public FT removeFilter(Object key) {
		return this.filtersProperty().remove(key);
	}

	@Override
	public Map<Object, FT> getFilters() {
		return FXCollections.unmodifiableObservableMap(this.filtersProperty());
	}

	@Override
	public MapProperty<Object, FT> filtersProperty() {

		if (filters == null) {
			filters = new SimpleMapProperty<>(this, "filters", FXCollections.observableHashMap());
		}
		return filters;

	}

	/*
	 * SortSupport methods
	 */

	private MapProperty<Object, ST> sorts;

	@Override
	public void addSort(Object key, ST sort) {
		this.sortsProperty().put(key, sort);
	}

	@Override
	public ST removeSort(Object key) {
		return this.sortsProperty().remove(key);
	}

	@Override
	public Map<Object, ST> getSorts() {
		return FXCollections.unmodifiableObservableMap(this.sortsProperty());
	}

	@Override
	public MapProperty<Object, ST> sortsProperty() {

		if (sorts == null) {
			sorts = new SimpleMapProperty<>(this, "sorts", FXCollections.observableMap(new LinkedHashMap<>()));
		}
		return sorts;

	}

}
