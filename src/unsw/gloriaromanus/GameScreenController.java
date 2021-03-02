package unsw.gloriaromanus;

import unsw.gloriaromanus.classes.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.GeoPackage;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol.HorizontalAlignment;
import com.esri.arcgisruntime.symbology.TextSymbol.VerticalAlignment;
import com.esri.arcgisruntime.data.Feature;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;
import org.json.JSONArray;
import org.json.JSONObject;

public class GameScreenController extends StackPane implements ControlledScreen {

  ScreensController myController;

  @Override
  public void setScreenParent(ScreensController screenParent) {
    myController = screenParent;

  }

  // main game view
  @FXML
  private MapView mapView;

  // goals
  @FXML
  private TextField and_goal;
  @FXML
  private TextField or_goal_1;
  @FXML
  private TextField or_goal_2;

  @FXML
  private TextField invading_province;
  @FXML
  private TextField opponent_province;

  // text boxes
  @FXML
  private TextArea output_terminal;
  @FXML
  private TextArea province1_info_output;
  @FXML
  private TextArea province2_info_output;

  @FXML
  private TextArea current_faction_info;

  @FXML
  private TextField current_round;

  @FXML
  private TextField current_playing_faction;

  @FXML
  private StackPane training_popup;

  @FXML

  private GridPane units_display;


  @FXML
  private Label error_message;

  @FXML
  private ToggleGroup options;

  @FXML
  private RadioButton elephant;
  @FXML
  private RadioButton roman_legionary;
  @FXML
  private RadioButton beserker;
  @FXML
  private RadioButton heavy_infantry;
  @FXML
  private RadioButton spearmen;
  @FXML
  private RadioButton missile_infantry;
  @FXML
  private RadioButton melee_cavalry;
  @FXML
  private RadioButton horse_archers;
  @FXML
  private RadioButton chariots;
  @FXML
  private RadioButton artillery;
  @FXML
  private RadioButton javelin_skirmisher;
  @FXML
  private RadioButton druid;
  @FXML
  private RadioButton melee_infantry;
  @FXML
  private RadioButton pikemen;

  @FXML
  private Button final_train;

  @FXML
  private StackPane tax_popup;

  @FXML
  private Label tax_text;

  @FXML
  private RadioButton low_tax;
  @FXML
  private RadioButton normal_tax;
  @FXML
  private RadioButton high_tax;
  @FXML
  private RadioButton veryhigh_tax;

  @FXML
  private Button final_tax;

  @FXML
  private StackPane move_popup;

  @FXML
  private ToggleGroup move_options;

  @FXML
  private Label move_text;

  @FXML
  private TextArea unit_info;

  @FXML
  private ChoiceBox unit_name;

  @FXML
  private Button final_move;

  @FXML
  private Label move_error;

  @FXML
  private Label save_game_message;

  @FXML
  private Label move_message;

  private ArcGISMap map;

  private Map<String, String> provinceToOwningFactionMap;

  private Map<String, Integer> provinceToNumberTroopsMap;

 

  private Feature currentlySelectedFirstProvince;
  private String currentlySelectedFirstFaction;
  private String currentlySelectedSecondFaction;
  private Feature currentlySelectedSecondProvince;

  private FeatureLayer featureLayer_provinces;
  // private String gameState;

  private Player currentPlayer;
  private Player player1;
  private Player player2;

  private Game game;
  private boolean trainingPopup = false;
  private boolean canTrain = false;
  private Province selectedProvince;
  private boolean taxPopup = false;
  private boolean movePopup = false;
  private Province secondProvince;

  public static String fileName = null;
  public static String folderName = null;
  public static boolean newGame = true;
  public static String newGameFaction1 = "Roman";
  public static String newGameFaction2 = "Gaul";

  private String firstFaction;
  private String secondFaction;

  private ArrayList<CheckBox> checkBoxes;

  @FXML
  private void initialize() throws JsonParseException, JsonMappingException, IOException {
    this.currentlySelectedFirstProvince = null;
    this.currentlySelectedSecondProvince = null;
    this.currentlySelectedFirstFaction = null;
    this.currentlySelectedSecondFaction = null;

    // creating a new game
    if (newGame) {
      //GloriaRomanusController.folderName = "testSaveGame3";
      String gameFolder = GameScreenController.folderName;
      System.out.println(GameScreenController.folderName);
      Path path = Paths.get("src/unsw/gloriaromanus/gamedata/" + folderName);
      this.firstFaction = newGameFaction1;
      this.secondFaction = newGameFaction2;

      training_popup.setVisible(trainingPopup);
      tax_popup.setVisible(taxPopup);
      move_popup.setVisible(movePopup);
      // hashmap mapping current province allocation
      HashMap<String, ArrayList<String>> province_allocation = generateNewProvince(gameFolder, newGameFaction1,
      newGameFaction2);

      this.provinceToOwningFactionMap = getProvinceToOwningFactionMap(gameFolder);

      HashMap<String, String> goals = createGoals(gameFolder);
      System.out.println(goals);

      // set goals
      this.game = new Game(folderName, newGameFaction1, newGameFaction2, province_allocation, goals);
      this.currentPlayer = this.game.getCurrentPlayer();

      // set players
      this.player1 = this.game.getPlayer1();
      this.player2 = this.game.getPlayer2();

      // create a save file
      createEmptySaveGameFile(GameScreenController.folderName);
      // create two factions
      // initialise a list of provinces
      provinceToNumberTroopsMap = new HashMap<String, Integer>();
      // initialise provinces

      // set current faction textbox
      this.current_playing_faction.setText(this.currentPlayer.getFaction().getFactionName());
      // display current faction info
      setCurrentFactionInfo();
      // set up goals
      this.setGoals(goals);


    } else {

      Game game = Game.loadGame(GameScreenController.folderName);

    }
    initializeProvinceLayers();
  }

  /**
   * Create a gridpane of units on right hand side
   */
  private void setUpUnitGrid() {

          Faction playerFaction = currentPlayer.getFaction();
          String provinceString = (String) currentlySelectedFirstProvince.getAttributes().get("name");
          ArrayList<Province> playerProvinces = playerFaction.getProvinces();
          Province province = null;
          // get selected units
          this.checkBoxes = new ArrayList<>();

          for (Province p : playerProvinces) {
            if (p.getProvinceName().equals(provinceString)) {
              province = p;
            }
          }
          for (int i = 1; i < province.getUnits().size() + 1; i++) {

              int index = i - 1;
              String unitName = province.getUnits().get(index).getUnitName();

              CheckBox checkBox = new CheckBox(unitName);
              Text numTroop = new Text(Integer.toString(province.getUnits().get(index).getNumTroops()));
              Text numMvPoints = new Text(Integer.toString(province.getUnits().get(index).getCurrentMovementPoint()));
              
              String attackCheck = "";

              if (province.getUnits().get(index).getAttacked()) {
                attackCheck = "*";
              }

              Text attacked = new Text(attackCheck);

              // use index to track

		          checkBox.setAlignment(Pos.CENTER);
		          //checkBox.selectedProperty().bindBidirectional(game.cellProperty(i, j));
              this.units_display.add(attacked, 0, i);
              this.units_display.add(checkBox, 1, i);
              this.units_display.add(numTroop, 2, i);
              this.units_display.add(numMvPoints, 3, i);
              // add to the list of checkboxes
              this.checkBoxes.add(checkBox);
          }

          System.out.println("Checkboxes are");
          for (int i = 0; i < this.checkBoxes.size(); i++){
            System.out.println(this.checkBoxes.get(i).getText() + "index = " + i);
          }
  }

  private void resetUnitGridPane() {
    this.units_display.getChildren().clear();
    this.units_display.setHgap(10);
    this.units_display.setPadding(new Insets(10,10,10,10));
    // add headings
    this.units_display.add(new Label("Unit"), 1, 0);
    this.units_display.add(new Label("#Troops"), 2, 0);
    this.units_display.add(new Label("#MvPoints"), 3, 0);  
  }

  /**
   * Set up goals
   * 
   * @param goals
   */
  private void setGoals(HashMap<String, String> goals) {
    String andGoal = goals.get("and");
    andGoal = andGoal.substring(0, 1).toUpperCase() + andGoal.substring(1);
    this.and_goal.setText(andGoal);

    String orGoal1 = goals.get("or1");
    orGoal1 = orGoal1.substring(0, 1).toUpperCase() + orGoal1.substring(1);
    this.or_goal_1.setText(orGoal1);

    String orGoal2 = goals.get("or2");
    orGoal2 = orGoal2.substring(0, 1).toUpperCase() + orGoal2.substring(1);
    this.or_goal_2.setText(orGoal2);
  }

  /**
   * Create an empty json file to save game data later, called when a new game is
   * carated
   */
  private static void createEmptySaveGameFile(String folderName) {
    System.out.println(folderName);
    File file = new File("src/unsw/gloriaromanus/gamedata/" + folderName + "/saved_data.json");
    try {
      file.createNewFile();
    } catch (IOException ioe) {
      System.out.println("Error while creating empty file: " + ioe);
    }
  }

  private void setCurrentFactionInfo() {
    // Faction name
    this.current_faction_info.appendText("Faction: " + this.currentPlayer.getPlayerFactionName() + "\n");
    // gold
    this.current_faction_info.appendText("Total Gold: " + this.currentPlayer.getFaction().getGold() + "\n");
    // total wealth
    this.current_faction_info.appendText("Total Wealth: " + this.currentPlayer.getFaction().getWealth() + "\n");
    // num provinces owned
    this.current_faction_info
        .appendText("Total #Provinces Owned: " + this.currentPlayer.getFaction().getProvinces().size());
  }

  @FXML
  public void clickedSaveGame(ActionEvent e) throws IOException {
    game.saveGame(folderName);
    this.save_game_message.setText("Game saved");
    System.out.println("Clicked save Game");
  }

  @FXML
  public void clickedExit(ActionEvent e) {
    System.out.println("Clicked exit");
    // this.terminate();
  }

  @FXML
  public void clickedEndTurnButton(ActionEvent e) {
    // end turn
    this.game.changeCurrentPlayer();
    this.currentPlayer = this.game.getCurrentPlayer();
    this.current_round.setText(Integer.toString(this.game.getCurrentRound()));
    this.current_playing_faction.setText(this.currentPlayer.getPlayerFactionName());
    this.province1_info_output.clear();
    this.province2_info_output.clear();

    this.resetUnitGridPane();
    resetSelections();
    resetTextArea();
    printMessageToTerminal("Turn ended");
    setCurrentFactionInfo();

  }

  @FXML
  public void clickedMoveButton(ActionEvent e) throws IOException {

    movePopup = !movePopup;
    move_popup.setVisible(movePopup);
    if (currentlySelectedFirstFaction.equals(currentlySelectedSecondFaction) && currentlySelectedFirstFaction != null) {
      String firstProvince = (String) currentlySelectedFirstProvince.getAttributes().get("name");
      String secondProvince = (String) currentlySelectedSecondProvince.getAttributes().get("name");
      Faction playerFaction = this.currentPlayer.getFaction();
      ArrayList<Province> playerProvinces = playerFaction.getProvinces();

      for (Province p : playerProvinces) {
        if (p.getProvinceName().equals(firstProvince)) {
          ArrayList<Unit> units = p.getUnits();
          this.selectedProvince = p;
          if (units.size() == 0) {
            move_text.setText("There are no troops in the province to move!");
            final_move.setVisible(false);
            move_message.setVisible(false);
            unit_name.setVisible(false);
            return;
          }
          for (Unit u : units) {
            unit_name.getItems().add(u.getUnitName());
          }
        }
        if (p.getProvinceName().equals(secondProvince)) {
          this.secondProvince = p;
        }
      }
      // unit_name.getItems().addAll("elephant", "roman-legionary", "beserker",
      // "heavy-infantry", "spearmen");
      this.resetUnitGridPane();
      resetSelections(); // reset selections in UI
      addAllPointGraphics(); // reset graphics
    } else {
      move_text.setText("Cannot move troops between the 2 provinces!");
      final_move.setVisible(false);
      move_message.setVisible(false);
      unit_name.setVisible(false);
    }
  }

  @FXML
  public void closeMove(ActionEvent e) {
    movePopup = !movePopup;
    move_popup.setVisible(movePopup);
    move_text.setText("");
    unit_name.getItems().clear();
    final_move.setVisible(true);
    move_message.setVisible(true);
    unit_name.setVisible(true);
  }

  @FXML
  public void callMove(ActionEvent e) throws IOException {
    String name = (String) unit_name.getSelectionModel().getSelectedItem();
    Faction playerFaction = this.currentPlayer.getFaction();
    if (name == null) {
      move_error.setText("Please select a unit type!");
    } else {

      Province from = this.selectedProvince;
      ArrayList<Unit> units = from.getUnits();
      for (Unit u : units) {
        if (u.getUnitName().equals(name)) {

          ArrayList<Province> playerProvinces = playerFaction.getProvinces();
          Province to = null;
          for (Province p : playerProvinces) {
            if (p.getProvinceName().equals(this.secondProvince.getProvinceName())) {
              to = p;
              break;
            }
          }
          ArrayList<Unit> unitss = new ArrayList<Unit>();
          unitss.add(u);
          Boolean res = playerFaction.moveUnits(unitss, from, to);
          if (!res) {
            move_error.setText("Unable to move successfully.");
          }

        }
      }
    }
  }

  @FXML
  public void clickedResetSelectionButton(ActionEvent e) throws IOException {
    // printMessageToTerminal("Resetted selection");
    resetSelections(); // reset selections in UI
    resetTextArea(); // reset tax areas for province information
    this.resetUnitGridPane();
    addAllPointGraphics(); // reset graphics
    setCurrentFactionInfo();
  }

  private void resetTextArea() {
    this.province1_info_output.clear();
    this.current_faction_info.clear();
    this.province2_info_output.clear();
  }



  private ArrayList<Integer> unitsFromCheckbox() {
    ArrayList<Integer> unitIndicies = new ArrayList<>();
    for (int i = 0; i < this.checkBoxes.size(); i++) {
      if (this.checkBoxes.get(i).isSelected()) {
        unitIndicies.add(i);
      }
    }
    return unitIndicies;
  }




  @FXML
  public void clickedInvadeButton(ActionEvent e) throws IOException {
    //System.out.print(currentlySelectedFirstFaction + currentlySelectedSecondFaction);
    
    
    if (!currentlySelectedFirstFaction.equalsIgnoreCase(currentlySelectedSecondFaction)) {


      if (currentlySelectedFirstProvince != null && currentlySelectedSecondProvince != null) {
        String humanProvince = (String) currentlySelectedFirstProvince.getAttributes().get("name");
        String enemyProvince = (String) currentlySelectedSecondProvince.getAttributes().get("name");

        // check if connected

        Graph map = this.game.getMap();
        int dist = map.getDistanceBetweenTwoProvinces(humanProvince, enemyProvince, this.currentPlayer.getFaction());
        Province attackingProvince = this.currentPlayer.getFaction().getProvinceFromString(humanProvince);

        Faction defendFaction = this.getOtherPlayer(this.currentPlayer);
        Province defendingProvince = defendFaction.getProvinceFromString(enemyProvince);
        
        // if there is a path
        if (dist != -1) {

          // check if there are units in the province

          if (attackingProvince.getUnits().size() == 0){
            printMessageToTerminal("No units in province");
            return;
          }

          // get the list of troops from checkboxes: a list of indeces
          ArrayList<Integer> selectedUnitsIndecies = this.unitsFromCheckbox();
          // from indicies get a list of units in an arraylist 


          ArrayList<Unit> selectedAttackingUnits = attackingProvince.unitsFromIndecies(selectedUnitsIndecies); // a new list instance of unit


          // launch an attack

          System.out.println("Selected Units are ");
          for(Unit u : selectedAttackingUnits) {
            System.out.println("\t" + u.getUnitName());
          }

          if (selectedAttackingUnits.size() == 0) {
            printMessageToTerminal("No free units to attack");
          }


          String battleResult = Faction.createArmyBattle(selectedAttackingUnits, attackingProvince, defendingProvince);

          System.out.println(battleResult);


          resetTextArea();
          resetSelections(); // reset selections in UI
          this.resetUnitGridPane();
          this.updateOwnership();
          //this.updateOwnership();

          addAllPointGraphics(); // reset graphics
        } else {
          printMessageToTerminal("Provinces not adjacent, cannot invade!");
        }
      }
    } else {
      printMessageToTerminal("Can't invade your own province");
    }

  }

  /**
   * run this initially to update province owner, change feature in each
   * FeatureLayer to be visible/invisible depending on owner. Can also update
   * graphics initially
   */
  private void initializeProvinceLayers() throws JsonParseException, JsonMappingException, IOException {
    Basemap myBasemap = Basemap.createImagery();
    // myBasemap.getReferenceLayers().remove(0);
    map = new ArcGISMap(myBasemap);
    mapView.setMap(map);
    // note - tried having different FeatureLayers for AI and human provinces to
    // allow different selection colors, but deprecated setSelectionColor method
    // does nothing
    // so forced to only have 1 selection color (unless construct graphics overlays
    // to give color highlighting)
    GeoPackage gpkg_provinces = new GeoPackage("src/unsw/gloriaromanus/provinces_right_hand_fixed.gpkg");
    gpkg_provinces.loadAsync();
    gpkg_provinces.addDoneLoadingListener(() -> {
      if (gpkg_provinces.getLoadStatus() == LoadStatus.LOADED) {
        // create province border feature
        featureLayer_provinces = createFeatureLayer(gpkg_provinces);
        map.getOperationalLayers().add(featureLayer_provinces);

      } else {
        System.out.println("load failure");
      }
    });

    addAllPointGraphics();
  }

  // update ownership to display on map
  private void updateOwnership() {
    HashMap<String, String> ownershipMap= new HashMap<>();
    // loop through all province of player 1
    ArrayList<Province> player1Provinces = this.player1.getFaction().getProvinces();
    ArrayList<Province> player2Provinces = this.player2.getFaction().getProvinces();
    String player1Faction = this.player1.getFaction().getFactionName();
    String player2Faction = this.player2.getFaction().getFactionName();
    for (Province p : player1Provinces) {
      ownershipMap.put(p.getProvinceName(), player1Faction);
    }
    for (Province p : player2Provinces) {
      ownershipMap.put(p.getProvinceName(), player2Faction);
    }
    this.provinceToOwningFactionMap = ownershipMap;
  }

  private void addAllPointGraphics() throws JsonParseException, JsonMappingException, IOException {
    mapView.getGraphicsOverlays().clear();

    InputStream inputStream = new FileInputStream(new File("src/unsw/gloriaromanus/provinces_label.geojson"));
    FeatureCollection fc = new ObjectMapper().readValue(inputStream, FeatureCollection.class);

    GraphicsOverlay graphicsOverlay = new GraphicsOverlay();

    for (org.geojson.Feature f : fc.getFeatures()) {
      if (f.getGeometry() instanceof org.geojson.Point) {
        org.geojson.Point p = (org.geojson.Point) f.getGeometry();
        LngLatAlt coor = p.getCoordinates();
        Point curPoint = new Point(coor.getLongitude(), coor.getLatitude(), SpatialReferences.getWgs84());
        PictureMarkerSymbol s = null;
        String province = (String) f.getProperty("name");

        // update ownership
        String faction = this.provinceToOwningFactionMap.get(province);

        TextSymbol t = new TextSymbol(10, faction + "\n" + province + "\n\n",
            0xFFFF0000, HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);

        switch (faction) {
          case "Gaul":
            // note can instantiate a PictureMarkerSymbol using the JavaFX Image class - so
            // could
            // construct it with custom-produced BufferedImages stored in Ram
            // http://jens-na.github.io/2013/11/06/java-how-to-concat-buffered-images/
            // then you could convert it to JavaFX image
            // https://stackoverflow.com/a/30970114

            // you can pass in a filename to create a PictureMarkerSymbol...
            s = new PictureMarkerSymbol(new Image((new File("images/Celtic_Druid.png")).toURI().toString()));
            break;
          case "Roman":
            // you can also pass in a javafx Image to create a PictureMarkerSymbol
            // (different to BufferedImage)
            s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/ArcherMan/Archer_Man_NB.png");
            break;
          case "Carthaginian":
            s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Camel/CamelArcher/CamelArcher_NB.png");
          break;
          case "Celtic Briton":
            s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Cannon/Cannon_NB.png");
          break;
          case "Spanish":
            s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Chariot/Chariot_NB.png");
          break;
          case "Numidian":
            s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Crossbowman/Crossbowman_NB.png");
          break;
          case "Egyptian":
            s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Druid/Celtic_Druid_NB.png");
          break;

          case "Seleucid Empire":
            s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Elephant/Elephant_Archers/Elephant_Archers_NB.png");
          break;
          case "Pontu":
            s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Flagbearer/FlagBearer_NB.png");
          break;
          case "Amenian":
            s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Hoplite/Hoplite_NB.png");
          break;

          case "Parthian":
            s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Horse/Horse_Heavy_Cavalry/Horse_Heavy_Cavalry_NB.png");
          break;
          case "Germanic":
            s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/NetFighter/NetFighter_NB.png");
          break;
          case "Greek City State":
            s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Pikeman/Pikeman_NB.png");
          break;
          case "Macedonian":
            s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Slingerman/Slinger_Man_NB.png");
          break;
          case "Tracian":
            s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Spearman/Spearman_NB.png");
          break;
          case "Dacian":
            s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Swordsman/Swordsman_NB.png");
          break;
        }
        t.setHaloColor(0xFFFFFFFF);
        t.setHaloWidth(2);
        Graphic gPic = new Graphic(curPoint, s);
        Graphic gText = new Graphic(curPoint, t);
        graphicsOverlay.getGraphics().add(gPic);
        graphicsOverlay.getGraphics().add(gText);
      } else {
        System.out.println("Non-point geo json object in file");
      }

    }

    inputStream.close();
    mapView.getGraphicsOverlays().add(graphicsOverlay);
  }

  private FeatureLayer createFeatureLayer(GeoPackage gpkg_provinces) {
    FeatureTable geoPackageTable_provinces = gpkg_provinces.getGeoPackageFeatureTables().get(0);

    // Make sure a feature table was found in the package
    if (geoPackageTable_provinces == null) {
      System.out.println("no geoPackageTable found");
      return null;
    }

    // Create a layer to show the feature table
    FeatureLayer flp = new FeatureLayer(geoPackageTable_provinces);

    // https://developers.arcgis.com/java/latest/guide/identify-features.htm
    // listen to the mouse clicked event on the map view
    mapView.setOnMouseClicked(e -> {
      // was the main button pressed?
      if (e.getButton() == MouseButton.PRIMARY) {
        // get the screen point where the user clicked or tapped
        Point2D screenPoint = new Point2D(e.getX(), e.getY());

        // specifying the layer to identify, where to identify, tolerance around point,
        // to return pop-ups only, and
        // maximum results
        // note - if select right on border, even with 0 tolerance, can select multiple
        // features - so have to check length of result when handling it
        final ListenableFuture<IdentifyLayerResult> identifyFuture = mapView.identifyLayerAsync(flp, screenPoint, 0,
            false, 25);

        // add a listener to the future
        identifyFuture.addDoneListener(() -> {
          try {
            // get the identify results from the future - returns when the operation is
            // complete
            IdentifyLayerResult identifyLayerResult = identifyFuture.get();
            // a reference to the feature layer can be used, for example, to select
            // identified features
            if (identifyLayerResult.getLayerContent() instanceof FeatureLayer) {
              FeatureLayer featureLayer = (FeatureLayer) identifyLayerResult.getLayerContent();
              // select all features that were identified
              List<Feature> features = identifyLayerResult.getElements().stream().map(f -> (Feature) f)
                  .collect(Collectors.toList());

              if (features.size() > 1) {
                printMessageToTerminal("Have more than 1 element - you might have clicked on boundary!");
              }
              // selecting a province
              else if (features.size() == 1) {
                // note maybe best to track whether selected...
                Feature f = features.get(0);
                String province = (String) f.getAttributes().get("name");

                if (this.currentPlayer.getPlayerFactionName().equals(firstFaction)) {
                  if (provinceToOwningFactionMap.get(province).equals(firstFaction)) {
                    // province owned by human
                    System.out.println("clicked " + province);

                    if (currentlySelectedFirstProvince != null && currentlySelectedSecondProvince == null) {
                      // SELECT A SECOND PROVINCE
                      // featureLayer.unselectFeature(currentlySelectedFirstProvince);
                      if (!f.equals(currentlySelectedFirstProvince)) {
                        currentlySelectedSecondProvince = f;
                        currentlySelectedSecondFaction = firstFaction;
                        opponent_province.setText(province);

                        // display second province info
                        this.displayProvince2Information();

                      }
                    } else if (currentlySelectedFirstProvince != null && currentlySelectedSecondProvince != null) {
                      // if both are selected
                      // unselect second province and select new one
                      featureLayer.unselectFeature(currentlySelectedSecondProvince);
                      currentlySelectedSecondProvince = f;
                      currentlySelectedSecondFaction = secondFaction;
                      opponent_province.setText(province);
                      this.displayProvince2Information();
                    } else {
                      currentlySelectedFirstProvince = f;
                      // set currently selected
                      currentlySelectedFirstFaction = firstFaction;
                      invading_province.setText(province);
                      this.setUpUnitGrid();
                      this.displayProvince1Information();
                    }
                  }
                  // if the selection isnt in the same faction
                  else {
                    if (currentlySelectedSecondProvince != null) {
                      featureLayer.unselectFeature(currentlySelectedSecondProvince);
                    }
                    // if (currentlySelectedFirstProvince != null) {
                    // featureLayer.unselectFeature(currentlySelectedFirstProvince);
                    // }
                    currentlySelectedSecondProvince = f;
                    currentlySelectedSecondFaction = secondFaction;
                    opponent_province.setText(province);
                    this.displayProvince2Information();
                  }
                } else {
                  // when second faction if attacking

                  if (provinceToOwningFactionMap.get(province).equals(secondFaction)) {
                    // province owned by human
                    System.out.println("in second faction clicked " + province);

                    if (currentlySelectedFirstProvince != null && currentlySelectedSecondProvince == null) {
                      // SELECT A SECOND PROVINCE
                      // featureLayer.unselectFeature(currentlySelectedFirstProvince);
                      if (!f.equals(currentlySelectedFirstProvince)) {
                        currentlySelectedSecondProvince = f;
                        currentlySelectedSecondFaction = firstFaction;
                        opponent_province.setText(province);
                        this.displayProvince2Information();
                      }
                    } else if (currentlySelectedFirstProvince != null && currentlySelectedSecondProvince != null) {
                      // if both are selected
                      // unselect second province and select new one
                      featureLayer.unselectFeature(currentlySelectedSecondProvince);
                      currentlySelectedSecondProvince = f;
                      currentlySelectedSecondFaction = secondFaction;
                      opponent_province.setText(province);
                      this.displayProvince2Information();
                    } else {
                      currentlySelectedFirstProvince = f;
                      // set currently selected
                      currentlySelectedFirstFaction = firstFaction;
                      invading_province.setText(province);
                      this.setUpUnitGrid();
                      this.displayProvince1Information();
                    }
                  }
                  // if the selection isnt in the same faction
                  else {
                    if (currentlySelectedSecondProvince != null) {
                      featureLayer.unselectFeature(currentlySelectedSecondProvince);
                    }
                    currentlySelectedSecondProvince = f;
                    currentlySelectedSecondFaction = secondFaction;
                    opponent_province.setText(province);
                    this.displayProvince2Information();
                  }
                }
                featureLayer.selectFeature(f);
              }
            }
          } catch (InterruptedException | ExecutionException ex) {
            // ... must deal with checked exceptions thrown from the async identify
            // operation
            System.out.println("InterruptedException occurred");
          }
        });
      }
    });
    return flp;
  }

  private Map<String, String> getProvinceToOwningFactionMap(String game_data_folder) throws IOException {
    String content = Files
        .readString(Paths.get("src/unsw/gloriaromanus/gamedata/" + game_data_folder + "/province_ownership.json"));
    JSONObject ownership = new JSONObject(content);
    Map<String, String> m = new HashMap<String, String>();

    for (String key : ownership.keySet()) {
      System.out.println(key);
      // key will be the faction name
      JSONArray ja = ownership.getJSONArray(key);
      // value is province name
      for (int i = 0; i < ja.length(); i++) {
        String value = ja.getString(i);
        m.put(value, key);
      }
    }
    return m;
  }

  public void displayProvince1Information() {

    this.province2_info_output.clear();

    Faction playerFaction = currentPlayer.getFaction();
    String provinceString = (String) currentlySelectedFirstProvince.getAttributes().get("name");
    ArrayList<Province> playerProvinces = playerFaction.getProvinces();

    for (Province p : playerProvinces) {
      if (p.getProvinceName().equals(provinceString)) {
        // display information
        // Province name
        this.province1_info_output.appendText("Province Name: " + p.getProvinceName() + "\n");
        // Faction name
        this.province1_info_output.appendText("Owner: " + p.getFaction().getFactionName() + "\n");
        // tax rate
        this.province1_info_output.appendText("Tax Rate: " + p.getTaxBehaviour().getTaxRate() * 100 + "%\n");
        // wealth rate
        this.province1_info_output.appendText("Wealth Rate: +" + p.getTaxBehaviour().getWealth() + " per turn\n");
        // wealth
        this.province1_info_output.appendText("Province Wealth: " + p.getWealth() + "\n");
        // Training slot avaliability
        ArrayList<TrainSlot> slots = p.getTrainingSlots();
        this.province1_info_output.appendText("Training Slot 1 Occupancy: " + slots.get(0).getOccupancy() + "\n");
        if (slots.get(0).getOccupancy()) {
          this.province1_info_output.appendText("\tTraining " + slots.get(0).getCurrentTraining().getUnitName() + "\n");
        }
        this.province1_info_output.appendText("Training Slot 2 Occupancy: " + slots.get(1).getOccupancy() + "\n");
        if (slots.get(1).getOccupancy()) {
          this.province1_info_output.appendText("\tTraining " + slots.get(1).getCurrentTraining().getUnitName() + "\n");
        }
        // Unit list:
        ArrayList<Unit> units = p.getUnits();

        if (units.size() != 0) {
          this.province1_info_output.appendText("Units + Quantity:\n");
          for (Unit u : units) {
            this.province1_info_output.appendText(u.getUnitName() + " : " + u.getNumTroops() + "\n");
          }
        } else {
          this.province1_info_output.appendText("No units in this province\n");
        }

      }

    }

  }

  private boolean findProvinceFromString(String provinceString, ArrayList<Province> provinces) {
    for (Province p : provinces) {
      if (p.getProvinceName().equals(provinceString)) {
        return true;
      }
    }
    return false;
  }

  public void displayProvince2Information() {
    this.province2_info_output.clear();

    Faction playerFaction = this.currentPlayer.getFaction();

    String provinceString = (String) currentlySelectedSecondProvince.getAttributes().get("name");
    // check if this string belongs to this player

    // two situation: second province owned by current player, 2. owned by other
    // player

    ArrayList<Province> playerProvinces = playerFaction.getProvinces();

    Boolean check = findProvinceFromString(provinceString, playerProvinces);
    // if the selected province isnt from the current player
    if (!check) {
      playerFaction = this.getOtherPlayer(this.currentPlayer);
      playerProvinces = playerFaction.getProvinces();
    }

    for (Province p : playerProvinces) {
      if (p.getProvinceName().equals(provinceString)) {
        // display information
        // Province name
        this.province2_info_output.appendText("Province Name: " + p.getProvinceName() + "\n");
        // Faction name
        this.province2_info_output.appendText("Owner: " + p.getFaction().getFactionName() + "\n");
        // tax rate
        this.province2_info_output.appendText("Tax Rate: " + p.getTaxBehaviour().getTaxRate() * 100 + "%\n");
        // wealth rate
        this.province2_info_output.appendText("Wealth Rate: +" + p.getTaxBehaviour().getWealth() + " per turn\n");
        // wealth
        this.province2_info_output.appendText("Province Wealth: " + p.getWealth() + "\n");
        // Training slot avaliability
        ArrayList<TrainSlot> slots = p.getTrainingSlots();
        this.province2_info_output.appendText("Training Slot 1 Occupancy: " + slots.get(0).getOccupancy() + "\n");
        if (slots.get(0).getOccupancy()) {
          this.province2_info_output.appendText("\tTraining " + slots.get(0).getCurrentTraining().getUnitName() + "\n");
        }
        this.province2_info_output.appendText("Training Slot 2 Occupancy: " + slots.get(1).getOccupancy() + "\n");
        if (slots.get(1).getOccupancy()) {
          this.province2_info_output.appendText("\tTraining " + slots.get(1).getCurrentTraining().getUnitName() + "\n");
        }
        // Unit list:
        ArrayList<Unit> units = p.getUnits();
        if (units.size() != 0) {
          this.province2_info_output.appendText("Units + Quantity:\n");
          for (Unit u : units) {
            this.province2_info_output.appendText(u.getUnitName() + " : " + u.getNumTroops() + "\n");
          }
        } else {
          this.province2_info_output.appendText("No units in this province\n");
        }
      }
    }
  }

  /**
   * Get the other player
   * 
   * @param currPlayer
   * @return
   */
  private Faction getOtherPlayer(Player currPlayer) {
    if (currPlayer.equals(this.player1)) {
      return this.player2.getFaction();
    }
    return this.player1.getFaction();
  }

  private ArrayList<String> getHumanProvincesList() throws IOException {
    // https://developers.arcgis.com/labs/java/query-a-feature-layer/
    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
    JSONObject ownership = new JSONObject(content);
    return ArrayUtil.convert(ownership.getJSONArray(firstFaction));
  }


  private void resetSelections() {
    if (currentlySelectedFirstProvince == null && currentlySelectedSecondProvince == null) {
      //printMessageToTerminal("Nothing to reset");
    } else if (currentlySelectedFirstProvince != null && currentlySelectedSecondProvince != null) {
      featureLayer_provinces
          .unselectFeatures(Arrays.asList(currentlySelectedSecondProvince, currentlySelectedFirstProvince));
      currentlySelectedSecondProvince = null;
      currentlySelectedFirstProvince = null;
      currentlySelectedFirstFaction = null;
      currentlySelectedSecondFaction = null;
      invading_province.setText("");
      opponent_province.setText("");
    } else if (currentlySelectedFirstProvince == null && currentlySelectedSecondProvince != null) {
      featureLayer_provinces.unselectFeature(currentlySelectedSecondProvince);
      currentlySelectedSecondProvince = null;
      currentlySelectedFirstProvince = null;
      currentlySelectedFirstFaction = null;
      currentlySelectedSecondFaction = null;
      invading_province.setText("");
      opponent_province.setText("");
    } else if (currentlySelectedFirstProvince != null && currentlySelectedSecondProvince == null) {
      featureLayer_provinces.unselectFeature(currentlySelectedFirstProvince);
      currentlySelectedSecondProvince = null;
      currentlySelectedFirstProvince = null;
      currentlySelectedFirstFaction = null;
      currentlySelectedSecondFaction = null;
      invading_province.setText("");
      opponent_province.setText("");
    } else {
      printMessageToTerminal("Make a selection before reseting");
    }
  }

  private void printMessageToTerminal(String message) {
    output_terminal.appendText(message + "\n");
  }

  /**
   * Stops and releases all resources used in application.
   */
  void terminate() {
    if (mapView != null) {
      mapView.dispose();
    }
  }

  @FXML
  private void goToNewGameView(ActionEvent event) {
    System.out.println(666);
  }

  /**
   * Randomly divid the map into two parts Store the arrangement in a json file
   * 
   * @param gameNameFolder
   * @param firstFaction
   * @param secondFaction
   * @return
   * @throws IOException
   */
  public static HashMap<String, ArrayList<String>> generateNewProvince(String gameNameFolder, String firstFaction,
      String secondFaction) throws IOException {
    // read provice_list.json
    // create a java arraylist
    // shuffle array
    // create a hashmap
    // create a directory for the game
    // output a json province list

    // String provinces =
    // Files.readString(Paths.get("src/unsw/gloriaromanus/province_list.json"));

    JSONArray ownership = new JSONArray(
        "[\r\n    \"Lugdunensis\",\r\n    \"Lusitania\",\r\n    \"Lycia et Pamphylia\",\r\n    \"Macedonia\",\r\n    \"Mauretania Caesariensis\",\r\n    \"Mauretania Tingitana\",\r\n    \"Moesia Inferior\",\r\n    \"Moesia Superior\",\r\n    \"Narbonensis\",\r\n    \"Noricum\",\r\n    \"Numidia\",\r\n    \"Pannonia Inferior\",\r\n    \"Pannonia Superior\",\r\n    \"Raetia\",\r\n    \"Sardinia et Corsica\",\r\n    \"Sicilia\",\r\n    \"Syria\",\r\n    \"Tarraconensis\",\r\n    \"Thracia\",\r\n    \"V\",\r\n    \"VI\",\r\n    \"VII\",\r\n    \"VIII\",\r\n    \"X\",\r\n    \"XI\",\r\n    \"Achaia\",\r\n    \"Aegyptus\",\r\n    \"Africa Proconsularis\",\r\n    \"Alpes Cottiae\",\r\n    \"Alpes Graiae et Poeninae\",\r\n    \"Alpes Maritimae\",\r\n    \"Aquitania\",\r\n    \"Arabia\",\r\n    \"Armenia Mesopotamia\",\r\n    \"Asia\",\r\n    \"Baetica\",\r\n    \"Belgica\",\r\n    \"Bithynia et Pontus\",\r\n    \"Britannia\",\r\n    \"Cilicia\",\r\n    \"Creta et Cyrene\",\r\n    \"Cyprus\",\r\n    \"Dacia\",\r\n    \"Dalmatia\",\r\n    \"Galatia et Cappadocia\",\r\n    \"Germania Inferior\",\r\n    \"Germania Superior\",\r\n    \"I\",\r\n    \"II\",\r\n    \"III\",\r\n    \"IV\",\r\n    \"IX\",\r\n    \"Iudaea\"\r\n  ]");

    ArrayList<String> province_list = ArrayUtil.convert(ownership);
    // System.out.println(province_list);
    // shuffle list
    Collections.shuffle(province_list);
    // System.out.println(province_list);

    // allocate first half to one player, second half to anothe rplayer
    int length = province_list.size();

    ArrayList<String> first_list = new ArrayList<>(province_list.subList(0, length / 2));
    ArrayList<String> second_list = new ArrayList<>(province_list.subList(length / 2, length));

    HashMap<String, ArrayList<String>> newAllocation = new HashMap<String, ArrayList<String>>();
    newAllocation.put(firstFaction, first_list);
    newAllocation.put(secondFaction, second_list);

    // error check for existing directory
    try {
      java.nio.file.Path path = Paths.get("src/unsw/gloriaromanus/gamedata/" + gameNameFolder);
      Files.createDirectories(path);
      System.out.println("Directory is created!");
    } catch (IOException e) {
      System.err.println("Failed to create directory!" + e.getMessage());
    }

    provinceHashMapToJson(gameNameFolder, newAllocation);
    return newAllocation;
  }

  /**
   * Takes in a province map and load it into a json file
   * 
   * @param gameFolder
   * @param allocation
   * @throws JsonGenerationException
   * @throws JsonMappingException
   * @throws IOException
   */
  public static void provinceHashMapToJson(String gameFolder, HashMap<String, ArrayList<String>> allocation)
      throws JsonGenerationException, JsonMappingException, IOException {
    ObjectMapper mapper = new ObjectMapper();
    
    System.out.println(allocation);
    try {
      // Writing to a file
      mapper.writeValue(new File("src/unsw/gloriaromanus/gamedata/" + gameFolder + "/province_ownership.json"),
          allocation);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Randomly create goals, pick one for the "AND" goal, and two for "OR" goals
   * 
   * @param gameFolder
   * @return HashMap<String, Object> - goalType : goal
   */
  public static HashMap<String, String> createGoals(String gameFolder) {
    ArrayList<String> goals = new ArrayList<>();
    goals.add("treasury");
    goals.add("wealth");
    goals.add("province");
    // shuffle goals
    Collections.shuffle(goals);
    // create a map
    HashMap<String, String> goalsMap = new HashMap<>();
    goalsMap.put("and", goals.get(0));
    goalsMap.put("or1", goals.get(1));
    goalsMap.put("or2", goals.get(2));
    // write as a goal file
    ObjectMapper mapper = new ObjectMapper();
    try {
      // Writing to a file
      mapper.writeValue(new File("src/unsw/gloriaromanus/gamedata/" + gameFolder + "/goals.json"), goalsMap);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return goalsMap;
  }

  @FXML
  public void clickedTrainButton(ActionEvent e) {
    trainingPopup = !trainingPopup;
    training_popup.setVisible(trainingPopup);
    this.error_message.setAlignment(Pos.CENTER);
    Faction playerFaction = this.currentPlayer.getFaction();
    // if nto your province
    if (this.currentlySelectedFirstProvince == null) {
      this.error_message.setText("Please select a faction that you own.");
      this.elephant.setVisible(false);
      this.roman_legionary.setVisible(false);
      this.beserker.setVisible(false);
      this.heavy_infantry.setVisible(false);
      this.spearmen.setVisible(false);
      this.missile_infantry.setVisible(false);
      this.spearmen.setVisible(false);
      this.missile_infantry.setVisible(false);
      this.melee_cavalry.setVisible(false);
      this.horse_archers.setVisible(false);
      this.chariots.setVisible(false);
      this.artillery.setVisible(false);
      this.javelin_skirmisher.setVisible(false);
      this.druid.setVisible(false);
      this.melee_infantry.setVisible(false);
      this.pikemen.setVisible(false);
      this.final_train.setVisible(false);
      return;
    }

    String humanProvince = (String) currentlySelectedFirstProvince.getAttributes().get("name");
    ArrayList<Province> playerProvinces = playerFaction.getProvinces();

    for (int i = 0; i < playerProvinces.size(); i++) {
      Province curr = playerProvinces.get(i);
      if (humanProvince.equals(curr.getProvinceName())) {
        this.selectedProvince = curr;
        if (curr.checkTrain() == false || playerFaction.getGold() < 5) {
          this.error_message.setText(
              "Currently cannot train in this province! Either you don't have money or there are not enough slots.");
          this.elephant.setVisible(false);
          this.roman_legionary.setVisible(false);
          this.beserker.setVisible(false);
          this.heavy_infantry.setVisible(false);
          this.spearmen.setVisible(false);
          this.missile_infantry.setVisible(false);
          this.spearmen.setVisible(false);
          this.missile_infantry.setVisible(false);
          this.melee_cavalry.setVisible(false);
          this.horse_archers.setVisible(false);
          this.chariots.setVisible(false);
          this.artillery.setVisible(false);
          this.javelin_skirmisher.setVisible(false);
          this.druid.setVisible(false);
          this.melee_infantry.setVisible(false);
          this.pikemen.setVisible(false);
          this.final_train.setVisible(false);
        } else {
          this.canTrain = true;
        }
      }
    }

  }

  @FXML
  public void closeTraining(ActionEvent e) {
    this.trainingPopup = false;
    this.training_popup.setVisible(trainingPopup);
    this.error_message.setText("");
    this.elephant.setVisible(true);
    this.roman_legionary.setVisible(true);
    this.beserker.setVisible(true);
    this.heavy_infantry.setVisible(true);
    this.spearmen.setVisible(true);
    this.missile_infantry.setVisible(true);
    this.spearmen.setVisible(true);
    this.missile_infantry.setVisible(true);
    this.melee_cavalry.setVisible(true);
    this.horse_archers.setVisible(true);
    this.chariots.setVisible(true);
    this.artillery.setVisible(true);
    this.javelin_skirmisher.setVisible(true);
    this.druid.setVisible(true);
    this.melee_infantry.setVisible(true);
    this.pikemen.setVisible(true);
    this.final_train.setVisible(true);
  }

  @FXML
  public void callTrain(ActionEvent e) {
    if (!this.canTrain) {
      this.error_message.setText("you can't train remember");
      return;
    }
    Province selected = this.selectedProvince;
    if (selected.checkTrain() == false) {
      this.error_message.setText("No more slots for training");
      return;
    }
    System.out.println("yeet i want to train");
    if (this.elephant.isSelected()) {
      selected.performTrain("elephant");
      this.error_message.setText("Elephant now in a training spot!");
    } else if (this.roman_legionary.isSelected()) {
      selected.performTrain("roman-legionary");
      this.error_message.setText("Roman legionary now in a training spot!");
    } else if (this.beserker.isSelected()) {
      selected.performTrain("beserker");
      this.error_message.setText("Beserker now in a training spot!");
    } else if (this.heavy_infantry.isSelected()) {
      selected.performTrain("heavy-infantry");
      this.error_message.setText("Heavy infantry now in a training spot!");
    } else if (this.spearmen.isSelected()) {
      selected.performTrain("spearmen");
      this.error_message.setText("Spearmen now in a training spot!");
    } else if (this.missile_infantry.isSelected()) {
      selected.performTrain("missile-infantry");
      this.error_message.setText("Missile infantry now in a training spot!");
    } else if (this.melee_cavalry.isSelected()) {
      selected.performTrain("melee-cavalry");
      this.error_message.setText("Melee cavalry now in a training spot!");
    } else if (this.horse_archers.isSelected()) {
      selected.performTrain("horse-archers");
      this.error_message.setText("Horse archers now in a training spot!");
    } else if (this.chariots.isSelected()) {
      selected.performTrain("chariots");
      this.error_message.setText("Chariots are now in a training spot!");
    } else if (this.artillery.isSelected()) {
      selected.performTrain("artillery");
      this.error_message.setText("Artillery are now in a training spot!");
    } else if (this.javelin_skirmisher.isSelected()) {
      selected.performTrain("javelin-skirmisher");
      this.error_message.setText("Javelin skirmishers are now in a training spot!");
    } else if (this.druid.isSelected()) {
      selected.performTrain("druid");
      this.error_message.setText("Druids are now in a training spot!");
    } else if (this.melee_infantry.isSelected()) {
      selected.performTrain("melee-infantry");
      this.error_message.setText("Melee infantry are now in a training spot!");
    } else if (this.pikemen.isSelected()) {
      selected.performTrain("pikemen");
      this.error_message.setText("Pikemen are now in a training spot!");
    }

  }

  @FXML
  public void clickedTax(ActionEvent e) {
    taxPopup = !taxPopup;
    tax_popup.setVisible(taxPopup);
    if (currentlySelectedFirstProvince == null) {
      tax_text.setText("Please select a province that you own.");
      low_tax.setVisible(false);
      normal_tax.setVisible(false);
      high_tax.setVisible(false);
      veryhigh_tax.setVisible(false);
      final_tax.setVisible(false);
      return;
    }
    Faction playerFaction = currentPlayer.getFaction();
    String humanProvince = (String) currentlySelectedFirstProvince.getAttributes().get("name");
    ArrayList<Province> playerProvinces = playerFaction.getProvinces();
    for (int i = 0; i < playerProvinces.size(); i++) {
      Province curr = playerProvinces.get(i);
      if (humanProvince.equals(curr.getProvinceName())) {
        this.selectedProvince = curr;
      }
    }

  }

  @FXML
  public void closeTax(ActionEvent e) {
    taxPopup = !taxPopup;
    tax_popup.setVisible(taxPopup);
    tax_text.setText("Please select the tax rate you would like to put on your province.");
  }

  @FXML
  public void callTax(ActionEvent e) {
    Province selected = this.selectedProvince;
    // double provinceWealth = selected.getWealth();
    if (low_tax.isSelected()) {
      selected.setTaxBehaviour(0.1, 10);
    } else if (normal_tax.isSelected()) {
      selected.setTaxBehaviour(0.15, 0);
    } else if (high_tax.isSelected()) {
      selected.setTaxBehaviour(0.2, -10);
    } else if (veryhigh_tax.isSelected()) {
      selected.setTaxBehaviour(0.25, -30);
    }
    tax_text.setText("Successfully changed the tax level!");
  }
}