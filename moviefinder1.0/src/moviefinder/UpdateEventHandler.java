package moviefinder;

import javafx.event.Event;
import javafx.event.EventHandler;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: HFH
 * Date: 19-04-14
 * Time: 17:50
 * To change this template use File | Settings | File Templates.
 */
public class UpdateEventHandler implements EventHandler {

    private UpdateEventProvider provider;

    public UpdateEventHandler(UpdateEventProvider provider){
        this.provider = provider;
    }

    @Override
    public void handle(Event event) {
        provider.update();
    }
}
