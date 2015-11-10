package app.georchestra.beneth.fr.georchestra.tasks

import android.os.AsyncTask
import app.georchestra.beneth.fr.georchestra.activities.GeonetworkActivity
import app.georchestra.beneth.fr.georchestra.holders.GeonetworkSourcesHolder
import fr.beneth.cswlib.geonetwork.GeoNetworkSource


public class RetrieveOrganismsTask extends AsyncTask<Object, Void, Object> {

    Throwable error = null
    GeonetworkActivity activity

    public RetrieveOrganismsTask(GeonetworkActivity activity) {
        this.activity = activity
    }

    @Override
    protected Object doInBackground(Object... params) {
        try {
            def gnUrl = params[0]
            def organisms = GeoNetworkSource.mapFromGeonetwork(gnUrl)
            GeonetworkSourcesHolder.getInstance().setGeonetworkSources(organisms)
            return organisms
        } catch (Throwable e) {
            error = e
        }
        return null
    }

    @Override
    protected void onPostExecute(Object result) {
        if (result == null)
            return
        activity.updateOrganismsList((List<GeoNetworkSource>) result)
    }
}
