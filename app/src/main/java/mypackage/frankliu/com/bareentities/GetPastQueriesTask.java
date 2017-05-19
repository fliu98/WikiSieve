package mypackage.frankliu.com.bareentities;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

import mypackage.frankliu.com.bareentities.database.DataController;
import mypackage.frankliu.com.bareentities.database.PastQuery;

public class GetPastQueriesTask extends AsyncTaskLoader<ArrayList<PastQuery>> {

    public GetPastQueriesTask(Context context) {
        super(context);
    }

    @Override
    public ArrayList<PastQuery> loadInBackground() {
        DataController dataController = new DataController(getContext());
        dataController.open();
        ArrayList<PastQuery> result = new ArrayList<>();
        List<PastQuery> pastQueries = dataController.getAllPastQueries();
        dataController.close();
        if(pastQueries!=null){
            result.addAll(pastQueries);
        }
        return result;
    }
}
