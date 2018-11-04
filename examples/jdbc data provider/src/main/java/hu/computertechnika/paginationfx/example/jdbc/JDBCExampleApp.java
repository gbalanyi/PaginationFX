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
package hu.computertechnika.paginationfx.example.jdbc;

import java.sql.Date;

import hu.computertechnika.paginationfx.control.PaginationTableView;
import hu.computertechnika.paginationfx.data.JDBCDataProvider;
import hu.computertechnika.paginationfx.sql.JDBCDataSource;
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
public class JDBCExampleApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		PaginationTableView<Object[]> paginationTableView = this.createPaginationTableView();
		JDBCDataProvider<Object[]> dataProvider = this.createDataProvider();
		paginationTableView.setDataProvider(dataProvider);
		
		BorderPane borderPane = new BorderPane(paginationTableView);

		Scene scene = new Scene(borderPane, 800, 480);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	protected JDBCDataProvider<Object[]> createDataProvider() {

		// Create an in memory SQL data base with one person table.
		MemoryDataBase.create();

		JDBCDataSource<Object[]> dataSource = new JDBCDataSource<>(MemoryDataBase.getConnection());
		// Get all columns (ID, first_name, last_name, age, gender, dob)
		dataSource.addSelectExpression("*");
		// Get data from the person table
		dataSource.setFromExpression("person");
		JDBCDataProvider<Object[]> dataProvider = new JDBCDataProvider<>(dataSource);

		return dataProvider;

	}

	protected PaginationTableView<Object[]> createPaginationTableView() {

		PaginationTableView<Object[]> paginationTableView = new PaginationTableView<>();
		TableView<Object[]> tableView = paginationTableView.getTableView();

		TableColumn<Object[], Long> idColumn = new TableColumn<>("ID");
		idColumn.setCellValueFactory(feature -> new SimpleObjectProperty<>((Long) feature.getValue()[0]));
		tableView.getColumns().add(idColumn);

		TableColumn<Object[], String> firstName = new TableColumn<>("First name");
		firstName.setPrefWidth(100);
		firstName.setCellValueFactory(feature -> new SimpleObjectProperty<>((String) feature.getValue()[1]));
		tableView.getColumns().add(firstName);

		TableColumn<Object[], String> lastName = new TableColumn<>("Last name");
		lastName.setPrefWidth(100);
		lastName.setCellValueFactory(feature -> new SimpleObjectProperty<>((String) feature.getValue()[2]));
		tableView.getColumns().add(lastName);

		TableColumn<Object[], Integer> age = new TableColumn<>("Age");
		age.setCellValueFactory(feature -> new SimpleObjectProperty<>((Integer) feature.getValue()[3]));
		tableView.getColumns().add(age);

		TableColumn<Object[], String> gender = new TableColumn<>("Gender");
		gender.setCellValueFactory(feature -> new SimpleObjectProperty<>((String) feature.getValue()[4]));
		tableView.getColumns().add(gender);

		TableColumn<Object[], Date> dateOfBirth = new TableColumn<>("Date of birth");
		dateOfBirth.setPrefWidth(100);
		dateOfBirth.setCellValueFactory(feature -> new SimpleObjectProperty<>((Date) feature.getValue()[5]));
		tableView.getColumns().add(dateOfBirth);

		return paginationTableView;

	}

	public static void main(String[] args) {
		launch(args);
	}

}
