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

import java.text.SimpleDateFormat;

import com.querydsl.core.Tuple;

import hu.computertechnika.paginationfx.control.PaginationTableView;
import hu.computertechnika.paginationfx.data.QDSLJDBCDataProvider;
import hu.computertechnika.paginationfx.sort.QDSLSort;
import hu.computertechnika.paginationfx.sql.MemoryDataBase;
import hu.computertechnika.paginationfx.sql.QDSLJDBCDataSource;
import hu.computertechnika.paginationfx.sql.QPerson;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * @author Gábor Balanyi
 */
public class QDSLJDBCDataProviderController {

	@FXML
	private PaginationTableView<Tuple> paginationTableView;

	@SuppressWarnings("unchecked")
	@FXML
	public void initialize() {

		TableView<Tuple> tableView = paginationTableView.getTableView();
		for (TableColumn<Tuple, ?> tableColumn : tableView.getColumns()) {
			((TableColumn<Tuple, Object>) tableColumn).setCellValueFactory(feature -> {
				if ("0".equals(feature.getTableColumn().getId())) {
					return new SimpleObjectProperty<>(feature.getValue(), "id",
							feature.getValue().get(QPerson.person.id));
				}
				if ("1".equals(feature.getTableColumn().getId())) {
					return new SimpleObjectProperty<>(feature.getValue(), "firstName",
							feature.getValue().get(QPerson.person.firstName));
				}
				if ("2".equals(feature.getTableColumn().getId())) {
					return new SimpleObjectProperty<>(feature.getValue(), "lastName",
							feature.getValue().get(QPerson.person.lastName));
				}
				if ("3".equals(feature.getTableColumn().getId())) {
					return new SimpleObjectProperty<>(feature.getValue(), "Age",
							feature.getValue().get(QPerson.person.age));
				}
				if ("4".equals(feature.getTableColumn().getId())) {
					return new SimpleObjectProperty<>(feature.getValue(), "Gender",
							feature.getValue().get(QPerson.person.gender));
				}
				if ("5".equals(feature.getTableColumn().getId())) {
					return new SimpleObjectProperty<>(feature.getValue(), "DateOfBirth",
							new SimpleDateFormat("yyyy.MM.dd").format(feature.getValue().get(QPerson.person.dob)));
				}
				return null;
			});
		}

		MemoryDataBase.create();
		QDSLJDBCDataSource<Tuple> dataSource = new QDSLJDBCDataSource<>(MemoryDataBase.getConnection());
		dataSource.addSelectExpression(QPerson.person.id);
		dataSource.addSelectExpression(QPerson.person.firstName);
		dataSource.addSelectExpression(QPerson.person.lastName);
		dataSource.addSelectExpression(QPerson.person.age);
		dataSource.addSelectExpression(QPerson.person.gender);
		dataSource.addSelectExpression(QPerson.person.dob);
		dataSource.setFromExpression(QPerson.person);
		QDSLJDBCDataProvider<Tuple, Tuple> dataProvider = new QDSLJDBCDataProvider<>(dataSource);
		QDSLSort sort = new QDSLSort(QPerson.person.id);
		dataProvider.addSort(tableView.getColumns().get(0), sort);
		sort = new QDSLSort(QPerson.person.firstName);
		dataProvider.addSort(tableView.getColumns().get(1), sort);
		sort = new QDSLSort(QPerson.person.lastName);
		dataProvider.addSort(tableView.getColumns().get(2), sort);
		sort = new QDSLSort(QPerson.person.age);
		dataProvider.addSort(tableView.getColumns().get(3), sort);
		sort = new QDSLSort(QPerson.person.gender);
		dataProvider.addSort(tableView.getColumns().get(4), sort);
		sort = new QDSLSort(QPerson.person.dob);
		dataProvider.addSort(tableView.getColumns().get(5), sort);
		paginationTableView.setDataProvider(dataProvider);

	}

}
