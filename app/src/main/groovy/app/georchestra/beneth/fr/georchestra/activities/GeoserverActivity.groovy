package app.georchestra.beneth.fr.georchestra.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import app.georchestra.beneth.fr.georchestra.R
import app.georchestra.beneth.fr.georchestra.holders.GeorInstanceHolder
import app.georchestra.beneth.fr.georchestra.holders.WmsCapabilitiesHolder
import app.georchestra.beneth.fr.georchestra.tasks.RetrieveWmsTask
import app.georchestra.beneth.fr.georchestra.utils.GsUtils
import fr.beneth.wxslib.Layer
import fr.beneth.wxslib.georchestra.Instance
import fr.beneth.wxslib.operations.Capabilities

public class GeoserverActivity extends AppCompatActivity {

    List<Layer> currentLayersList = null
    final int MENU_LAYER_INFO = 0
    final int MENU_METADATA = 1
    int georInstanceId



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gs)
        setTitle("GeoServer Layers")

        Bundle extras = getIntent().getExtras()
        georInstanceId = extras.getInt("GeorInstance.id")
        Instance ist = GeorInstanceHolder.getInstance().getGeorInstances().get(georInstanceId)
        Capabilities wmsCap = WmsCapabilitiesHolder.getInstance().getWmsCapabilities()
        def gsUrl = GsUtils.getGeoserverWmsUrl(ist.url, "getcapabilities")

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
        this.findViewById(R.id.ProgressBar).setVisibility(View.VISIBLE)
        wmsTask.execute(gsUrl)
    }

    public void refreshLayersList(ArrayList<Layer> layers) {
        currentLayersList = layers
        ListView lv = (ListView) this.findViewById(R.id.LayersList)

        def aa = new ArrayAdapter(this, android.R.layout.simple_list_item_2,
                android.R.id.text1, layers) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent)
                Layer l = layers.get(position)
                // Show layers with a mdUrl in green
                if (l.metadataUrls.size() > 0) {
                    view.setBackgroundResource(R.color.itemWithMd)
                }
                TextView text1 = (TextView) view.findViewById(android.R.id.text1)
                TextView text2 = (TextView) view.findViewById(android.R.id.text2)
                text1.setText(l.name ? l.name : l.title)
                text2.setText(l.title)
                return view
            }
        }
        registerForContextMenu(lv)
        lv.setAdapter(aa)
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        if (v.getId() == R.id.LayersList) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo
            Layer l = currentLayersList.get(info.position)
            menu.setHeaderTitle(l.title)
            if (l.metadataUrls.size() > 0) {
                menu.add(Menu.NONE, MENU_METADATA, MENU_METADATA, "Metadata")
            }
            menu.add(Menu.NONE, MENU_LAYER_INFO, MENU_LAYER_INFO, "Layer info")
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo()
        int menuItemIndex = item.getItemId()
        Layer l = currentLayersList.get(info.position)

        if (menuItemIndex == MENU_METADATA) {
            Intent mdIntent = new Intent(getApplicationContext(), MetadataActivity.class)
            mdIntent.putExtra("GeorInstance.id", georInstanceId)
            mdIntent.putExtra("GeorInstance.layer_name", l.name)

            startActivityForResult(mdIntent, RESULT_OK)
        } else if  (menuItemIndex == MENU_LAYER_INFO) {
            Intent liIntent = new Intent(getApplicationContext(), LayerInfoActivity.class)
            liIntent.putExtra("GeorInstance.layer_name", l.name)
            liIntent.putExtra("GeorInstance.id", georInstanceId)
            startActivityForResult(liIntent, RESULT_OK)
        }

        return true;
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
                    refreshLayersList(WmsCapabilitiesHolder.
                            getInstance().getWmsCapabilities().layers)
                } else {
                    // else get backwards in the layers list
                    refreshLayersList(layerParent.parentLayer.layers)
                }
                return true
        }
        return super.onOptionsItemSelected(item)
    }
}
