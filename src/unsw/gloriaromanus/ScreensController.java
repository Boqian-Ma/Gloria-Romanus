package unsw.gloriaromanus;


import java.util.HashMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;



public class ScreensController extends StackPane {
    // Maps screen id to the root of the screen
    private HashMap<String, Node> screens = new HashMap<>();
    
    public ScreensController() {
        super();
    }

    /**
     * Add the screen to the collection 
     * @param name
     * @param screen
     */
    public void addScreen(String name, Node screen) {
        screens.put(name, screen);
    }

    public Node getScreen(String name) {
        return screens.get(name);
    }

    /**
     * Loads the fxml file, add the screen to the screens collection and 
     * injects the screenPane to the controller
     * @param id
     * @param file
     * @return
     */
    public boolean loadScreen(String id, String file) {
        try {
            // load file
            System.out.println("id = " + id + " file = " + file);
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(file));
            System.out.println(id + "  " + myLoader);

            Parent loadScreen = (Parent) myLoader.load();

            ControlledScreen myScreenControler = ((ControlledScreen) myLoader.getController());
            //System.out.println(myLoader.getController().toString());
            myScreenControler.setScreenParent(this);
            addScreen(id, loadScreen);
            return true;
        } catch (Exception e) {
            System.out.println("Message " + e.getMessage());
            System.out.println("Message 2" + e.getMessage());
            return false;
        }
    }

    /**
     * Set a screen to be displayed 
     * @param name
     * @return
     */
    public boolean setScreen(final String name) {
        System.out.println("name = " + name);
        
        if (screens.containsKey(name)) {    // if a screen is in the list loaded initially
            if (!getChildren().isEmpty()) {
                getChildren().remove(0);
                getChildren().add(0, screens.get(name));
            } else {
                getChildren().add(screens.get(name));   // show a screen
            }
            return true;
        } else {
            System.out.println("Screen isnt loaded \n");
            return false;
        }
    }

    /**
     * Remove a screen from the tree
     * @param name
     * @return
     */
    public boolean unloadScreen(String name) {
        if (screens.remove(name) == null) {
            System.out.println("Screen didnt exist\n");
            return false;
        } else {
            return true;
        }
    }
}
