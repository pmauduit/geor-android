package app.georchestra.beneth.fr.georchestra;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle
import fr.beneth.wxslib.georchestra.Instance
import fr.beneth.wxslib.operations.Capabilities;

public class gsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gs)
        Bundle extras = getIntent().getExtras()
        int pos = extras.getInt("GeorInstance.id")
        Instance ist = GeorInstanceHolder.getInstance().getGeorInstances().get(pos)

        def gsUrl = ist.url -~ /mapfishapp\// + "geoserver/wms?service=wms&request=getcapabilities"

        // to be continued ...
        // Capabilities cap = Capabilities.mapFromDocument(gsUrl)
    }
}
