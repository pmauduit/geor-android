package app.georchestra.beneth.fr.georchestra.tasks

import android.os.AsyncTask
import android.util.Log
import app.georchestra.beneth.fr.georchestra.activities.GeonetworkActivity
import app.georchestra.beneth.fr.georchestra.holders.CatalogueResourcesHolder
import fr.beneth.cswlib.GetDomain
import fr.beneth.cswlib.geonetwork.GeoNetworkSource


public class RetrieveCatalogueResourcesTask extends AsyncTask<Object, Void, Object> {

    Throwable error = null
    GeonetworkActivity activity

    public RetrieveCatalogueResourcesTask(GeonetworkActivity activity) {
        this.activity = activity
    }

    @Override
    protected Object doInBackground(Object... params) {
        try {
            def gnUrl = params[0]
            def organisms = GeoNetworkSource.mapFromGeonetwork(gnUrl)
            def typeOfResources = GetDomain.executeGetDomain(gnUrl + "srv/eng/csw", ["Type"])
            CatalogueResourcesHolder.getInstance().setGeonetworkSources(organisms)
            def tor = typeOfResources.domainValues.get("Type")
            CatalogueResourcesHolder.getInstance().setTypeOfResources(tor)
            return [ organisms,  tor ]
        } catch (Throwable e) {
            error = e
        }
        return null
    }

    @Override
    protected void onPostExecute(Object result) {
        if ((result == null) || (result[0] == null) || (result[1] == null)) {
            activity.discard()
            return
        }
        activity.updateView((List<GeoNetworkSource>) result[0],
                (List<String>) result[1])
    }
}
