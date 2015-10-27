package app.georchestra.beneth.fr.georchestra

import android.app.ProgressDialog
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import fr.beneth.wxslib.georchestra.Instance;

/**
 * Created by pmauduit on 10/27/15.
 */
class RetrieveGeorInstancesTask extends AsyncTask<Object, Void, Object> {

    private MainActivity activity

    ArrayList<Instance> geOrInstances

    RetrieveGeorInstancesTask(MainActivity a) {
        activity = a
    }

    @Override
    protected Object doInBackground(Object... params) {
        geOrInstances =  Instance.loadGeorchestraInstances()
        return
    }

    @Override
    protected void onPostExecute(Object result) {
        ListView lv = (ListView) activity.findViewById(R.id.wxsServersListView)
        def arr = geOrInstances.findAll { it.title != "" }.collect { it.title }
        def aa = new ArrayAdapter(activity, android.R.layout.simple_list_item_1,
                arr.toArray(new String[arr.size()]))
        lv.setAdapter(aa)
        return
    }
}