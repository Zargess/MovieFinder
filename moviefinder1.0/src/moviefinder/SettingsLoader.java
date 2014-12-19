package moviefinder;

import javafx.scene.control.Dialogs;
import javafx.stage.Stage;

import javax.naming.spi.StateFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by Helene on 06-04-14.
 */
public class SettingsLoader {

    private static final String movieFinderPath = "Settings\\MovieFinderPath.txt";
    private static final String movieFinderCategories = "Settings\\MovieFinderCategories.txt";
    private static final String movieFinderFileExtensions = "Settings\\MovieFinderFileExtensions.txt";

    public static File[] loadMainDirs(Stage stage){
        File path = new File(movieFinderPath);
        ArrayList<File> mainDirs = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line = br.readLine();
            if(line != null){
                while(line != null){
                    File dir = new File(line);
                    mainDirs.add(dir);
                    line = br.readLine();
                }
            }else {
                String message = nullMessage(movieFinderPath,"main movie directory", "path to the directory");
                String title = title(movieFinderPath);
                Dialogs.showErrorDialog(stage, message, title, "ERROR");
                System.exit(0);
            }
        } catch (Exception e){
            String message = excepMessege(movieFinderPath);
            String title = title(movieFinderPath);
            Dialogs.showErrorDialog(stage, message, title, "ERROR", e);
            System.exit(0);
        }
        File[] result = new File[mainDirs.size()];
        //System.out.println("MainDirs: " + mainDirs);
        return mainDirs.toArray(result);
    }

    public static File loadCategoryFile(Stage stage){
        File categoryFile = new File(movieFinderCategories);
        try {
            BufferedReader br = new BufferedReader(new FileReader((categoryFile)));
            String line = br.readLine();
            if(line == null){
                String message = nullMessage(movieFinderCategories,"categories","some categories");
                String title = title(movieFinderCategories);
                Dialogs.showErrorDialog(stage, message, title, "ERROR");
                System.exit(0);
            }
        } catch (Exception e){
            String message = excepMessege(movieFinderCategories);
            String title = title(movieFinderCategories);
            Dialogs.showErrorDialog(stage, message, title, "ERROR", e);
            System.exit(0);
        }
        return categoryFile;
    }

    public static File loadExtensionsFile(Stage stage){
        File extensionFile = new File(movieFinderFileExtensions);
        try {
            BufferedReader br = new BufferedReader(new FileReader((extensionFile)));
            String line = br.readLine();
            if(line == null){
                String message = nullMessage(movieFinderFileExtensions,"file extensions","some file extensions");
                String title = title(movieFinderFileExtensions);
                Dialogs.showErrorDialog(stage, message, title, "ERROR");
                System.exit(0);
            }
        } catch (Exception e){
            String message = excepMessege(movieFinderFileExtensions);
            String title = title(movieFinderFileExtensions);
            Dialogs.showErrorDialog(stage, message, title, "ERROR", e);
            System.exit(0);
        }
        return extensionFile;
    }

    private static String nullMessage(String file, String info, String add){
        return "Oops, the file \"" + file + "\" is missing information about " + info + ". \nAdd " + add + " and try again.";
    }

    private static String title(String file){
        return "Error in file \"" + file + "\"";
    }

    private static String excepMessege(String file){
        return "Oops, the file \"" + file + "\" doesn't exists. \nCreate it and add appropriated information.";
    }
}
