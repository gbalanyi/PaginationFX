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

import hu.computertechnika.paginationfx.control.PaginationTableView;
import hu.computertechnika.paginationfx.data.QDSLJPADataProvider;
import hu.computertechnika.paginationfx.sort.QDSLSort;
import hu.computertechnika.paginationfx.sql.MemoryDataBase;
import hu.computertechnika.paginationfx.sql.Person;
import hu.computertechnika.paginationfx.sql.QDSLJPADataSource;
import hu.computertechnika.paginationfx.sql.QPerson;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * @author Gábor Balanyi
 */
public class QDSLJPADataProviderController {

	@FXML
	private PaginationTableView<Person> paginationTableView;

	@SuppressWarnings("unchecked")
	@FXML
	public void initialize() {

		TableView<Person> tableView = paginationTableView.getTableView();
		for (TableColumn<Person, ?> tableColumn : tableView.getColumns()) {
			((TableColumn<Person, Object>) tableColumn).setCellValueFactory(feature -> {
				if ("0".equals(feature.getTableColumn().getId())) {
					return new SimpleObjectProperty<>(feature.getValue(), "id", feature.getValue().getId());
				}
				if ("1".equals(feature.getTableColumn().getId())) {
					return new SimpleObjectProperty<>(feature.getValue(), "firstName",
							feature.getValue().getFirstName());
				}
				if ("2".equals(feature.getTableColumn().getId())) {
					return new SimpleObjectProperty<>(feature.getValue(), "lastName", feature.getValue().getLastName());
				}
				if ("3".equals(feature.getTableColumn().getId())) {
					return new SimpleObjectProperty<>(feature.getValue(), "Age", feature.getValue().getAge());
				}
				if ("4".equals(feature.getTableColumn().getId())) {
					return new SimpleObjectProperty<>(feature.getValue(), "Gender", feature.getValue().getGender());
				}
				if ("5".equals(feature.getTableColumn().getId())) {
					return new SimpleObjectProperty<>(feature.getValue(), "DateOfBirth",
							new SimpleDateFormat("yyyy.MM.dd").format(feature.getValue().getDateOfBirth()));
				}
				return null;
			});
		}

		MemoryDataBase.create();
		QDSLJPADataSource<Person> dataSource = new QDSLJPADataSource<>(MemoryDataBase.getEntityManager());
		dataSource.addSelectExpression(QPerson.person);
		dataSource.setFromExpression(QPerson.person);
		QDSLJPADataProvider<Person, Person> dataProvider = new QDSLJPADataProvider<>(dataSource);
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
		sort = new QDSLSort(QPerson.person.dateOfBirth);
		dataProvider.addSort(tableView.getColumns().get(5), sort);
		paginationTableView.setDataProvider(dataProvider);

	}

}
