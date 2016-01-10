package app.georchestra.beneth.fr.georchestra.activities

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import app.georchestra.beneth.fr.georchestra.R
import app.georchestra.beneth.fr.georchestra.holders.GeorInstanceHolder
import app.georchestra.beneth.fr.georchestra.tasks.RetrieveImageTask
import app.georchestra.beneth.fr.georchestra.tasks.RetrieveLastModifiedDatasetsTask
import app.georchestra.beneth.fr.georchestra.utils.GnUtils
import fr.beneth.cswlib.GetRecords
import fr.beneth.cswlib.metadata.Metadata
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

        def lastModLayout = findViewById(R.id.lastModifiedLayout)
        lastModLayout.setVisibility(View.GONE)


        def georInstanceId = extras.getInt("GeorInstance.id")
        Instance currentInst = GeorInstanceHolder.
                getInstance().getGeorInstances().get(georInstanceId)

        this.setTitle(currentInst.title)
        if (currentInst.logo_url) {
            ImageView logoView = this.findViewById(R.id.LogoView)
            def rit = new RetrieveImageTask([logoView])
            rit.execute([currentInst.logo_url])
        }

        new RetrieveLastModifiedDatasetsTask(this).execute(
                GnUtils.getCswUrl(currentInst.getUrl())
        )

        // Filling up the html desc of the instance
        def uv = (TextView) this.findViewById(R.id.urlText)
        uv.setText(currentInst.url)
        def av = (TextView) this.findViewById(R.id.abstractText)
        av.setText(currentInst._abstract)
        def pcb = (CheckBox) this.findViewById(R.id.productionCheckBox)
        pcb.setChecked(currentInst.isInProduction)
        def pubcb = (CheckBox) this.findViewById(R.id.publicCheckBox)
        pubcb.setChecked(currentInst.isPublic)

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

    void updateLastModified(GetRecords getRecords, Throwable error) {

        if ((getRecords == null) && (error != null)) {
            Toast.makeText(getApplicationContext(), error.getMessage(),
                    Toast.LENGTH_LONG).show()
            return
        }
        def lv = findViewById(R.id.lastModifiedList)
        def aa = new ArrayAdapter(this, android.R.layout.simple_list_item_2,
                android.R.id.text1, getRecords.metadatas) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent)
                TextView text1 = (TextView) view.findViewById(android.R.id.text1)
                TextView text2 = (TextView) view.findViewById(android.R.id.text2)
                text1.setText((position + 1) + ". " + getRecords.getMetadatas().get(position).title)
                def _abstract = getRecords.getMetadatas().get(position)._abstract
                if (_abstract.length() > 150) {
                    _abstract = _abstract.substring(0, 147) + "..."
                }
                text2.setText(_abstract)
                text1.setTypeface(null, Typeface.BOLD)
                text2.setTypeface(null, Typeface.ITALIC)
                return view
            }
        }
        lv.setAdapter(aa)
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle extras = getIntent().getExtras()
                def georInstanceId = extras.getInt("GeorInstance.id")
                def md = getRecords.metadatas.get(position)
                Intent mdIntent = new Intent(getApplicationContext(), MetadataActivity.class)
                mdIntent.putExtra("md.uuid", md.fileIdentifier)
                mdIntent.putExtra("GeorInstance.id", georInstanceId)
                startActivityForResult(mdIntent, RESULT_OK)
            }
        })
        def lastModLayout = findViewById(R.id.lastModifiedLayout)
        lastModLayout.setVisibility(View.VISIBLE)

    }
}
