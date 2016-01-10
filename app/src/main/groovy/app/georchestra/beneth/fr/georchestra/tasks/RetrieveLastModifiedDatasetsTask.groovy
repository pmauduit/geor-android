package app.georchestra.beneth.fr.georchestra.tasks

import android.os.AsyncTask
import app.georchestra.beneth.fr.georchestra.activities.InstanceActivity
import fr.beneth.cswlib.GetRecords
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response


/**
 * Created by pmauduit on 1/10/16.
 */
public class RetrieveLastModifiedDatasetsTask extends AsyncTask<Object, Void, GetRecords> {
    Throwable error = null
    InstanceActivity activity
    GetRecords getRecords

    public RetrieveLastModifiedDatasetsTask(InstanceActivity activity) {
        this.activity = activity
    }

    @Override
    protected GetRecords doInBackground(Object... params) {
        try {
            def cswUrl = params[0]
            // Too bad, Apache HTTPClient from groovyx.HTTPBuilder
            // is incompatible with the one provided by Android.
            // Considering hitting the CSW server by hand, using OKHttp
            def XML = MediaType.parse("application/xml; charset=utf-8")
            OkHttpClient client = new OkHttpClient()
            def cswReq = GetRecords.buildQueryOrder(1, 10, "dataset", "changeDate", "DESC")
            RequestBody body = RequestBody.create(XML, cswReq)
            Request request = new Request.Builder()
                        .url(cswUrl)
                        .post(body)
                        .build()
            Response response = client.newCall(request).execute()
            getRecords = GetRecords.getAllMetadatasFromDocument(response.body().string())
            return getRecords
        } catch (Exception e) {
            error = e
        }
        return null
    }

    @Override
    protected void onPostExecute(GetRecords result) {
        activity.updateLastModified(result, error)
    }
}
