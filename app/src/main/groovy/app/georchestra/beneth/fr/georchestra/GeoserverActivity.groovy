package app.georchestra.beneth.fr.georchestra

import android.app.Activity
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle
import android.view.MenuItem
import fr.beneth.wxslib.georchestra.Instance
import fr.beneth.wxslib.operations.Capabilities;

public class GeoserverActivity extends AppCompatActivity {

    private int georInstanceId

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gs)
        Bundle extras = getIntent().getExtras()
        georInstanceId = extras.getInt("GeorInstance.id")
        Instance ist = GeorInstanceHolder.getInstance().getGeorInstances().get(georInstanceId)

        def gsUrl = ist.url -~ /mapfishapp\// + "geoserver/wms?service=wms&request=getcapabilities"

        def wmsTask = new RetrieveWmsTask((Activity) this)
        wmsTask.execute(gsUrl)
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
