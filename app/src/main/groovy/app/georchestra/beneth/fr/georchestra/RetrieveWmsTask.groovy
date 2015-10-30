package app.georchestra.beneth.fr.georchestra

import android.app.Activity
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import fr.beneth.wxslib.operations.Capabilities

public class RetrieveWmsTask extends AsyncTask<Object, Void, Object> {

    private error = null

    private GeoserverActivity activity


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
        if (result == null) {
            // TODO handle error ?
            return
        }
        def wmsCap = (Capabilities) result
        activity.currentLayersList = wmsCap.layers

        ListView lv = (ListView) activity.findViewById(R.id.LayersList)
        def aa = new ArrayAdapter(activity, android.R.layout.simple_list_item_2,
                android.R.id.text1, wmsCap) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                text1.setText(wmsCap.layers.get(position).name ?
                        wmsCap.layers.get(position).name :
                        wmsCap.layers.get(position).title);
                text2.setText(wmsCap.layers.get(position).title);
                return view;
            }
        }
        lv.setAdapter(aa)
    }
}
