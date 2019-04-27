package cecs343.pollio;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Requestor {

    private static Requestor instance; // Does not cause memory leak as long as you always pass getApplicationContext (Not an activity's context)
    private RequestQueue requestQueue;
    private static Context ctx; // Same as above

    private Requestor(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();

    }

    // Passed context MUST be applicationContext, not activity
    public static synchronized Requestor getInstance(Context context) {
        if (instance == null) {
            instance = new Requestor(context);
        }
        return instance;
    }

    public static void postRequest(final Context context, final String path, FirebaseUser user, final HashMap<String, String> args) {

        user.getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
            @Override
            public void onSuccess(GetTokenResult result) {
                final String idToken = result.getToken();

                String url = "http://polls.lorenzen.dev/" + path;


                StringRequest stringRequest = new StringRequest
                        (Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("JSON", response);
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error
                                Log.d("JSON", error.getMessage());

                            }
                        })
                {
                    @Override
                    public Map<String, String> getParams() {
                        args.put("token", idToken);
                        return args;
                    }
                };
                getInstance(context).addToRequestQueue(stringRequest);

            }
        });


    }

    public static ArrayList<PollItem> getHotPolls(Context context, String uid) {
        final ArrayList<PollItem> newPolls = new ArrayList<>();

        String url = "http://polls.lorenzen.dev/new?uid=" + uid;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("JSON", response.toString());
                        try{
                            for (int i = 0, size = response.length(); i < size; i++) {
                                JSONObject jsonPoll = response.getJSONObject(i);
                                PollItem poll = new PollItem(jsonPoll.get("title").toString(), (jsonPoll.get("favorited").equals("true")), Integer.parseInt(jsonPoll.get("pollID").toString()));
                                JSONArray options = jsonPoll.getJSONArray("options");
                                JSONArray votes = jsonPoll.getJSONArray("votes");
                                for(int j = 0; j < options.length(); j++)
                                {
                                    poll.addPollOption(new PollOption(options.getString(j), Integer.parseInt(votes.getString(j))));
                                }
                                poll.checkVoted(jsonPoll.get("voted").toString());
                                newPolls.add(poll);
                            }
                        }
                        catch (Exception e){
                            Log.d("JSON", "JSON EXCEPTION: " + e.getMessage());
                            for (StackTraceElement s : e.getStackTrace()) {
                                Log.d("JSON", s.toString());
                            }
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                })
        {
//
//            @Override
//            public Map<String, String> getParams() {
//                HashMap<String, String> params = new HashMap<>();
//                params.put("uid", uid);
//                return params;
//            }
        };
        getInstance(context).addToRequestQueue(jsonArrayRequest);

        return newPolls;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
