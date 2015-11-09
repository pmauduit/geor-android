package app.georchestra.beneth.fr.georchestra.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.*
import app.georchestra.beneth.fr.georchestra.R
import app.georchestra.beneth.fr.georchestra.holders.GeorInstanceHolder
import app.georchestra.beneth.fr.georchestra.holders.WmsCapabilitiesHolder
import app.georchestra.beneth.fr.georchestra.tasks.RetrieveWmsTask
import app.georchestra.beneth.fr.georchestra.utils.GnUtils
import app.georchestra.beneth.fr.georchestra.utils.GsUtils
import fr.beneth.wxslib.Layer
import fr.beneth.wxslib.georchestra.Instance
import fr.beneth.wxslib.operations.Capabilities

public class GeoserverActivity extends AppCompatActivity {

    List<Layer> currentLayersList = new ArrayList<Layer>()
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
                def oldColor = ((ColorDrawable) view.getBackground()).getColor()
                view.setBackgroundColor(android.R.drawable.list_selector_background)

                def clickedLayer =  currentLayersList.get(position)
                if (clickedLayer.layers.isEmpty()) {
                    view.setBackgroundColor(oldColor)
                    view.showContextMenu()
                    return
                }
                refreshLayersList(clickedLayer.layers)
            }
        })
        registerForContextMenu(lv)

        def wmsTask = new RetrieveWmsTask(this)
        this.findViewById(R.id.ProgressBar).setVisibility(View.VISIBLE)
        wmsTask.execute(gsUrl)
    }

    public void refreshLayersList(ArrayList<Layer> layers) {
        currentLayersList = layers.clone()
        ListView lv = (ListView) this.findViewById(R.id.LayersList)
        if (lv.getAdapter() == null) {
            def aa = new ArrayAdapter(this, android.R.layout.simple_list_item_2,
                    android.R.id.text1, currentLayersList) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent)
                    Layer l = currentLayersList.get(position)
                    // Show layers with a mdUrl in green
                    if (GnUtils.isEligible(l.metadataUrls)) {
                        view.setBackgroundResource(R.color.itemWithMd)
                    } else {
                        view.setBackgroundResource(android.R.color.transparent)
                    }
                    TextView text1 = (TextView) view.findViewById(android.R.id.text1)
                    TextView text2 = (TextView) view.findViewById(android.R.id.text2)
                    text1.setText(l.name ? l.name : l.title)
                    text2.setText(l.title)
                    return view
                }
            }
            lv.setAdapter(aa)
        } else {
            def aa = (ArrayAdapter) lv.getAdapter()
            aa.clear()
            aa.addAll(layers)
            aa.notifyDataSetChanged()
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        if (v.getId() == R.id.LayersList) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo
            Layer l = currentLayersList.get(info.position)
            menu.setHeaderTitle(l.title)
            if (GnUtils.isEligible(l.metadataUrls)) {
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
            if (GnUtils.isEligible(l.metadataUrls)) {
                Intent mdIntent = new Intent(getApplicationContext(), MetadataActivity.class)
                mdIntent.putExtra("GeorInstance.id", georInstanceId)
                mdIntent.putExtra("GeorInstance.layer_name", l.name)
                startActivityForResult(mdIntent, RESULT_OK)
            } else {
                Toast.makeText(this,"No metadata eligible for display !", Toast.LENGTH_LONG).show()
            }
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
                if (currentLayersList.size() == 0
                        || currentLayersList.get(0).parentLayer == null) {
                    this.finish()
                    return true
                }
                def parent = currentLayersList.get(0).parentLayer
                // Root of the layer hierarchy
                if (parent.parentLayer == null) {
                    def wmsCap = WmsCapabilitiesHolder.getInstance().getWmsCapabilities()
                    def newList = wmsCap.layers
                    refreshLayersList(newList)
                } else {
                    refreshLayersList(parent.parentLayer.layers)
                }
                return true
        }
        return super.onOptionsItemSelected(item)
    }
}
