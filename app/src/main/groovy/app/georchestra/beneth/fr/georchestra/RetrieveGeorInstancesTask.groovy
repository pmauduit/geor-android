package app.georchestra.beneth.fr.georchestra

import android.app.ProgressDialog
import android.content.Context;
import android.os.AsyncTask
import android.view.View
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView
import android.widget.TextView;
import fr.beneth.wxslib.georchestra.Instance;

class RetrieveGeorInstancesTask extends AsyncTask<Object, Void, Object> {

    private MainActivity activity

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

        def aa = new ArrayAdapter(activity, android.R.layout.simple_list_item_2, android.R.id.text1, geOrInstances) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                def georInstances = GeorInstanceHolder.getInstance().getGeorInstances()
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                text1.setText(georInstances.get(position).title);
                text2.setText(georInstances.get(position).url);
                return view;
            }
        }
        lv.setAdapter(aa)
        return
    }
}