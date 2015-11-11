package app.georchestra.beneth.fr.georchestra.tasks

import android.os.AsyncTask
import android.widget.Toast
import app.georchestra.beneth.fr.georchestra.activities.GeonetworkActivity
import app.georchestra.beneth.fr.georchestra.holders.GeoNetworkQueryHolder
import fr.beneth.cswlib.geonetwork.GeoNetworkQuery

public class RetrieveGeonetworkResultsTask  extends AsyncTask<Object, Void, GeoNetworkQuery> {
    Throwable error = null
    GeonetworkActivity activity


    public RetrieveGeonetworkResultsTask(GeonetworkActivity activity) {
        this.activity = activity
    }

    @Override
    protected GeoNetworkQuery doInBackground(Object... params) {
        try {
            def qurl = params[0]
            def gnq = GeoNetworkQuery.mapFromUrl(qurl)
            GeoNetworkQueryHolder.getInstance().setGeoNetworkQuery(gnq)
            return gnq
        } catch (Throwable e) {
            error = e
        }
        return null
    }

    @Override
    protected void onPostExecute(GeoNetworkQuery result) {
        if ((result == null) || (error)) {
            Toast.makeText(activity.getApplicationContext(), error.getMessage(),
                    Toast.LENGTH_LONG).show()
        }
        activity.notifyGeonetworkResults(result)

    }
}
