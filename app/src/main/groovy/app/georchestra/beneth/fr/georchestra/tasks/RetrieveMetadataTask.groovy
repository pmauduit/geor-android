package app.georchestra.beneth.fr.georchestra.tasks

import android.os.AsyncTask
import android.widget.Toast
import app.georchestra.beneth.fr.georchestra.activities.MetadataActivity
import fr.beneth.cswlib.metadata.Metadata

/**
 * Created by pmauduit on 11/7/15.
 */
public class RetrieveMetadataTask extends AsyncTask<Object, Void, Metadata> {
    Throwable error = null
    MetadataActivity activity


    public RetrieveMetadataTask(MetadataActivity activity) {
        this.activity = activity
    }

    @Override
    protected Metadata doInBackground(Object... params) {
        try {
            def mdUrl = params[0]
            def metadata = Metadata.mapFromXmlDocument(mdUrl.toString())
            return metadata
        } catch (Throwable e) {
            error = e
        }
        return null
    }

    @Override
    protected void onPostExecute(Metadata result) {
        if ((result == null) && (error)) {
            Toast.makeText(activity.getApplicationContext(), error.getMessage(),
                    Toast.LENGTH_LONG).show()
        }
        activity.updateInterface(result)
    }
}
