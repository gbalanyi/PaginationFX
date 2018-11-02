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

import hu.computertechnika.paginationfx.PaginationSupport;
import hu.computertechnika.paginationfx.skin.PaginationNavigatorSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 * @author Gábor Balanyi
 */
public class PaginationNavigator extends Control implements PaginationSupport {

	private static final String DEFAULT_STYLE_CLASS = "pagination-navigator";

	public PaginationNavigator() {
		this(INDETERMINATE, 0);
	}

	public PaginationNavigator(int pageNumber) {
		this(pageNumber, 0);
	}

	public PaginationNavigator(int pageNumber, int pageIndex) {

		this.getStyleClass().add(DEFAULT_STYLE_CLASS);
		this.pageNumberProperty().set(pageNumber);
		this.setCurrentPageIndex(pageIndex);

	}

	@Override
	protected Skin<?> createDefaultSkin() {
		return new PaginationNavigatorSkin(this);
	}

	private ObjectProperty<Integer> pageNumber;

	@Override
	public int getPageNumber() {
		return this.pageNumberProperty().get();
	}

	@Override
	public ObjectProperty<Integer> pageNumberProperty() {

		if (pageNumber == null) {
			pageNumber = new SimpleObjectProperty<>();
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

}
