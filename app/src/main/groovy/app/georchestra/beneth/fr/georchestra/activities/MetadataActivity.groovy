package app.georchestra.beneth.fr.georchestra.activities

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.TextView
import app.georchestra.beneth.fr.georchestra.R
import app.georchestra.beneth.fr.georchestra.holders.GeorInstanceHolder
import app.georchestra.beneth.fr.georchestra.holders.WmsCapabilitiesHolder
import fr.beneth.wxslib.Layer
import fr.beneth.wxslib.georchestra.Instance
import fr.beneth.wxslib.operations.Capabilities

public class MetadataActivity extends AppCompatActivity {
    private Layer layer
    public MetadataActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metadata)
        Bundle extras = getIntent().getExtras()
        int georInstanceId = extras.getInt("GeorInstance.id")
        String layerName = extras.getString("GeorInstance.layer_name")
        Instance ist = GeorInstanceHolder.getInstance().getGeorInstances().get(georInstanceId)
        Capabilities wmsCap = WmsCapabilitiesHolder.getInstance().getWmsCapabilities()
        layer = wmsCap.findLayerByName(layerName)
        TextView tv = (TextView) this.findViewById(R.id.mdTextView)
        tv.setText(layer.title)
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
