package app.georchestra.beneth.fr.georchestra.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import app.georchestra.beneth.fr.georchestra.R
import app.georchestra.beneth.fr.georchestra.holders.GeorInstanceHolder
import app.georchestra.beneth.fr.georchestra.tasks.RetrieveGeorInstancesTask
import fr.beneth.wxslib.georchestra.Instance

public class MainActivity extends AppCompatActivity {

    RetrieveGeorInstancesTask georInstancesTask = new RetrieveGeorInstancesTask(this)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView lv = (ListView) this.findViewById(R.id.wxsServersListView)

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                def georInstances = GeorInstanceHolder.getInstance().getGeorInstances()
                if (georInstances == null)
                    return
                Instance i = georInstances.get(position)
                Intent georInstanceIntent = new Intent(getApplicationContext(), InstanceActivity.class)
                georInstanceIntent.putExtra("GeorInstance.id", position)
                startActivityForResult(georInstanceIntent, RESULT_OK)
            }
        })
        this.findViewById(R.id.ProgressBar).setVisibility(View.VISIBLE)
        georInstancesTask.execute()
    }
}

