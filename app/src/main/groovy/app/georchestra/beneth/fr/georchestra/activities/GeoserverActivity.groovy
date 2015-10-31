package app.georchestra.beneth.fr.georchestra.activities

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import app.georchestra.beneth.fr.georchestra.R
import app.georchestra.beneth.fr.georchestra.holders.GeorInstanceHolder
import app.georchestra.beneth.fr.georchestra.holders.WmsCapabilitiesHolder
import app.georchestra.beneth.fr.georchestra.tasks.RetrieveWmsTask
import fr.beneth.wxslib.Layer
import fr.beneth.wxslib.georchestra.Instance
import fr.beneth.wxslib.operations.Capabilities;

public class GeoserverActivity extends AppCompatActivity {

    List<Layer> currentLayersList = null

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gs)
        Bundle extras = getIntent().getExtras()
        int georInstanceId = extras.getInt("GeorInstance.id")
        Instance ist = GeorInstanceHolder.getInstance().getGeorInstances().get(georInstanceId)
        Capabilities wmsCap = WmsCapabilitiesHolder.getInstance().getWmsCapabilities()
        def gsUrl = ist.url -~ /mapfishapp\// + "geoserver/wms?service=wms&request=getcapabilities"

        ListView lv = (ListView) this.findViewById(R.id.LayersList)
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                def clickedLayer =  currentLayersList.get(position)
                if (clickedLayer.layers.isEmpty()) return
                refreshLayersList(clickedLayer.layers)
            }
        })

        def wmsTask = new RetrieveWmsTask(this)
        wmsTask.execute(gsUrl)
    }

    private void refreshLayersList(ArrayList<Layer> layers) {
        currentLayersList = layers
        ListView lv = (ListView) this.findViewById(R.id.LayersList)


        def aa = new ArrayAdapter(this, android.R.layout.simple_list_item_2,
                android.R.id.text1, layers) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                text1.setText(layers.get(position).name ?
                        layers.get(position).name :
                        layers.get(position).title);
                text2.setText(layers.get(position).title);
                return view;
            }
        }
        lv.setAdapter(aa)

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // handles the "arrow left" pressed event:
                // if there are parent layers, we need to
                // update the layers list

                // No list or no parent above, finishes the activity
                if (currentLayersList == null
                        || currentLayersList.size() == 0
                        || currentLayersList.get(0).parentLayer == null) {
                    this.finish()
                    return true
                }
                def layerParent = currentLayersList.get(0).parentLayer
                // if we have reached the root of layers
                if (layerParent.parentLayer == null) {
                    refreshLayersList(WmsCapabilitiesHolder.getInstance().getWmsCapabilities().layers)
                } else {
                    // else get backwards in the layers list
                    refreshLayersList(layerParent.parentLayer.layers)
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
