package Modules;

import java.util.List;

/**
 * Created by Mrs.shimaa on 2/27/2018.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
