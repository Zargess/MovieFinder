package moviefinder;

import javafx.event.Event;
import javafx.event.EventHandler;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Helene on 22-03-14.
 */
public class SearchEventHandler implements EventHandler{

    private SearchEventProvider provider;
    private MovieManager manager;

    public SearchEventHandler(SearchEventProvider provider){
        this.provider = provider;
        manager = provider.getManager();
    }

    @Override
    public void handle(Event event) {
        SearchInfo info = provider.getEventInfo();
        Set<Movie> movies = manager.search(info.searchText,info.category);
        provider.addEntriesToTable(movies);
    }
}
