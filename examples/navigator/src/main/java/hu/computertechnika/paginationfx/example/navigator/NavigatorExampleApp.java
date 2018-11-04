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
package hu.computertechnika.paginationfx.example.navigator;

import java.util.ArrayList;

import hu.computertechnika.paginationfx.control.PaginationTableView;
import hu.computertechnika.paginationfx.data.ComparableCollectionDataProvider;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/**
 * @author Gábor Balanyi
 */
public class NavigatorExampleApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		GridPane gridPane = new GridPane();
		gridPane.setHgap(5);

		PaginationTableView<String> paginationTableView = this.createPaginationTableView();
		GridPane.setVgrow(paginationTableView, Priority.ALWAYS);
		GridPane.setHgrow(paginationTableView, Priority.ALWAYS);
		gridPane.add(paginationTableView, 0, 0);

		HBox hBox = new HBox(5);
		GridPane.setMargin(hBox, new Insets(5, 5, 5, 5));
		gridPane.add(hBox, 0, 1);

		Label navigatorPositionLabel = new Label("Navigator position");
		navigatorPositionLabel.setMaxHeight(Double.MAX_VALUE);
		hBox.getChildren().add(navigatorPositionLabel);

		ComboBox<VPos> navigatorPositionComboBox = new ComboBox<>(
				FXCollections.observableArrayList(VPos.TOP, VPos.BOTTOM));
		navigatorPositionComboBox.getSelectionModel().select(VPos.BOTTOM);
		paginationTableView.navigatorPositionProperty()
				.bind(navigatorPositionComboBox.getSelectionModel().selectedItemProperty());
		hBox.getChildren().add(navigatorPositionComboBox);

		CheckBox hideNavigatorCheckBox = new CheckBox("Hide navigator");
		hideNavigatorCheckBox.setMaxHeight(Double.MAX_VALUE);
		hideNavigatorCheckBox.selectedProperty().bindBidirectional(paginationTableView.hideNavigatorProperty());
		hideNavigatorCheckBox.selectedProperty().addListener((o, oldValue, newValue) -> {
			if (newValue) {
				Alert alert = new Alert(AlertType.INFORMATION,
						"Use pageUp, pageDown, Home and End keys for pagination.", ButtonType.OK);
				alert.setHeaderText("");
				alert.setTitle("Info");
				alert.showAndWait();
				paginationTableView.getTableView().requestFocus();
			}
		});
		hBox.getChildren().add(hideNavigatorCheckBox);

		Scene scene = new Scene(gridPane, 800, 480);
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

		ArrayList<String> items = new ArrayList<>();
		for (int i = 0; i < 10000; i++) {
			items.add("Items " + i);
		}
		paginationTableView.setDataProvider(new ComparableCollectionDataProvider<>(items));

		return paginationTableView;

	}

	public static void main(String[] args) {
		launch(args);
	}

}
