package application;

import java.io.FileNotFoundException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

public class MainController implements Initializable {

	@FXML
	private Button Generate;

	@FXML
	private TextField Rseed;

	@FXML
	private TextField Source;

	@FXML
	private AnchorPane pane;

	@FXML
	private TextField pathCost;

	@FXML
	private Button run;

	@FXML
	private Label showTown;

	@FXML
	private TextField size;

	@FXML
	private TextField target;

	@FXML
	private Label LLL;

	private String mode = "green";

	private ObservableList<Integer> pathCities = FXCollections.observableArrayList();
	private ObservableList<Integer> nodesList = FXCollections.observableArrayList();
	private ArrayList<City> Nodes = new ArrayList<>();
	private ArrayList<Label> LinesDest = new ArrayList<>();
	private ArrayList<Line> lines = new ArrayList<>();
	private ArrayList<Label> label = new ArrayList<>();

	private HashMap<Integer, Node> table = new HashMap<Integer, Node>();
	private String sor = "";
	Circle sss;
	Circle ttt;

	@FXML
	void Generate(ActionEvent event) {
		try {

			clear(new ActionEvent());
			int n = Integer.parseInt(size.getText());
			int s = Integer.parseInt(Rseed.getText());
			
		int c=(int)((pane.getWidth()*pane.getHeight())/50);
			
			if (n > c) {

				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("WARNING!");
				alert.setContentText("the input must be less than "+c);
				alert.show();
			} else if (n < 4) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("WARNING!");
				alert.setContentText("the input must be more than 3");
				alert.show();
			} else {
				getCities(s, n);
				initalizeMap();
				LLL.setText("Select node from 0 to " + String.valueOf((n - 1)) + "");
			}

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("WARNING!");
			alert.setContentText("You have to ReEnter Correct Numper");
			alert.show();
			e.printStackTrace();
		}
	}

	@FXML
	void run(ActionEvent event) {
		try {
			int t = Integer.parseInt(size.getText());

			if (Source.getText() != "" && target.getText() != "") {
				if (t < Integer.parseInt(Source.getText()) || t < Integer.parseInt(target.getText())
						|| 0 > Integer.parseInt(target.getText()) || 0 > Integer.parseInt(Source.getText())) {
					throw new Exception();
					/*
					 * ----> run "shortest path"
					 */
				}
				if (!getCity(Integer.parseInt(Source.getText())).equals(getCity(Integer.parseInt(target.getText())))) {
					for (int i = 0; i < pathCities.size(); i++) {
						getCity(pathCities.get(i)).getCircle().setFill(Color.web("#008CBA"));

					}
					if (!(sor == ""))
						getCity(Integer.parseInt(sor)).getCircle().setFill(Color.web("#008CBA"));
					sor = Source.getText();
					getShortestPath(getCity(Integer.parseInt(Source.getText())),
							getCity(Integer.parseInt(target.getText())));
					for (int i = 0; i < lines.size(); i++) {
						lines.get(i).setStrokeWidth(2);
						pane.getChildren().add(lines.get(i));
						pane.getChildren().add(LinesDest.get(i));

					}
				} else {

					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("heeeey!");
					alert.setContentText("You are already in the city");
					alert.show();
				}

			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("WARNING!");
				alert.setContentText("You have to choose two Nodes");
				alert.show();
			}
			Source.setText("");
			target.setText("");
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("WARNING!");
			alert.setContentText("You have to Enter a Number");
			alert.show();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@FXML
	void clear(ActionEvent event) {
		try {
			pathCost.setText("");
			Source.setText("");
			target.setText("");

			if (!(sor == ""))
				getCity(Integer.parseInt(sor)).getCircle().setFill(Color.web("#008CBA"));

			for (int i = 0; i < pathCities.size(); i++) {
				getCity(pathCities.get(i)).getCircle().setFill(Color.web("#008CBA"));

			}

			for (int i = 0; i < Nodes.size(); i++) {
				pane.getChildren().remove(Nodes.get(i).getCircle());
				pane.getChildren().remove(label.get(i));

			}
			for (int i = 0; i < lines.size(); i++) {
				pane.getChildren().remove(lines.get(i));

			}

//			for (int i = 0; i < label.size(); i++) {
//			pane.getChildren().remove(label.get(i));
//			}
			for (int i = 0; i < LinesDest.size(); i++) {
				pane.getChildren().remove(LinesDest.get(i));

			}
//		for (int i = 0; i < LinesDest.size(); i++) {
//			pane.getChildren().remove(label.get(i));
//			//System.out.println(Nodes.get(i).getName());
//		}

			label.clear();

			LinesDest.clear();
			Nodes.clear();

			// clear lines Array List
			pathCities.clear();
			lines.clear();
			sor = "";

			// pathCities.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * Reset the table
	 */

	/*
	 * fill the map with Nodes by putting each city on its right coordinates. each
	 * city with its name and a red circle that represents it
	 */
	public void initalizeMap() {
		try {

			for (int i = 0; i < Nodes.size(); i++) {
				// the circle that represents the city on the map
				Circle point = new Circle(5);

				// a label that hold the city name
				Label cityName = new Label(Nodes.get(i).getName() + "");

				final double MAX_FONT_SIZE = 8;
				cityName.setStyle("-fx-font-width: bold;");
				cityName.setFont(new Font(MAX_FONT_SIZE));
				// set circle coordinates

				point.setCenterY((Nodes.get(i).getCoordinateY()));
				point.setCenterX((Nodes.get(i).getCoordinateX()));

				// set label beside the circle
				cityName.setLayoutY((Nodes.get(i).getCoordinateY()));
				cityName.setLayoutX((Nodes.get(i).getCoordinateX()) + 3);
				// label.add(cityName);

				point.setFill(Color.web("#008CBA"));

				point.setStroke(Color.BLACK);
				point.setStrokeWidth(.3);

				Tooltip tooltip = new Tooltip(
						Nodes.get(i).toString().replaceAll("_", " ").replaceAll("Y", "X").replaceFirst("X", " Y"));
				tooltip.setAutoFix(true);
				Tooltip.install(point, tooltip);

				// setting city circle to the circle above
				Nodes.get(i).setCircle(point);
				label.add(i, cityName);
				// add the circle and the label to the scene
				pane.getChildren().addAll( Nodes.get(i).getCircle());
				String temp = Nodes.get(i).toString();

				/*
				 * Get city name and coordinates, and choose it in the choice box(if it is null)
				 * when clicking on the circle
				 */
				// String mod ="read";
				point.setOnMouseClicked(e -> {
					String cityInfo = temp.replaceAll("_", " ");

					showTown.setText(cityInfo);
					String[] sp = temp.split("[-]");

					if (mode.equals("green")) {
						Source.setText(sp[0].trim());
						point.setFill(Color.web("#1ACF26"));
						if (sss != null) {
							sss.setFill(Color.web("#008CBA"));

						}
						sss = point;

						mode = "red";
					} else if (mode.equals("red")) {
						target.setText(sp[0].trim());
						point.setFill(Color.web("#DB241A"));

						if (ttt != null) {
							ttt.setFill(Color.web("#008CBA"));
						}
						ttt = point;

						mode = "green";

					}

				});

//			point.setOnMouseClicked(e -> {
//                String[] sp = temp.split("[-]");
//                if (mode.equals("green")){
//                        Source.setText(sp[0].trim());
//                        mode = "red";
//                    }else if (mode.equals("red")) {
//                        target.setText(sp[0].trim());
//                        mode = "green";
//                }
//            });
//
//            point.setOnMouseEntered(e -> {
//                if (mode.equals("green")){
//                    point.setFill(Color.LIGHTGREEN);
//                } else if (mode.equals("red"))
//                    point.setFill(Color.RED);
//            });

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

//			/*
//			point.setOnMouseEntered(e -> {
//				point.setFill(Color.LIGHTGREEN);
//
//			});
//			point.setOnMouseExited(e -> {
//				point.setFill(Color.web("#008CBA"));
//			});
//		}
//	


	
	  /* Get shortest path between to Nodes
	 */
	private void getShortestPath(City sourceCity, City targetCity) {
		// reset the table to start new one
		try {

			buildTable(sourceCity, targetCity);

			clearl(new ActionEvent());

			// check if there is path
			if (table.get(targetCity.getName()).getDistance() != Double.POSITIVE_INFINITY
					&& table.get(targetCity.getName()).getDistance() != 0) {
				shortestPath(sourceCity, targetCity);
				DecimalFormat df = new DecimalFormat("###.#");
				Node t = table.get(targetCity.getName());
				pathCost.setText(df.format(t.getDistance()));
				sourceCity.getCircle().setFill(Color.web("#1ACF26"));
				targetCity.getCircle().setFill(Color.web("#DB241A"));
				/*
				 * Add all the Nodes that were found between the source and target Nodes
				 */DecimalFormat ds = new DecimalFormat("###.##");
				double path = getDistance(getCity(sourceCity.getName()).getCoordinateX(),
						getCity(sourceCity.getName()).getCoordinateY(),
						getCity(pathCities.get(pathCities.size() - 1)).getCoordinateX(),
						getCity(pathCities.get(pathCities.size() - 1)).getCoordinateY());
				Label l = new Label(ds.format(path) + "");
				l.setFont(new Font(18));
				l.setTextFill(Color.web("#FFA419"));
				// l.setText(sor)t
				l.setLayoutX((getCity(sourceCity.getName()).getCoordinateX()
						+ getCity(pathCities.get(pathCities.size() - 1)).getCoordinateX()) / 2);
				l.setLayoutY((getCity(sourceCity.getName()).getCoordinateY()
						+ getCity(pathCities.get(pathCities.size() - 1)).getCoordinateY()) / 2);
				LinesDest.add(l);
				// pane.getChildren().add(l);

				for (int i = pathCities.size() - 1; i >= 0; i--) {

					if (i == 0) {
					} else {
						path = getDistance(getCity(pathCities.get(i)).getCoordinateX(),
								getCity(pathCities.get(i)).getCoordinateY(),
								getCity(pathCities.get(i - 1)).getCoordinateX(),
								getCity(pathCities.get(i - 1)).getCoordinateY());

						l = new Label(ds.format(path) + "");
						l.setTextFill(Color.web("#FFA419"));
						l.setFont(new Font(18));

						// l.setText(sor)t
						l.setLayoutX((getCity(pathCities.get(i)).getCoordinateX()
								+ getCity(pathCities.get(i - 1)).getCoordinateX()) / 2);
						l.setLayoutY((getCity(pathCities.get(i)).getCoordinateY()
								+ getCity(pathCities.get(i - 1)).getCoordinateY()) / 2);
						LinesDest.add(l);

					}
				}
			} else {

				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Ther is no path!");
				alert.setContentText("Missing Edges!");
				alert.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * A recursive method to trace the path between two Nodes
	 */
	private void shortestPath(City sourceCity, City targetCity) {
		pathCities.add(targetCity.getName());

		Node t = table.get(targetCity.getName());

		if (t.getSourceCity() == null) {
			return;
		}
		if (t.getSourceCity() == sourceCity) {

			if (sourceCity != targetCity) {
				t.getSourceCity().getCircle().setFill(Color.web("#FFA419"));
				targetCity.getCircle().setFill(Color.web("#FFA419"));

				lines.add(new Line((t.getSourceCity().getCoordinateX()), (t.getSourceCity().getCoordinateY()),
						(targetCity.getCoordinateX()), (targetCity.getCoordinateY())));
			}
			return;
		}

		t.getSourceCity().getCircle().setFill(Color.web("#FFA419"));
		targetCity.getCircle().setFill(Color.web("#FFA419"));
		lines.add(new Line((t.getSourceCity().getCoordinateX()), (t.getSourceCity().getCoordinateY()),
				(targetCity.getCoordinateX()), (targetCity.getCoordinateY())));
		shortestPath(sourceCity, t.getSourceCity());
	}

	/*
	 * Build the hash table by applying the dijkstra algorithm
	 */
	private void buildTable(City source, City targetCity) {
		try {

			table.clear();
			for (City i : Nodes) {
				table.put(i.getName(), new Node(i, false, Double.POSITIVE_INFINITY, null));
			}

			TableCompare comp = new TableCompare();
			PriorityQueue<Node> q = new PriorityQueue<Node>(9, comp);

			Node node = table.get(source.getName());
			node.setDistance(0.0);
			node.setKnown(true);
			q.add(node);

			while (!q.isEmpty()) {

				Node temp = q.poll();

				temp.setKnown(true);

				if (temp.getCurrentCity() == targetCity) {
					break;
				}
				ArrayList<Adjacent> adj = temp.getCurrentCity().getAdjacent();

				for (Adjacent c : adj) {
					Node t = table.get(c.getCity().getName());
					if (t.isKnown()) {
						continue;
					}
					// && its distance >= the distance between it and the current source city + all
					// previous path distance
					// && are adjacent
					double newDis = c.getDistance() + temp.getDistance();
					if (newDis < t.getDistance()) {
						t.setSourceCity(temp.getCurrentCity());
						t.setDistance(newDis);
					}
					q.add(t);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public double getDistance(double x1, double y1, double x2, double y2) {
		return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
	}

	/*
	 * Get City coordinates by its name from the Nodes Array List
	 */
	private String getCityCoordinates(int cityNumper) {
		for (int i = 0; i < nodesList.size(); i++) {
			if (Nodes.get(i).getName() == cityNumper) {
				return Nodes.get(i).getCoordinateX() + "," + Nodes.get(i).getCoordinateY();
			}
		}
		return null;
	}

	/*
	 * Get the City by its name from the Nodes Array List
	 */
	private City getCity(int cityNumper) {

		for (int i = 0; i < Nodes.size(); i++) {
			if (Nodes.get(i).getName() == cityNumper) {
				return Nodes.get(i);
			}
		}
		return null;
	}

	/*
	 * Read Nodes file and store its content to the Nodes Array List
	 */
	private void getCities(int seed, int n) {
		double Width = pane.getWidth();
		double Height = pane.getHeight();
		double x;
		double y;
		Random rx = new Random(seed);
		Random ry = new Random(seed);
		y = ry.nextDouble(Height);
		try {

			for (int i = 0; i < n; i++) {
				x = rx.nextDouble() * (Width - 30) + 13;
				y = ry.nextDouble() * (Height - 28) + 15;
				Nodes.add(new City(x, y, i, new Circle()));

			}

			Random rand = new Random(seed);

			for (int i = 0; i < Nodes.size(); i++) {
				for (int j = 0; j < 3; j++) {
					int ss[] = { i };
					int r = getRandomWithExclusion(rand, 0, n - 1, ss);
					rand.nextInt(Nodes.size());
					City c = getCity(r);
					Adjacent s = new Adjacent(c, getDistance(c.getCoordinateX(), c.getCoordinateY(),
							Nodes.get(i).getCoordinateX(), Nodes.get(i).getCoordinateY()));

					Nodes.get(i).getAdjacent().add(s);

				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public int getRandomWithExclusion(Random rnd, int start, int end, int[] exclude) {
		int random = start + rnd.nextInt(end - start + 1 - exclude.length);
		for (int ex : exclude) {
			if (random < ex) {
				break;
			}
			random++;
		}
		return random;
	}

	/*
	 * Read Distances file and store its content to the Edges Array List
	 */

	/*
	 * Get each city and set all its adjacent Nodes
	 */

	/*
	 * Remove all the paths on the map
	 */
	@FXML

	void clearl(ActionEvent event) {
		try {

			pathCost.setText("");
			Source.setText("");
			target.setText("");
			sss.setFill(Color.web("#008CBA"));
			if (!(sor == ""))
				getCity(Integer.parseInt(sor)).getCircle().setFill(Color.web("#008CBA"));

			for (int i = 0; i < pathCities.size(); i++) {
				getCity(pathCities.get(i)).getCircle().setFill(Color.web("#008CBA"));

			}
			for (int i = 0; i < LinesDest.size(); i++) {
				pane.getChildren().remove(LinesDest.get(i));

			}

			for (int i = 0; i < pathCities.size(); i++) {
				getCity(pathCities.get(i)).getCircle().setFill(Color.web("#008CBA"));

			}

			for (int i = 0; i < lines.size(); i++) {
				pane.getChildren().remove(lines.get(i));

			}

//			for (int i = 0; i < label.size(); i++) {
//			pane.getChildren().remove(label.get(i));
//			}

//		for (int i = 0; i < LinesDest.size(); i++) {
//			pane.getChildren().remove(label.get(i));
//			//System.out.println(Nodes.get(i).getName());
//		}

			LinesDest.clear();
			// clear lines Array List
			pathCities.clear();
			lines.clear();
			sor = "";

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Worning!");
			alert.setContentText("Ther are no paths!");
			alert.show();
		}
	}

	/*
	 * Display mouse coordinates
	 */

}
