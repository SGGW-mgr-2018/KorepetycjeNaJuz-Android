package pl.dawidkulpa.serverconnectionmanager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
    private ImageView imageView;

    public ImageDownloader(ImageView imageView){
        this.imageView= imageView;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap bm;

        try{
            InputStream is= new URL(strings[0]).openStream();
            bm= BitmapFactory.decodeStream(is);
        } catch (IOException e){
            return null;
        }

        return bm;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}
