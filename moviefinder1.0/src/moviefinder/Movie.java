package moviefinder;

import java.util.Arrays;

/**
 * Created by Helene on 16-02-14.
 */
public class Movie {

    public final String mainDir;
    public final String[] categories;
    public final String title;

    public Movie(String mainDir, String[] categories, String title){
        this.mainDir = mainDir;
        this.categories = categories;
        this.title = title;
    }

    public String toString(){
        return "(" + mainDir + ", [" + categoriesToString() + "], " + title + ")";
    }

    public String categoriesToString(){
        if(categories.length == 0) return "";
        String res = "";

        for(int i = 0; i < categories.length-1; i++){
            res = res + categories[i] + ", ";
        }
        res = res + categories[categories.length-1];

        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        if (!Arrays.equals(categories, movie.categories)) return false;
        if (mainDir != null ? !mainDir.equals(movie.mainDir) : movie.mainDir != null) return false;
        if (title != null ? !title.equals(movie.title) : movie.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mainDir != null ? mainDir.hashCode() : 0;
        result = 31 * result + (categories != null ? Arrays.hashCode(categories) : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }
}
