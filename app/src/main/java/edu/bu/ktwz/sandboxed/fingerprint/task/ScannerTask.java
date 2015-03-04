package edu.bu.ktwz.sandboxed.fingerprint.task;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;

import java.util.List;

import edu.bu.ktwz.sandboxed.R;
import edu.bu.ktwz.sandboxed.fingerprint.APIScanner;
import edu.bu.ktwz.sandboxed.fingerprint.AndroidFrameworkFileIO;

/**
 * Build the database in a separate thread
 * Created by wil on 2/1/15.
 */

//TODO may want this in a service if it takes to long
public class ScannerTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private DatabaseBuildTaskCallback callback;
    public ScannerTask(Context context, DatabaseBuildTaskCallback callback){
        super();
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(Void... params) {

        Looper.prepare(); //Prevent RTE cant create handler without calling looper.prepare

        AndroidFrameworkFileIO d = new AndroidFrameworkFileIO(context);

        String preloadedClassFilename = context.getString(R.string.filename_preloaded_classes);

        List<String> classList = d.loadPreloadedClassList(preloadedClassFilename);

        APIScanner scanner = new APIScanner(context);
        scanner.fullScan(classList);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        callback.onDatabaseBuildSuccess();
    }

    public interface DatabaseBuildTaskCallback {
        public void onDatabaseBuildSuccess();
        public void onDatabaseBuildFailure();

    }
}