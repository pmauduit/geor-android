package app.georchestra.beneth.fr.georchestra.tasks

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView

public class RetrieveImageTask extends AsyncTask<Object, Void, ArrayList<Bitmap>> {
    Throwable error
    ArrayList<Bitmap> images = new ArrayList<Bitmap>()
    ArrayList<ImageView> imageViews
    ListView listView

    HashMap<String, Bitmap> hmImages = new HashMap<String, Bitmap>()

    public RetrieveImageTask(ArrayList<ImageView> imageViews) {
            this.imageViews = imageViews
    }
    public RetrieveImageTask(ListView v) {
        this.listView = v
    }

    @Override
    protected ArrayList<Bitmap> doInBackground(Object... urls) {
            def alUrls = (ArrayList<String>) urls[0]
            alUrls.each {
                if (it == null)
                    return
                try {
                    InputStream ins = new URL(it).openStream()
                    def bm =  BitmapFactory.decodeStream(ins)
                    images << bm
                    hmImages[it] = bm
                } catch (Throwable e) {
                    error = e
                    Log.d(this.getClass().toString(), "Error parsing image " + it, e)
                    return
                }
            }
        return images
    }

    @Override
    protected void onPostExecute(ArrayList<Bitmap> result) {
        if (result == null) {
            return
        }
        if (imageViews) {
            imageViews.eachWithIndex { it, idx ->
                it.setImageBitmap(result[idx])
            }
        } else if (listView) {
            ((ArrayAdapter) listView.getAdapter()).notifyDataSetChanged()
        }
    }
}