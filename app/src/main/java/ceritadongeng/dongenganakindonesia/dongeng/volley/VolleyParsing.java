package ceritadongeng.dongenganakindonesia.dongeng.volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import ceritadongeng.dongenganakindonesia.dongeng.model.Cerita;
import ceritadongeng.dongenganakindonesia.dongeng.model.CeritaWrapper;

/**
 * Created by asywalulfikri on 12/9/16.
 */

public class VolleyParsing {

    public static CeritaWrapper ceritaParsing(String response) throws Exception {
        CeritaWrapper wrapper = null;
        JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();

        try {
            JSONArray dataJson = jsonObject.getJSONArray("data");
            int size = dataJson.length();

            if (size > 0) {
                wrapper = new CeritaWrapper();

                wrapper.list = new ArrayList<Cerita>();

                for (int i = 0; i < size; i++) {
                    JSONObject itemJson = dataJson.getJSONObject(i);

                    Cerita cerita   = new Cerita();
                    cerita.id         = (!itemJson.isNull("id"))      ? itemJson.getString("id") : "";
                    cerita.title      = (!itemJson.isNull("title"))   ? itemJson.getString("title") : "";
                    cerita.content    = (!itemJson.isNull("content")) ? itemJson.getString("content") : "";
                    cerita.createdAt  = (!itemJson.isNull("createdAt")) ? itemJson.getString("createdAt") : "";
                    cerita.type       = (!itemJson.isNull("type"))    ? itemJson.getString("type") : "";
                    cerita.image1     = (!itemJson.isNull("image1"))  ? itemJson.getString("image1") : "";
                    cerita.image2     = (!itemJson.isNull("image2"))  ? itemJson.getString("image2") : "";
                    cerita.image3     = (!itemJson.isNull("image3"))  ? itemJson.getString("image3") : "";
                    cerita.sumber     = (!itemJson.isNull("sumber"))  ? itemJson.getString("sumber") : "";
                    cerita.category   = (!itemJson.isNull("category"))  ? itemJson.getString("category") : "";
                    cerita.languange  = (!itemJson.isNull("languange"))  ? itemJson.getString("languange") : "";


                    wrapper.list.add(cerita);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();

            throw e;
        }

        return wrapper;
    }

}
