package ar.edu.unc.famaf.redditreader.backend;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ar.edu.unc.famaf.redditreader.model.Listing;
import ar.edu.unc.famaf.redditreader.model.PostModel;


public class GetTopPostsTask extends AsyncTask<RedditDBHelper, Void, List<PostModel>> {

    @Override
    protected List<PostModel> doInBackground(RedditDBHelper... redditDBHelpers) {

        InputStream input;
        RedditDBHelper dbreddit = redditDBHelpers[0];

        try {
            HttpURLConnection conn = (HttpURLConnection) new URL
                    ("https://www.reddit.com/top.json?limit=2").openConnection();
            conn.setRequestMethod("GET");
            input = conn.getInputStream();
            Parser parserJson = new Parser();
            Listing listing = parserJson.readJsonStream(input);
            if (listing != null) {
                SQLiteDatabase db = dbreddit.getWritableDatabase();
                dbreddit.onUpgrade(db, 1, 2);
                for (int i = 0; i < listing.getChildren().size(); i++) {
                    ContentValues values = new ContentValues();
                    values.put(dbreddit.POST_TABLE_TITLE, listing.getChildren().get(i).getTitle());
                    values.put(dbreddit.POST_TABLE_AUTHOR, listing.getChildren().get(i).getAuthor());
                    values.put(dbreddit.POST_TABLE_DATE, listing.getChildren().get(i).getDate());
                    values.put(dbreddit.POST_TABLE_COMMENTS, listing.getChildren().get(i).getComments());
                    values.put(dbreddit.POST_TABLE_URLSTRING, listing.getChildren().get(i).getUrlString());
                    long insertId = db.insert(dbreddit.POST_TABLE, null, values);

                }

                List<PostModel> postModelList = new ArrayList<>();
                Cursor cursor = db.rawQuery("SELECT * FROM " + dbreddit.POST_TABLE, null);

                if (cursor.moveToFirst()) {
                    do {
                        PostModel postModel = new PostModel();
                        postModel.setTitle(cursor.getString(1));
                        postModel.setAuthor(cursor.getString(2));
                        postModel.setDate(cursor.getString(3));
                        postModel.setComments(cursor.getLong(4));
                        postModel.setUrlString(cursor.getString(5));
                        postModelList.add(postModel);
                    } while (cursor.moveToNext());
                }

                return postModelList;

            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}



