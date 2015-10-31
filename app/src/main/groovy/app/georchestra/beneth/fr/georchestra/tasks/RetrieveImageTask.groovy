package app.georchestra.beneth.fr.georchestra.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView

public class RetrieveImageTask extends AsyncTask<Object, Void, Bitmap> {
    ImageView imageView
    Throwable error

    public RetrieveImageTask(ImageView imageView) {
        this.imageView = imageView
    }

    @Override
    protected Bitmap doInBackground(Object... urls) {
        String url = (String) urls[0]
        Bitmap icon = null
        try {
            InputStream ins = new java.net.URL(url).openStream()
            icon = BitmapFactory.decodeStream(ins)
        } catch (Throwable e) {
            Log.e(RetrieveImageTask.class.toString(), e.getMessage())
            error = e
        }
        return icon
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result)
    }
}