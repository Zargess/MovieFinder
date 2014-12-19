package moviefinder;

import java.util.Set;

/**
 * Created by Helene on 22-03-14.
 */
public interface SearchEventProvider {

    public SearchInfo getEventInfo();
    public MovieManager getManager();
    public void addEntriesToTable(Set<Movie> movies);
}
