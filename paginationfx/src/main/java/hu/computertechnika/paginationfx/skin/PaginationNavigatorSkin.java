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

import hu.computertechnika.paginationfx.PaginationSupport;
import hu.computertechnika.paginationfx.control.PaginationNavigator;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;

/**
 * @author Gábor Balanyi
 */
public class PaginationNavigatorSkin extends SkinBase<PaginationNavigator> {

	private HBox hbox;

	private Button firstPage;

	private Button previousPage;

	private Button nextPage;

	private Button lastPage;

	private TextField pageIndex;

	private Label pageNumber;

	public PaginationNavigatorSkin(PaginationNavigator control) {

		super(control);

		hbox = new HBox();
		hbox.getStyleClass().add("hbox");

		firstPage = new Button();
		firstPage.getStyleClass().add("first-page");
		firstPage.setOnAction(e -> control.firstPage());
		hbox.getChildren().add(firstPage);

		previousPage = new Button();
		previousPage.getStyleClass().add("previous-page");
		previousPage.setOnAction(event -> control.previousPage());
		hbox.getChildren().add(previousPage);

		pageIndex = new TextField();
		pageIndex.getStyleClass().add("page-index");
		pageIndex.setOnKeyPressed(e -> {

			if (KeyCode.ENTER == e.getCode()) {

				try {

					int newPagIntex = Integer.parseInt(pageIndex.getText().trim()) - 1;
					if (newPagIntex >= 0) {
						control.setCurrentPageIndex(newPagIntex);
					}

				} catch (NumberFormatException nfe) {
					// Ignore exception
				}

			}

		});
		pageIndex.focusedProperty().addListener((o, oldValue, newValue) -> {
			if (!newValue) {
				this.updatePageIndex(control.getCurrentPageIndex());
			}
		});
		control.currentPageIndexProperty().addListener((o, oldValue, newValue) -> {
			this.updatePageIndex(newValue);
		});
		this.updatePageIndex(control.getCurrentPageIndex());
		hbox.getChildren().add(pageIndex);

		pageNumber = new Label();
		pageNumber.getStyleClass().add("page-number");
		pageNumber.setMaxHeight(Double.MAX_VALUE);
		control.pageNumberProperty().addListener((ov, o, n) -> {
			if (Platform.isFxApplicationThread()) {
				updatePageNumber();
			} else {
				Platform.runLater(() -> updatePageNumber());
			}
		});
		this.updatePageNumber();
		hbox.getChildren().add(pageNumber);

		nextPage = new Button();
		nextPage.getStyleClass().add("next-page");
		nextPage.setOnAction(event -> control.nextPage());
		hbox.getChildren().add(nextPage);

		lastPage = new Button();
		lastPage.getStyleClass().add("last-page");
		lastPage.setOnAction(e -> control.lastPage());
		hbox.getChildren().add(lastPage);

		this.getChildren().add(hbox);

	}

	private void updatePageIndex(int newPageIndex) {

		if (this.getSkinnable().getPageNumber() > 0
				&& this.getSkinnable().getPageNumber() != PaginationSupport.INDETERMINATE) {
			newPageIndex++;
		}
		pageIndex.setText(Integer.toString(newPageIndex));

	}

	private void updatePageNumber() {

		this.updatePageIndex(this.getSkinnable().getCurrentPageIndex());
		if (this.getSkinnable().getPageNumber() != PaginationSupport.INDETERMINATE) {
			pageNumber.setText("/" + this.getSkinnable().getPageNumber());
		} else {
			pageNumber.setText("/...");
		}

	}

}
