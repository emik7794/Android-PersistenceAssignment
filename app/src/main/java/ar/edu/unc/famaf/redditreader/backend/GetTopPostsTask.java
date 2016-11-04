package ar.edu.unc.famaf.redditreader.backend;


import android.os.AsyncTask;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import ar.edu.unc.famaf.redditreader.model.Listing;
import ar.edu.unc.famaf.redditreader.model.PostModel;


public class GetTopPostsTask extends AsyncTask<Void, Void, List<PostModel>> {

    @Override
    protected List<PostModel> doInBackground(Void... params) {

        InputStream input;

        try {
            HttpURLConnection conn = (HttpURLConnection) new URL
                    ("https://www.reddit.com/top.json?limit=50").openConnection();
            conn.setRequestMethod("GET");
            input = conn.getInputStream();
            Parser parserJson = new Parser();
            Listing listing = parserJson.readJsonStream(input);
            if (listing != null) {
                return listing.getChildren();
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}



