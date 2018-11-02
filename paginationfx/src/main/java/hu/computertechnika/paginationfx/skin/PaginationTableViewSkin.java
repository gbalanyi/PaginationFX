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
package hu.computertechnika.paginationfx.skin;

import hu.computertechnika.paginationfx.control.PaginationNavigator;
import hu.computertechnika.paginationfx.control.PaginationTableView;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * @author Gábor Balanyi
 */
public class PaginationTableViewSkin<UT> extends SkinBase<PaginationTableView<UT>> {

	private GridPane gridPane;

	public PaginationTableViewSkin(PaginationTableView<UT> control) {

		super(control);

		gridPane = new GridPane();
		gridPane.getStyleClass().add("grid-pane");

		// TODO Manage filter control

		// TableView
		this.initTableView(control.getTableView());
		this.getSkinnable().tableViewProperty().addListener((o, oldValue, newValue) -> {

			if (oldValue != null) {
				gridPane.getChildren().remove(oldValue);
			}
			if (newValue != null) {
				this.initTableView(newValue);
			}
			gridPane.requestLayout();

		});

		// PaginationNavigator
		if (!control.isHideNavigator()) {
			this.initPaginationNavigator(control.getPaginationNavigator());
		}
		this.getSkinnable().paginationNavigatorProperty().addListener((o, oldValue, newValue) -> {

			if (oldValue != null) {
				gridPane.getChildren().remove(oldValue);
			}
			if (newValue != null) {
				this.initPaginationNavigator(newValue);
			}
			gridPane.requestLayout();

		});
		this.getSkinnable().navigatorPositionProperty().addListener((o, oldValue, newValue) -> {

			if (!this.getSkinnable().isHideNavigator()) {

				// TODO Filter control...
				GridPane.setRowIndex(this.getSkinnable().getTableView(), this.getTableViewRowIndex());
				GridPane.setRowIndex(this.getSkinnable().getPaginationNavigator(), this.getPaginationNavigatorRow());

				gridPane.requestLayout();

			}

		});
		this.getSkinnable().hideNavigatorProperty().addListener((o, oldValue, newValue) -> {

			if (newValue) {
				this.getSkinnable().paginationNavigatorProperty().set(null);
			} else {
				this.getSkinnable().paginationNavigatorProperty().set(new PaginationNavigator());
			}

		});
		this.getSkinnable().dataProviderProperty().addListener((o, oldValue, newValue) -> {

			if (this.getSkinnable().getPaginationNavigator() != null) {
				this.getSkinnable().getPaginationNavigator().setDisable(newValue == null);
			}
			// TODO set filter component enabled

		});

		this.getChildren().add(gridPane);

	}

	private int getTableViewRowIndex() {

		int rowIndex = 0;
		// TODO Filter component
		if (!this.getSkinnable().isHideNavigator() && VPos.TOP.equals(this.getSkinnable().getNavigatorPosition())) {
			rowIndex++;
		}

		return rowIndex;

	}

	protected void initTableView(TableView<UT> tableView) {

		GridPane.setColumnIndex(tableView, 0);
		GridPane.setRowIndex(tableView, this.getTableViewRowIndex());
		GridPane.setHgrow(tableView, Priority.ALWAYS);
		GridPane.setVgrow(tableView, Priority.ALWAYS);

		gridPane.getChildren().add(tableView);

		tableView.setOnKeyPressed(e -> {
			if (KeyCode.HOME == e.getCode()) {
				this.getSkinnable().firstPage();
			} else if (KeyCode.PAGE_UP == e.getCode()) {
				this.getSkinnable().previousPage();
			} else if (KeyCode.PAGE_DOWN == e.getCode()) {
				this.getSkinnable().nextPage();
			} else if (KeyCode.END == e.getCode()) {
				this.getSkinnable().lastPage();
			}
		});

		// TODO add SwipeEvent handling

	}

	private int getPaginationNavigatorRow() {

		int rowIndex;
		if (VPos.TOP.equals(this.getSkinnable().getNavigatorPosition())) {
			rowIndex = 0;
		} else {

			// TODO Filter control...
			rowIndex = 1;

		}

		return rowIndex;

	}

	protected void initPaginationNavigator(PaginationNavigator paginationNavigator) {

		GridPane.setColumnIndex(paginationNavigator, 0);
		GridPane.setRowIndex(paginationNavigator, this.getPaginationNavigatorRow());
		GridPane.setFillWidth(paginationNavigator, false);
		GridPane.setHalignment(paginationNavigator, HPos.CENTER);

		gridPane.getChildren().add(paginationNavigator);

		paginationNavigator.setDisable(this.getSkinnable().getDataProvider() == null);

	}

}
