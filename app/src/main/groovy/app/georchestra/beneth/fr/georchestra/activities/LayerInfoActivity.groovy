package app.georchestra.beneth.fr.georchestra.activities

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle
import android.util.AttributeSet
import android.view.MenuItem
import android.widget.AbsoluteLayout
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
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

        if (l.name)
            this.setTitle(l.name)
        else
            this.setTitle(l.title)
        TextView ltt = (TextView) this.findViewById(R.id.layerTitleText)
        TextView lat = (TextView) this.findViewById(R.id.layerAbstractText)
        TextView lattrt = (TextView) this.findViewById(R.id.layerAttributionText)
        TextView lkt = (TextView) this.findViewById(R.id.layerKeywordsText)
        TextView lst =  (TextView) this.findViewById(R.id.layerStylesText)
        CheckBox qchk = (CheckBox) this.findViewById(R.id.layerQueryableCheck)
        CheckBox qopk = (CheckBox) this.findViewById(R.id.layerOpaqueCheck)

        ltt.setText(l.title)
        lat.setText(l._abstract)
        if (l.attributionTitle) {
            lattrt.setText("Attribution: ${l.attributionTitle}")
        } else {
            lattrt.setText("")
        }
        if (l.keywords.size()) {
            lkt.setText("Keywords: ${l.keywords.join(", ")}")
        } else {
            lkt.setText("")
        }
        if (l.styles.size()) {
            def layersTitles = l.styles.collect { it.name }
            lst.setText("Styles: ${layersTitles.join(", ")}")
        }
        else {
            lst.setText("")
        }
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
