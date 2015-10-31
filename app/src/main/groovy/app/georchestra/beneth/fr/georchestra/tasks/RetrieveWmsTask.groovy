package app.georchestra.beneth.fr.georchestra.tasks

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.AsyncTask
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import app.georchestra.beneth.fr.georchestra.activities.GeoserverActivity
import app.georchestra.beneth.fr.georchestra.holders.WmsCapabilitiesHolder
import app.georchestra.beneth.fr.georchestra.R
import fr.beneth.wxslib.Layer
import fr.beneth.wxslib.operations.Capabilities

public class RetrieveWmsTask extends AsyncTask<Object, Void, Object> {

    Throwable error = null
    GeoserverActivity activity


    public RetrieveWmsTask(GeoserverActivity activity) {
        this.activity = activity
    }

    @Override
    protected Object doInBackground(Object... params) {
        try {
            def wmsUrl = params[0]
            def cap = Capabilities.mapFromDocument(wmsUrl.toString())
            WmsCapabilitiesHolder.getInstance().setWmsCapabilities(cap)
            return cap
        } catch (Throwable e) {
            error = e
        }
        return null
    }

    @Override
    protected void onPostExecute(Object result) {
        activity.findViewById(R.id.ProgressBar).setVisibility(View.GONE)
        ListView lv = (ListView) activity.findViewById(R.id.LayersList)

        if (result == null) {
            if (error) {
                AlertDialog.Builder alert  = new AlertDialog.Builder(activity)
                alert.setMessage("Error: " + error.getLocalizedMessage())
                alert.setTitle("geOrchestra - GeoServer")
                alert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                activity.finish()
                            }
                        }
                )
                alert.create().show()
            }
            return
        }
        def wmsCap = (Capabilities) result
        activity.refreshLayersList(wmsCap.layers)
    }
}
