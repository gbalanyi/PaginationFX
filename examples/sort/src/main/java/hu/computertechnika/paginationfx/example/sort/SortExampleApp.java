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
package hu.computertechnika.paginationfx.example.sort;

import java.sql.Date;

import hu.computertechnika.paginationfx.control.PaginationTableView;
import hu.computertechnika.paginationfx.data.JDBCDataProvider;
import hu.computertechnika.paginationfx.sort.JDBCSort;
import hu.computertechnika.paginationfx.sort.SortType;
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
public class SortExampleApp extends Application {

	private PaginationTableView<Object[]> paginationTableView;

	@Override
	public void start(Stage primaryStage) throws Exception {

		paginationTableView = this.createPaginationTableView();
		JDBCDataProvider<Object[]> dataProvider = this.createDataProvider();
		paginationTableView.setDataProvider(dataProvider);

		// Set initial ascending sort order on the last_name column
		// Click on the TableView Header to change sort
		paginationTableView.getSorts().get(paginationTableView.getTableView().getColumns().get(2))
				.setSortType(SortType.ASCENDING);
		paginationTableView.loadPage();

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

		// Define sorts. Use person table column names
		JDBCSort sort = new JDBCSort("ID");
		dataProvider.addSort(paginationTableView.getTableView().getColumns().get(0), sort);

		sort = new JDBCSort("first_name");
		dataProvider.addSort(paginationTableView.getTableView().getColumns().get(1), sort);

		sort = new JDBCSort("last_name");
		dataProvider.addSort(paginationTableView.getTableView().getColumns().get(2), sort);

		sort = new JDBCSort("age");
		dataProvider.addSort(paginationTableView.getTableView().getColumns().get(3), sort);

		sort = new JDBCSort("gender");
		dataProvider.addSort(paginationTableView.getTableView().getColumns().get(4), sort);

		sort = new JDBCSort("dob");
		dataProvider.addSort(paginationTableView.getTableView().getColumns().get(5), sort);

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
