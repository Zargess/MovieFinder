package moviefinder;

/**
 * Created by Helene on 04-04-14.
 */
public class SearchInfo {
    public final String searchText;
    public final String category;

    public SearchInfo(String searchString, String categoryString){
        if(searchString == null){
            searchString = "";
        }
        if(categoryString == null){
            categoryString = "none";
        }

        searchText = searchString;
        category = categoryString;
    }
}
