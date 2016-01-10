package app.georchestra.beneth.fr.georchestra.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import app.georchestra.beneth.fr.georchestra.R
import app.georchestra.beneth.fr.georchestra.holders.GeorInstanceHolder
import app.georchestra.beneth.fr.georchestra.tasks.RetrieveImageTask
import fr.beneth.wxslib.georchestra.Instance

public class InstanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instance)
        Bundle extras = getIntent().getExtras()

        if ((GeorInstanceHolder.getInstance().getGeorInstances() == null) || (! extras)) {
            this.finish()
        }

        def georInstanceId = extras.getInt("GeorInstance.id")
        Instance currentInst = GeorInstanceHolder.
                getInstance().getGeorInstances().get(georInstanceId)

        this.setTitle(currentInst.title)
        if (currentInst.logo_url) {
            ImageView logoView = this.findViewById(R.id.LogoView)
            def rit = new RetrieveImageTask([logoView])
            rit.execute([currentInst.logo_url])
        }

        // Filling up the html desc of the instance
        def uv = (TextView) this.findViewById(R.id.urlText)
        uv.setText(currentInst.url)
        def av = (TextView) this.findViewById(R.id.abstractText)
        av.setText(currentInst._abstract)
        def pcb = (CheckBox) this.findViewById(R.id.productionCheckBox)
        pcb.setChecked(currentInst.isInProduction)
        def pubcb = (CheckBox) this.findViewById(R.id.publicCheckBox)
        pubcb.setChecked(currentInst.isPublic)

//        def lv = (ExpandableListView) findViewById(R.id.lastChangedView)
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//                this,
//                android.R.layout.simple_list_item_1,
//                ["aaaaa", "bbbbbbbbbbb", "cccccc", "dddddddddd", "e",
//                "fffff", "gggggg"])
//        lv.setAdapter(arrayAdapter)
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.instance, menu)
        return true
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish()
                return true
            case R.id.action_gs:
                openActivity(GeoserverActivity.class)
                break
            case R.id.action_gn:
                openActivity(GeonetworkActivity.class)
                break
        }
        return super.onOptionsItemSelected(item)
    }

    private void openActivity(Class activity) {
        Bundle extras = getIntent().getExtras()
        def georInstanceId = extras.getInt("GeorInstance.id")
        Intent gsActivity = new Intent(getApplicationContext(), activity)
        gsActivity.putExtra("GeorInstance.id", georInstanceId)
        startActivityForResult(gsActivity, RESULT_OK)
    }
}
