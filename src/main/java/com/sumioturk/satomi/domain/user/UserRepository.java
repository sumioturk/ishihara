package com.sumioturk.satomi.domain.user;

import com.sumioturk.satomi.domain.AsyncRepository;
import com.sumioturk.satomi.infrastructure.converter.user.UserJsonConverter;
import com.sumioturk.satomi.service.HttpAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sumioturk on 6/16/13.
 */
public class UserRepository implements AsyncRepository<User> {

    private HttpAsyncTask task;

    private List<User> users;

    private UserJsonConverter converter;

    public UserRepository() {
        this.users = new ArrayList<User>();
        this.converter = new UserJsonConverter();
    }


    @Override
    public void resolve(String id, final RepositoryAsyncCallback<User> callback) {
        try {
            URL url = new URL("http://sashimiquality.com:9000/user/" + id);
            String method = "GET";
            HttpAsyncTask task = new HttpAsyncTask(url, method, null, new HttpAsyncTask.HttpAsyncTaskCallback() {
                @Override
                public void onSuccess(String result) {
                    callback.onEntity(converter.fromJsonString(result));
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

    @Override
    public void resolveAll(final RepositoryAsyncCallback<List<User>> callback) {
        try {
            URL url = new URL("http://sashimiquality.com:9000/users");
            String method = "GET";
            HttpAsyncTask task = new HttpAsyncTask(url, method, null, new HttpAsyncTask.HttpAsyncTaskCallback() {
                @Override
                public void onSuccess(String result) {
                    try {
                        users.addAll(converter.fromJsonArray(new JSONArray(result)));
                        callback.onEntity(users);
                    } catch (JSONException e) {
                        callback.onError(e);
                    }
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

    @Override
    public void store(final User obj, final RepositoryAsyncCallback<User> callback) {
        try {
            String name = URLEncoder.encode(obj.getName(), "UTF-8");
            URL url = new URL(String.format("http://sashimiquality.com:9000/user?name=%s&isGay=%s", name, obj.isGay()));
            String method = "POST";
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
