package mypackage.frankliu.com.bareentities;

import android.app.IntentService;
import android.content.Intent;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InsertPastQueryIntentService extends IntentService{

    private static final String EXTRA_TEXT_ANALYSIS = "EXTRA_TEXT_ANALYSIS";
    private static final String EXTRA_QUERY_STRING_RECORD = "EXTRA_QUERY_STRING_RECORD";
    private static final String EXTRA_RESULT_RECEIVER = "EXTRA_RESULT_RECEIVER";

    private ResultReceiver mResultReceiver;

    public InsertPastQueryIntentService(){
        super(InsertPastQueryIntentService.class.getSimpleName());
    }

    public InsertPastQueryIntentService(String name) {
        super(name);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        if(intent!=null) {
            mResultReceiver = intent.getExtras().getParcelable(EXTRA_RESULT_RECEIVER);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent==null){
            return;
        }
        TextAnalysis analysis = intent.getExtras().getParcelable(EXTRA_TEXT_ANALYSIS);
        DataController dataController = new DataController(getApplicationContext());
        dataController.open();
        if(analysis==null){
            return;
        }
        if(analysis.getAnnotations()==null||analysis.getAnnotations().size()==0){
            return;
        }

        String imageUrl = null;
        String queryText = intent.getExtras().getString(EXTRA_QUERY_STRING_RECORD);
        int conceptCount = analysis.getAnnotations().size();
        String timestamp = new SimpleDateFormat().format(Calendar.getInstance().getTime());

        if(analysis.getAnnotations().get(0).getImage()!=null){
            imageUrl = analysis.getAnnotations().get(0).getImage().getThumbnail();
        }

        dataController.insertPastQuery(
                imageUrl,
                queryText,
                conceptCount,
                timestamp
        );
        dataController.close();
        mResultReceiver.send(0,null);
    }
}
