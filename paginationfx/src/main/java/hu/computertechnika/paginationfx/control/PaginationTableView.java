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
package hu.computertechnika.paginationfx.control;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import hu.computertechnika.paginationfx.PaginationSupport;
import hu.computertechnika.paginationfx.data.DataProvider;
import hu.computertechnika.paginationfx.filter.AbstractFilter;
import hu.computertechnika.paginationfx.skin.PaginationTableViewSkin;
import hu.computertechnika.paginationfx.sort.AbstractSort;
import hu.computertechnika.paginationfx.sort.SortSupport;
import hu.computertechnika.paginationfx.sort.SortType;
import javafx.application.Platform;
import javafx.beans.DefaultProperty;
import javafx.beans.binding.Bindings;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.VPos;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.SortEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;

/**
 * @author Gábor Balanyi
 */
@DefaultProperty("tableView")
public class PaginationTableView<UT> extends Control implements PaginationSupport, SortSupport<AbstractSort<?>> {

	private static final String DEFAULT_STYLE_CLASS = "pagination-table-view";

	private ChangeListener<? super Number> updatePageSizeListener = (ob, oldVal, newVal) -> this.updatePageSize();

	public PaginationTableView() {

		this.getStyleClass().add(DEFAULT_STYLE_CLASS);

		this.initAutoPageSize(this.getTableView());
		autoPageSizeProperty().addListener((o, oldValue, newValue) -> {

			if (newValue) {
				this.initAutoPageSize(this.getTableView());
			} else {

				this.getTableView().fixedCellSizeProperty().removeListener(updatePageSizeListener);
				this.getTableView().heightProperty().removeListener(updatePageSizeListener);
				this.getTableView().setFixedCellSize(Region.USE_COMPUTED_SIZE);
				pageSize.set(0);

			}

		});

		this.initTableView(this.getTableView());
		tableViewProperty().addListener((o, oldValue, newValue) -> {

			if (oldValue != null) {

				oldValue.getColumns().removeListener(columnListener);
				oldValue.getSortOrder().removeListener(sortOrderListener);
				if (this.isAutoPageSize()) {

					oldValue.fixedCellSizeProperty().removeListener(updatePageSizeListener);
					oldValue.heightProperty().removeListener(updatePageSizeListener);

				}

			}
			if (newValue != null) {

				this.initTableView(newValue);
				if (this.isAutoPageSize()) {
					this.initAutoPageSize(newValue);
				}

			}

		});

		this.initPaginationNavigator(this.getPaginationNavigator());
		paginationNavigatorProperty().addListener((o, oldValue, newValue) -> {

			if (oldValue != null) {

				Bindings.unbindBidirectional(this.currentPageIndexProperty(), oldValue.currentPageIndexProperty());
				Bindings.unbindBidirectional(this.pageNumberProperty(), oldValue.pageNumberProperty());

			}
			if (newValue != null) {
				this.initPaginationNavigator(newValue);
			}

		});

		@SuppressWarnings("unchecked")
		MapChangeListener<Object, AbstractSort<?>> mapListener = c -> {

			if (this.getTableView() != null) {

				AbstractSort<?> sort;
				if (c.wasAdded()) {
					sort = c.getValueAdded();
				} else {
					sort = c.getValueRemoved();
				}

				if (this.getTableView().getColumns().contains(c.getKey())) {
					this.sortChanged((TableColumn<UT, ?>) c.getKey(), sort);
				}

				if (!SortType.NONE.equals(sort.getSortType())) {
					this.loadPage();
				}

			}

		};
		sortsProperty().addListener(mapListener);

	}

	@Override
	protected Skin<?> createDefaultSkin() {
		return new PaginationTableViewSkin<>(this);
	}

	protected boolean sortChanged(TableColumn<UT, ?> tableColumn) {
		return this.sortChanged(tableColumn, this.sortsProperty().get(tableColumn));
	}

	protected boolean sortChanged(TableColumn<UT, ?> tableColumn, AbstractSort<?> sort) {

		boolean requireLoadPage = false;
		if (sort != null) {

			tableColumn.setSortable(true);
			if (SortType.ASCENDING.equals(sort.getSortType())) {

				tableColumn.setSortType(javafx.scene.control.TableColumn.SortType.ASCENDING);
				requireLoadPage = true;

			} else if (SortType.DESCENDING.equals(sort.getSortType())) {

				tableColumn.setSortType(javafx.scene.control.TableColumn.SortType.DESCENDING);
				requireLoadPage = true;

			} else {
				tableColumn.setSortType(null);
			}

		} else {

			tableColumn.setSortable(false);
			tableColumn.setSortType(null);

		}

		return requireLoadPage;

	}

	private String styleSheet;

	@Override
	public String getUserAgentStylesheet() {

		if (styleSheet == null) {
			styleSheet = this.getClass().getResource("paginationtableview.css").toExternalForm();
		}
		return styleSheet;

	}

	private ObjectProperty<Boolean> autoPageSize = new SimpleObjectProperty<>(this, "autoPageSize", true);

	protected void initAutoPageSize(TableView<UT> tableView) {

		if (tableView.getFixedCellSize() <= 0) {
			// TODO Get correct .table-cell {-fx-cell-size} CSS value
			tableView.setFixedCellSize(24D);
		}
		tableView.fixedCellSizeProperty().addListener(updatePageSizeListener);
		tableView.heightProperty().addListener(updatePageSizeListener);

	}

	public boolean isAutoPageSize() {
		return this.autoPageSizeProperty().get();
	}

	public void setAutoPageSize(boolean autoPageSize) {
		this.autoPageSizeProperty().set(autoPageSize);
	}

	public ObjectProperty<Boolean> autoPageSizeProperty() {
		return autoPageSize;
	}

	private ObjectProperty<Integer> pageSize;

	protected void updatePageSize() {

		int newPageSize = (int) (this.getTableView().heightProperty().doubleValue()
				/ this.getTableView().getFixedCellSize()) - 1;
		int newPageIndex;
		if (newPageSize < this.getPageSize()) {

			int firstVisibleIndex = this.getPageSize() * this.getCurrentPageIndex();
			newPageIndex = firstVisibleIndex / newPageSize;

		} else {

			int lastVisibleIndex = this.getPageSize() * (this.getCurrentPageIndex() + 1) - 1;
			newPageIndex = lastVisibleIndex / newPageSize;

		}

		this.setPageSize(newPageSize);
		if (newPageIndex != this.getCurrentPageIndex()) {
			this.setCurrentPageIndex(newPageIndex);
		}

	}

	public void setPageSize(int pageSize) {
		this.pageSizeProperty().set(pageSize);
	}

	public int getPageSize() {
		return this.pageSizeProperty().get();
	}

	public ObjectProperty<Integer> pageSizeProperty() {

		if (pageSize == null) {

			pageSize = new SimpleObjectProperty<>(this, "pageSize", 1);
			pageSize.addListener((o, oldValue, newValue) -> this.loadPage());

		}
		return pageSize;

	}

	public void loadPage() {

		if (this.getDataProvider() != null) {

			new Service<Void>() {

				@Override
				protected Task<Void> createTask() {
					return new Task<Void>() {

						@Override
						protected Void call() throws Exception {

							List<UT> page = getDataProvider().loadPage();
							Platform.runLater(() -> {
								getTableView().itemsProperty().get().clear();
								getTableView().itemsProperty().get().addAll(page);
							});
							return null;

						}

					};

				}

			}.start();

		}

	}

	private ObjectProperty<DataProvider<?, ?, UT, ? extends AbstractFilter<?, ?>, ? extends AbstractSort<?>>> dataProvider;

	public DataProvider<?, ?, UT, ? extends AbstractFilter<?, ?>, ? extends AbstractSort<?>> getDataProvider() {
		return this.dataProviderProperty().get();
	}

	public void setDataProvider(
			DataProvider<?, ?, UT, ? extends AbstractFilter<?, ?>, ? extends AbstractSort<?>> dataProvider) {
		this.dataProviderProperty().set(Objects.requireNonNull(dataProvider));
	}

	@SuppressWarnings("unchecked")
	public ObjectProperty<DataProvider<?, ?, UT, ? extends AbstractFilter<?, ?>, ? extends AbstractSort<?>>> dataProviderProperty() {

		if (dataProvider == null) {

			dataProvider = new SimpleObjectProperty<>(this, "dataProvider");
			dataProvider.addListener((o, oldValue, newValue) -> {

				if (this.getTableView() != null) {
					this.getTableView().itemsProperty().get().clear();
				}

				if (oldValue != null) {

					Bindings.unbindBidirectional(this.currentPageIndexProperty(), oldValue.currentPageIndexProperty());
					Bindings.unbindBidirectional(this.pageSizeProperty(), oldValue.pageSizeProperty());
					this.pageNumberProperty().unbind();
					Bindings.unbindBidirectional(this.sortsProperty(), oldValue.sortsProperty());

				}
				if (newValue != null) {

					Bindings.bindBidirectional(this.currentPageIndexProperty(), newValue.currentPageIndexProperty());
					Bindings.bindBidirectional(this.pageSizeProperty(), newValue.pageSizeProperty());
					this.pageNumberProperty().bind(newValue.pageNumberProperty());
					Bindings.bindBidirectional(this.sortsProperty(),
							(MapProperty<Object, AbstractSort<?>>) newValue.sortsProperty());

				}

			});
		}
		return dataProvider;

	}

	private ObjectProperty<TableView<UT>> tableView = new SimpleObjectProperty<>(this, "tableView", new TableView<>());

	@SuppressWarnings("unchecked")
	private ChangeListener<javafx.scene.control.TableColumn.SortType> sortListener = (o, oldValue, newValue) -> {

		AbstractSort<?> sort = this.getSorts()
				.get(((SimpleObjectProperty<javafx.scene.control.TableColumn.SortType>) o).getBean());
		if (sort != null) {

			boolean requireLoadPage = false;
			if (javafx.scene.control.TableColumn.SortType.ASCENDING.equals(newValue)) {

				sort.setSortType(SortType.ASCENDING);
				requireLoadPage = true;

			} else if (javafx.scene.control.TableColumn.SortType.DESCENDING.equals(newValue)) {

				sort.setSortType(SortType.DESCENDING);
				requireLoadPage = true;

			}

			if (requireLoadPage) {
				this.loadPage();
			}

		}

	};

	private ListChangeListener<TableColumn<UT, ?>> columnListener = c -> {

		boolean requireLoadPage = false;
		while (c.next()) {

			for (TableColumn<UT, ?> tableColumn : c.getAddedSubList()) {

				requireLoadPage &= this.sortChanged(tableColumn);
				tableColumn.sortTypeProperty().addListener(sortListener);

			}

			for (TableColumn<UT, ?> tableColumn : c.getRemoved()) {

				tableColumn.sortTypeProperty().removeListener(sortListener);
				AbstractSort<?> sort = this.getSorts().get(tableColumn);
				if (sort != null) {
					// Fire map list change event. The given event call loadPage() if necessary.
					this.getSorts().remove(tableColumn);
				}

			}

		}

		if (requireLoadPage) {
			this.loadPage();
		}

	};

	private ListChangeListener<TableColumn<UT, ?>> sortOrderListener = c -> {

		boolean requireLoadPage = false;
		while (c.next()) {

			for (TableColumn<UT, ?> tableColumn : c.getRemoved()) {

				tableColumn.setSortType(null);
				AbstractSort<?> sort = this.getSorts().get(tableColumn);
				if (sort != null) {

					sort.setSortType(SortType.NONE);
					requireLoadPage = true;

				}

			}

		}

		if (requireLoadPage) {
			this.loadPage();
		}

	};

	protected void initTableView(TableView<UT> tableView) {

		// Disable default TableView sort function
		tableView.addEventHandler(SortEvent.ANY, e -> e.consume());
		tableView.getColumns().addListener(columnListener);
		tableView.getSortOrder().addListener(sortOrderListener);
		boolean requireLoadPage = false;
		for (TableColumn<UT, ?> tableColumn : tableView.getColumns()) {

			requireLoadPage &= this.sortChanged(tableColumn);
			tableColumn.sortTypeProperty().addListener(sortListener);

		}

		if (requireLoadPage) {
			this.loadPage();
		}

	}

	public TableView<UT> getTableView() {
		return this.tableViewProperty().get();
	}

	public ObjectProperty<TableView<UT>> tableViewProperty() {
		return tableView;
	}

	private ObjectProperty<PaginationNavigator> paginationNavigator = new SimpleObjectProperty<>(this,
			"paginationNavigator", new PaginationNavigator());

	protected void initPaginationNavigator(PaginationNavigator paginationNavigator) {

		Bindings.bindBidirectional(this.currentPageIndexProperty(), paginationNavigator.currentPageIndexProperty());
		Bindings.bindBidirectional(this.pageNumberProperty(), paginationNavigator.pageNumberProperty());

	}

	public PaginationNavigator getPaginationNavigator() {
		return this.paginationNavigatorProperty().get();
	}

	public ObjectProperty<PaginationNavigator> paginationNavigatorProperty() {
		return paginationNavigator;
	}

	private ObjectProperty<VPos> navigatorPosition;

	public VPos getNavigatorPosition() {
		return navigatorPositionProperty().get();
	}

	public void setNavigatorPosition(VPos vpos) {
		this.navigatorPositionProperty().set(Objects.requireNonNull(vpos));
	}

	public ObjectProperty<VPos> navigatorPositionProperty() {

		if (navigatorPosition == null) {
			navigatorPosition = new SimpleObjectProperty<VPos>(this, "navigatorPosition", VPos.BOTTOM) {

				@Override
				protected void invalidated() {

					if (VPos.BASELINE.equals(this.get()) || VPos.CENTER.equals(this.get())) {
						this.set(VPos.BOTTOM);
					}

				}

			};

		}
		return navigatorPosition;

	}

	private ObjectProperty<Boolean> hideNavigator;

	public void setHideNavigator(boolean hideNavigator) {
		this.hideNavigatorProperty().set(hideNavigator);
	}

	public boolean isHideNavigator() {
		return hideNavigatorProperty().get();
	}

	public ObjectProperty<Boolean> hideNavigatorProperty() {

		if (hideNavigator == null) {
			hideNavigator = new SimpleObjectProperty<>(this, "hideNavigator", false);
		}
		return hideNavigator;

	}

	// TODO Add paginationFilter methods

	/*
	 * PaginationSupport methods
	 */

	private ObjectProperty<Integer> pageNumber;

	@Override
	public int getPageNumber() {
		return this.pageNumberProperty().get();
	}

	@Override
	public ObjectProperty<Integer> pageNumberProperty() {

		if (pageNumber == null) {
			pageNumber = new SimpleObjectProperty<>(this, "pageNumber", 0);
		}
		return pageNumber;

	}

	private ObjectProperty<Integer> currentPageIndex;

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

			currentPageIndex = new SimpleObjectProperty<>(this, "currentPageIndex", 0);
			currentPageIndex.addListener((o, oldValue, newValue) -> {

				if (newValue >= 0 && newValue < this.getPageNumber()) {
					Platform.runLater(() -> this.loadPage());
				}

			});

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

	/*
	 * SortSupport methods
	 */

	private MapProperty<Object, AbstractSort<?>> sorts;

	@Override
	public void addSort(Object key, AbstractSort<?> sort) {
		this.sortsProperty().put(key, sort);
	}

	@Override
	public AbstractSort<?> removeSort(Object key) {
		return this.sortsProperty().remove(key);
	}

	@Override
	public Map<Object, AbstractSort<?>> getSorts() {
		return FXCollections.unmodifiableObservableMap(this.sortsProperty());
	}

	@Override
	public MapProperty<Object, AbstractSort<?>> sortsProperty() {

		if (sorts == null) {
			sorts = new SimpleMapProperty<>(this, "sorts", FXCollections.observableMap(new LinkedHashMap<>()));
		}
		return sorts;

	}

}
