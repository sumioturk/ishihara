package com.sumioturk.satomi.domain.message;

import com.sumioturk.satomi.domain.AsyncRepository;
import com.sumioturk.satomi.service.HttpAsyncTask;

import java.net.URL;
import java.util.List;

/**
 * Created by sumioturk on 6/16/13.
 */
public class MessageRepository implements AsyncRepository<String> {

    public MessageRepository() {
    }

    @Override
    public void resolve(String id, RepositoryAsyncCallback<String> callback) {
        return;
    }

    @Override
    public void resolveAll(String id, RepositoryAsyncCallback<List<String>> callback) {
        return;
    }

    @Override
    public void store(final String obj, final RepositoryAsyncCallback<String> callback) {
        try {
            URL url = new URL("http://sashimiquality.com:9000/event/message/5176c206e4b0e56350837a44/517787a1e4b0422dfe1136f2/" + obj);
            String method = "GET";
            HttpAsyncTask task = new HttpAsyncTask(url, method, null, new HttpAsyncTask.HttpAsyncTaskCallback() {
                @Override
                public void onSuccess(String result) {
                    callback.onEntity(obj);
                }

                @Override
                public void onFailure(Exception e) {
                    callback.onError(e);
                }
            });
            task.execute();

        } catch (Exception e) {
            callback.onError(e);
        }
    }
}
