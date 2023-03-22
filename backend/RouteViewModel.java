package edu.ucsd.cse110.team19.walkwalkrevolution.backend;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class RouteViewModel extends AndroidViewModel {

    private RouteRepository mRepository;
    private LiveData<List<Route>> mAllRoutes;

    public RouteViewModel(Application application) {
        super(application);
        mRepository = new RouteRepository(application);
        mAllRoutes = mRepository.getAllRoutes();
    }

    public LiveData<List<Route>> getAllRoutes() {
        return mAllRoutes;
    }

    public void insert(Route route) {
        mRepository.insert(route);
    }

    public void update(Route route) {
        mRepository.update(route);
    }

    public void updateMetrics(String name, int steps, float miles) {
        mRepository.updateMetrics(name, steps, miles);
    }

    public void delete(String name) {
        mRepository.delete(name);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }
}
