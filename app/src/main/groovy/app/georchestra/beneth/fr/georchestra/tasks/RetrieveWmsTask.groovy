package app.georchestra.beneth.fr.georchestra.tasks

import android.app.AlertDialog
import android.os.AsyncTask
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import app.georchestra.beneth.fr.georchestra.activities.GeoserverActivity
import app.georchestra.beneth.fr.georchestra.R
import app.georchestra.beneth.fr.georchestra.holders.WmsCapabilitiesHolder
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
                alert.setPositiveButton("OK", null)
                alert.setCancelable(true)
                alert.create().show()
            }
            return
        }
        def wmsCap = (Capabilities) result
        activity.currentLayersList = wmsCap.layers


        def aa = new ArrayAdapter(activity, android.R.layout.simple_list_item_2,
                android.R.id.text1, wmsCap) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent)
                TextView text1 = (TextView) view.findViewById(android.R.id.text1)
                TextView text2 = (TextView) view.findViewById(android.R.id.text2)
                text1.setText(wmsCap.layers.get(position).name ?
                        wmsCap.layers.get(position).name :
                        wmsCap.layers.get(position).title)
                text2.setText(wmsCap.layers.get(position).title)
                return view
            }
        }
        lv.setAdapter(aa)
    }
}
