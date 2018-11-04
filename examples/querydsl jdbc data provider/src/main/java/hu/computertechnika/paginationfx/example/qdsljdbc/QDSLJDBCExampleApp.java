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
package hu.computertechnika.paginationfx.example.qdsljdbc;

import java.sql.Date;

import com.querydsl.core.Tuple;

import hu.computertechnika.paginationfx.control.PaginationTableView;
import hu.computertechnika.paginationfx.data.QDSLJDBCDataProvider;
import hu.computertechnika.paginationfx.sql.QDSLJDBCDataSource;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author Gábor Balanyi
 */
public class QDSLJDBCExampleApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		PaginationTableView<Tuple> paginationTableView = this.createPaginationTableView();
		QDSLJDBCDataProvider<Tuple, Tuple> dataProvider = this.createDataProvider();
		paginationTableView.setDataProvider(dataProvider);

		BorderPane borderPane = new BorderPane(paginationTableView);

		Scene scene = new Scene(borderPane, 800, 480);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	protected QDSLJDBCDataProvider<Tuple, Tuple> createDataProvider() {

		// Create an in memory SQL data base with one person table.
		MemoryDataBase.create();

		QDSLJDBCDataSource<Tuple> dataSource = new QDSLJDBCDataSource<>(MemoryDataBase.getConnection());
		// Get all columns (ID, first_name, last_name, age, gender, dob)
		dataSource.addSelectExpression(QPerson.person.id);
		dataSource.addSelectExpression(QPerson.person.firstName);
		dataSource.addSelectExpression(QPerson.person.lastName);
		dataSource.addSelectExpression(QPerson.person.age);
		dataSource.addSelectExpression(QPerson.person.gender);
		dataSource.addSelectExpression(QPerson.person.dob);
		// Get data from the person table
		dataSource.setFromExpression(QPerson.person);
		QDSLJDBCDataProvider<Tuple, Tuple> dataProvider = new QDSLJDBCDataProvider<>(dataSource);

		return dataProvider;

	}

	protected PaginationTableView<Tuple> createPaginationTableView() {

		PaginationTableView<Tuple> paginationTableView = new PaginationTableView<>();
		TableView<Tuple> tableView = paginationTableView.getTableView();

		TableColumn<Tuple, Long> idColumn = new TableColumn<>("ID");
		idColumn.setCellValueFactory(feature -> new SimpleObjectProperty<>(feature.getValue().get(QPerson.person.id)));
		tableView.getColumns().add(idColumn);

		TableColumn<Tuple, String> firstName = new TableColumn<>("First name");
		firstName.setPrefWidth(100);
		firstName.setCellValueFactory(
				feature -> new SimpleObjectProperty<>(feature.getValue().get(QPerson.person.firstName)));
		tableView.getColumns().add(firstName);

		TableColumn<Tuple, String> lastName = new TableColumn<>("Last name");
		lastName.setPrefWidth(100);
		lastName.setCellValueFactory(
				feature -> new SimpleObjectProperty<>(feature.getValue().get(QPerson.person.lastName)));
		tableView.getColumns().add(lastName);

		TableColumn<Tuple, Integer> age = new TableColumn<>("Age");
		age.setCellValueFactory(feature -> new SimpleObjectProperty<>(feature.getValue().get(QPerson.person.age)));
		tableView.getColumns().add(age);

		TableColumn<Tuple, String> gender = new TableColumn<>("Gender");
		gender.setCellValueFactory(
				feature -> new SimpleObjectProperty<>(feature.getValue().get(QPerson.person.gender)));
		tableView.getColumns().add(gender);

		TableColumn<Tuple, Date> dateOfBirth = new TableColumn<>("Date of birth");
		dateOfBirth.setPrefWidth(100);
		dateOfBirth
				.setCellValueFactory(feature -> new SimpleObjectProperty<>(feature.getValue().get(QPerson.person.dob)));
		tableView.getColumns().add(dateOfBirth);

		return paginationTableView;

	}

	public static void main(String[] args) {
		launch(args);
	}

}
