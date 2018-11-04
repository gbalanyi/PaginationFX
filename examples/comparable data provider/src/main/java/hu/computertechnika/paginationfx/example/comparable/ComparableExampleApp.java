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
package hu.computertechnika.paginationfx.example.comparable;

import java.util.ArrayList;
import java.util.List;

import hu.computertechnika.paginationfx.control.PaginationTableView;
import hu.computertechnika.paginationfx.data.ComparableCollectionDataProvider;
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
public class ComparableExampleApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		List<String> items = new ArrayList<>();
		for (int i = 0; i < 10000; i++) {
			items.add("Items " + i);
		}
		ComparableCollectionDataProvider<String, String> dataProvider = new ComparableCollectionDataProvider<>(items);

		PaginationTableView<String> paginationTableView = this.createPaginationTableView();
		paginationTableView.setDataProvider(dataProvider);

		BorderPane borderPane = new BorderPane(paginationTableView);

		Scene scene = new Scene(borderPane, 800, 480);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	protected PaginationTableView<String> createPaginationTableView() {

		PaginationTableView<String> paginationTableView = new PaginationTableView<>();
		TableView<String> tableView = paginationTableView.getTableView();
		TableColumn<String, String> tableColumn = new TableColumn<>("Items");
		tableColumn.setPrefWidth(200);
		tableColumn.setCellValueFactory(feature -> new SimpleObjectProperty<>(feature.getValue()));
		tableView.getColumns().add(tableColumn);

		return paginationTableView;

	}

	public static void main(String[] args) {
		launch(args);
	}

}
