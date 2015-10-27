package app.georchestra.beneth.fr.georchestra

import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import fr.beneth.wxslib.georchestra.Instance

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView lv = (ListView) this.findViewById(R.id.wxsServersListView)
        try {
            def task = new RetrieveGeorInstancesTask(this)
            task.execute()
        } catch (Throwable e) {
            Log.e("MainActivity", Log.getStackTraceString(e))
        }

    }
}

