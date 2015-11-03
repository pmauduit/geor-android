package app.georchestra.beneth.fr.georchestra.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle
import android.view.MenuItem
import android.widget.CheckBox
import android.widget.TextView
import app.georchestra.beneth.fr.georchestra.R
import app.georchestra.beneth.fr.georchestra.holders.WmsCapabilitiesHolder
import fr.beneth.wxslib.Layer;

public class LayerInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layer_info)

        Bundle extras = getIntent().getExtras()
        String layerName = extras.getString("GeorInstance.layer_name")
        def wmsCap = WmsCapabilitiesHolder.getInstance().getWmsCapabilities()
        Layer l = wmsCap.findLayerByName(layerName)

        this.setTitle(l.getName())

        TextView ltt = (TextView) this.findViewById(R.id.layerTitleText)
        TextView lat = (TextView) this.findViewById(R.id.layerAbstractText)
        TextView lattrt = (TextView) this.findViewById(R.id.layerAttributionText)
        TextView lkt = (TextView) this.findViewById(R.id.layerKeywordsText)
        TextView lst =  (TextView) this.findViewById(R.id.layerStylesText)
        CheckBox qchk = (CheckBox) this.findViewById(R.id.layerQueryableCheck)
        CheckBox qopk = (CheckBox) this.findViewById(R.id.layerOpaqueCheck)

        ltt.setText(l.title)
        lat.setText(l._abstract)
        lattrt.setText("Attribution: ${l.attributionTitle}")
        lkt.setText("Keywords: ${l.keywords.join(", ")}")
        qchk.setChecked(l.queryable)
        qopk.setChecked(l.opaque)
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish()
                return true
        }
        return super.onOptionsItemSelected(item)
    }
}
