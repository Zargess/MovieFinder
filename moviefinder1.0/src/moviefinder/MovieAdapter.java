package moviefinder;

import javafx.beans.property.SimpleStringProperty;
import moviefinder.Movie;

/**
 * Created by Helene on 04-04-14.
 */
public class MovieAdapter {
    private final SimpleStringProperty mainDir;
    private final SimpleStringProperty categories;
    private final SimpleStringProperty title;

    public MovieAdapter(Movie movie){
        this.mainDir = new SimpleStringProperty(movie.mainDir);
        this.title = new SimpleStringProperty(movie.title);
        this.categories = new SimpleStringProperty(movie.categoriesToString());
    }

    public String getMainDir() {
        return mainDir.get();
    }

    public SimpleStringProperty mainDirProperty() {
        return mainDir;
    }

    public String getCategories() {
        return categories.get();
    }

    public SimpleStringProperty categoriesProperty() {
        return categories;
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }
}
