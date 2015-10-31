package app.georchestra.beneth.fr.georchestra.tasks

import android.os.AsyncTask
import android.view.View
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import app.georchestra.beneth.fr.georchestra.holders.GeorInstanceHolder
import app.georchestra.beneth.fr.georchestra.activities.MainActivity
import app.georchestra.beneth.fr.georchestra.R;
import fr.beneth.wxslib.georchestra.Instance;

class RetrieveGeorInstancesTask extends AsyncTask<Object, Void, Object> {

    MainActivity activity
    Throwable error = null

    RetrieveGeorInstancesTask(MainActivity a) {
        activity = a
    }

    @Override
    protected Object doInBackground(Object... params) {
        try {
            def ists =  Instance.loadGeorchestraInstances()
            // Removes instances with no title and not public
            def georInstances = ists.findAll { it.title != ""  && it.isPublic }
            GeorInstanceHolder.getInstance().setGeorInstances(georInstances)
            return georInstances
        } catch (Throwable e) {
            error = e
        }
        // error occured, returning empty array
        return new ArrayList<Instance>()
    }

    @Override
    protected void onPostExecute(Object result) {
        ArrayList<Instance> geOrInstances = (ArrayList<Instance>) result
        ListView lv = (ListView) activity.findViewById(R.id.wxsServersListView)

        activity.findViewById(R.id.ProgressBar).setVisibility(View.GONE)

        def aa = new ArrayAdapter(activity, android.R.layout.simple_list_item_2, android.R.id.text1, geOrInstances) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent)
                TextView text1 = (TextView) view.findViewById(android.R.id.text1)
                TextView text2 = (TextView) view.findViewById(android.R.id.text2)
                text1.setText(geOrInstances.get(position).title)
                text2.setText(geOrInstances.get(position).url)
                return view
            }
        }
        lv.setAdapter(aa)

        return
    }
}