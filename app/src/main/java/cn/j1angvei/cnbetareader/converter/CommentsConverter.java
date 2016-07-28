package cn.j1angvei.cnbetareader.converter;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.j1angvei.cnbetareader.bean.CommentItem;
import cn.j1angvei.cnbetareader.bean.Comments;
import rx.Observable;

/**
 * convert response to comments
 * Created by Wayne on 2016/7/23.
 */
public class CommentsConverter implements Converter<Comments> {
    final private Gson mGson;

    public CommentsConverter(Gson gson) {
        mGson = gson;
    }

    @Override
    public Comments to(String json) {
        Comments comments = new Comments();
        try {
            JSONObject result = new JSONObject(json).getJSONObject("result");
            comments.setCommentNum(result.getString("comment_num"));
            comments.setJoinNum(result.getString("join_num"));
            comments.setOpen(result.getString("open").equals("1"));
            comments.setToken(result.getString("token"));
            comments.setPage(result.getString("page"));
            comments.setSid(result.getString("sid"));

            //hot comments tid list
            JSONArray hotArray = result.getJSONArray("hotlist");
            comments.setHotIds(getCommentIds(hotArray));

            //all comments tid list
            JSONArray allArray = result.getJSONArray("cmntlist");
            List<String> allIds = getCommentIds(allArray);
            comments.setAllIds(getCommentIds(allArray));

            //comments map
            JSONObject store = result.getJSONObject("cmntstore");
            Map<String, CommentItem> map = new HashMap<>();
            for (String tid : allIds) {
                CommentItem item = mGson.fromJson(store.getJSONObject(tid).toString(), CommentItem.class);
                map.put(tid, item);
            }
            comments.setCommentMap(map);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return comments;
    }

    @Override
    public List<Comments> toList(String json) {
        return null;
    }

    @Override
    public Observable<Comments> toObservable(String json) {
        return Observable.just(to(json));
    }

    private List<String> getCommentIds(JSONArray array) throws JSONException {
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            ids.add(object.getString("tid"));
        }
        return ids;
    }
}