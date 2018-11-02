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
package hu.computertechnika.paginationfx;

import hu.computertechnika.paginationfx.control.PaginationTableView;
import hu.computertechnika.paginationfx.data.JDBCDataProvider;
import hu.computertechnika.paginationfx.sort.JDBCSort;
import hu.computertechnika.paginationfx.sql.JDBCDataSource;
import hu.computertechnika.paginationfx.sql.MemoryDataBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * @author Gábor Balanyi
 */
public class JDBCDataProviderController {

	@FXML
	private PaginationTableView<Object[]> paginationTableView;

	@SuppressWarnings("unchecked")
	@FXML
	public void initialize() {

		TableView<Object[]> tableView = paginationTableView.getTableView();
		for (TableColumn<Object[], ?> tableColumn : tableView.getColumns()) {
			((TableColumn<Object[], Object>) tableColumn).setCellValueFactory(
					feature -> new SimpleObjectProperty<>(feature.getValue()[Integer.parseInt(tableColumn.getId())]));
		}

		MemoryDataBase.create();
		JDBCDataSource<Object[]> dataSource = new JDBCDataSource<>(MemoryDataBase.getConnection());
		dataSource.addSelectExpression("*");
		dataSource.setFromExpression("person");
		JDBCDataProvider<Object[]> dataProvider = new JDBCDataProvider<>(dataSource);
		JDBCSort sort = new JDBCSort("ID");
		dataProvider.addSort(tableView.getColumns().get(0), sort);
		sort = new JDBCSort("first_name");
		dataProvider.addSort(tableView.getColumns().get(1), sort);
		sort = new JDBCSort("last_name");
		dataProvider.addSort(tableView.getColumns().get(2), sort);
		sort = new JDBCSort("age");
		dataProvider.addSort(tableView.getColumns().get(3), sort);
		sort = new JDBCSort("gender");
		dataProvider.addSort(tableView.getColumns().get(4), sort);
		sort = new JDBCSort("dob");
		dataProvider.addSort(tableView.getColumns().get(5), sort);
		paginationTableView.setDataProvider(dataProvider);

	}

}
