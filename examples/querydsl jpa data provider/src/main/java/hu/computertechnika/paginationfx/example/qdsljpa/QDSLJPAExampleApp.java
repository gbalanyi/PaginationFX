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
package hu.computertechnika.paginationfx.example.qdsljpa;

import java.text.DateFormat;

import hu.computertechnika.paginationfx.control.PaginationTableView;
import hu.computertechnika.paginationfx.data.QDSLJPADataProvider;
import hu.computertechnika.paginationfx.sql.QDSLJPADataSource;
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
public class QDSLJPAExampleApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		PaginationTableView<Person> paginationTableView = this.createPaginationTableView();
		QDSLJPADataProvider<Person, Person> dataProvider = this.createDataProvider();
		paginationTableView.setDataProvider(dataProvider);

		BorderPane borderPane = new BorderPane(paginationTableView);

		Scene scene = new Scene(borderPane, 800, 480);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	protected QDSLJPADataProvider<Person, Person> createDataProvider() {

		// Create an in memory SQL data base with one person table.
		MemoryDataBase.create();

		QDSLJPADataSource<Person> dataSource = new QDSLJPADataSource<>(MemoryDataBase.getEntityManager());
		// Get all columns (ID, first_name, last_name, age, gender, dob)
		dataSource.addSelectExpression(QPerson.person);
		// Get data from the person table
		dataSource.setFromExpression(QPerson.person);
		QDSLJPADataProvider<Person, Person> dataProvider = new QDSLJPADataProvider<>(dataSource);

		return dataProvider;

	}

	protected PaginationTableView<Person> createPaginationTableView() {

		PaginationTableView<Person> paginationTableView = new PaginationTableView<>();
		TableView<Person> tableView = paginationTableView.getTableView();

		TableColumn<Person, Long> idColumn = new TableColumn<>("ID");
		idColumn.setCellValueFactory(feature -> new SimpleObjectProperty<>(feature.getValue().getId()));
		tableView.getColumns().add(idColumn);

		TableColumn<Person, String> firstName = new TableColumn<>("First name");
		firstName.setPrefWidth(100);
		firstName.setCellValueFactory(feature -> new SimpleObjectProperty<>(feature.getValue().getFirstName()));
		tableView.getColumns().add(firstName);

		TableColumn<Person, String> lastName = new TableColumn<>("Last name");
		lastName.setPrefWidth(100);
		lastName.setCellValueFactory(feature -> new SimpleObjectProperty<>(feature.getValue().getLastName()));
		tableView.getColumns().add(lastName);

		TableColumn<Person, Integer> age = new TableColumn<>("Age");
		age.setCellValueFactory(feature -> new SimpleObjectProperty<>(feature.getValue().getAge()));
		tableView.getColumns().add(age);

		TableColumn<Person, Gender> gender = new TableColumn<>("Gender");
		gender.setCellValueFactory(feature -> new SimpleObjectProperty<>(feature.getValue().getGender()));
		tableView.getColumns().add(gender);

		TableColumn<Person, String> dateOfBirth = new TableColumn<>("Date of birth");
		dateOfBirth.setPrefWidth(100);
		dateOfBirth.setCellValueFactory(feature -> new SimpleObjectProperty<>(
				DateFormat.getDateInstance().format(feature.getValue().getDateOfBirth())));
		tableView.getColumns().add(dateOfBirth);

		return paginationTableView;

	}

	public static void main(String[] args) {
		launch(args);
	}

}
