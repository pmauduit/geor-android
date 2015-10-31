package app.georchestra.beneth.fr.georchestra.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import app.georchestra.beneth.fr.georchestra.R
import app.georchestra.beneth.fr.georchestra.holders.GeorInstanceHolder
import app.georchestra.beneth.fr.georchestra.tasks.RetrieveImageTask
import fr.beneth.wxslib.georchestra.Instance

public class InstanceActivity extends AppCompatActivity {

    private int georInstanceId

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instance)
        Bundle extras = getIntent().getExtras()

        georInstanceId = extras.getInt("GeorInstance.id")
        Instance currentInst = GeorInstanceHolder.
                getInstance().getGeorInstances().get(georInstanceId)

        this.setTitle(currentInst.title)
        if (currentInst.logo_url) {
            ImageView logoView = this.findViewById(R.id.LogoView)
            def rit = new RetrieveImageTask(logoView)
            rit.execute(currentInst.logo_url)
        }

        // Hook for GeoServer button
        this.findViewById(R.id.gsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            void onClick(View v) {
                Intent gsActivity = new Intent(getApplicationContext(), GeoserverActivity.class)
                gsActivity.putExtra("GeorInstance.id", georInstanceId)
                startActivityForResult(gsActivity, RESULT_OK)
            }
        })
        // Hook for GeoNetwork button
        this.findViewById(R.id.gnButton).setOnClickListener(new View.OnClickListener() {
            @Override
            void onClick(View v) {
                Toast.makeText(getApplicationContext(), "not implemented yet !",
                        Toast.LENGTH_LONG).show()
            }
        })
        // Hook for viewer button
        this.findViewById(R.id.viewerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            void onClick(View v) {
                Toast.makeText(getApplicationContext(), "not implemented yet !",
                        Toast.LENGTH_LONG).show()
            }
        })
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
