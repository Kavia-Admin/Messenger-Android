package org.mesibo.messenger;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Advanced search activity for messages, users, or groups.
 * PUBLIC_INTERFACE
 */
public class SearchActivity extends AppCompatActivity {

    private EditText queryEditText;
    private ImageButton searchButton;
    private Spinner typeSpinner;
    private ListView resultsListView;

    private ArrayAdapter<String> resultAdapter;
    private List<String> resultList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        queryEditText = findViewById(R.id.edit_search_query);
        searchButton = findViewById(R.id.button_search);
        typeSpinner = findViewById(R.id.spinner_search_type);
        resultsListView = findViewById(R.id.list_search_results);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.search_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        resultAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, resultList);
        resultsListView.setAdapter(resultAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performSearch();
            }
        });
    }

    private void performSearch() {
        String query = queryEditText.getText().toString().trim();
        String type = typeSpinner.getSelectedItem().toString();

        if(query.isEmpty()) {
            Toast.makeText(this, "Please enter search text", Toast.LENGTH_SHORT).show();
            return;
        }

        new SearchTask(type, query).execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // AsyncTask for API search
    private class SearchTask extends AsyncTask<Void, Void, List<String>> {
        private String type, query;

        SearchTask(String type, String query){
            this.type = type.toLowerCase();
            this.query = query;
        }

        @Override
        protected List<String> doInBackground(Void... voids) {
            try {
                String apiUrl = "";
                if(type.startsWith("message"))
                    apiUrl = "https://YOUR_BACKEND_URL/?api=search_messages&query="+query;
                else if(type.startsWith("user"))
                    apiUrl = "https://YOUR_BACKEND_URL/?api=search_users&query="+query;
                else
                    apiUrl = "https://YOUR_BACKEND_URL/?api=search_groups&query="+query;

                java.net.URL url = new java.net.URL(apiUrl);
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                int status = conn.getResponseCode();
                if(status == 200) {
                    java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();
                    JSONObject res = new JSONObject(sb.toString());
                    List<String> results = new ArrayList<>();
                    if(type.startsWith("message")) {
                        JSONArray arr = res.optJSONArray("messages");
                        if(arr != null) {
                            for(int i=0; i<arr.length(); i++) {
                                JSONObject msg = arr.getJSONObject(i);
                                results.add("[Msg] " + msg.optString("message", "") + " (" + msg.optString("from_user","") + ")");
                            }
                        }
                    }
                    else if(type.startsWith("user")){
                        JSONArray arr = res.optJSONArray("users");
                        if(arr != null) {
                            for(int i=0; i<arr.length(); i++) {
                                JSONObject user = arr.getJSONObject(i);
                                results.add("[User] " + user.optString("name","") + " / " + user.optString("phone",""));
                            }
                        }
                    }
                    else{
                        JSONArray arr = res.optJSONArray("groups");
                        if(arr != null) {
                            for(int i=0; i<arr.length(); i++) {
                                JSONObject group = arr.getJSONObject(i);
                                results.add("[Group] " + group.optString("name","") + " (ID: "+group.optString("id","")+")");
                            }
                        }
                    }
                    return results;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> results) {
            resultList.clear();
            if(results != null && results.size() > 0){
                resultList.addAll(results);
            } else {
                resultList.add("No results found");
            }
            resultAdapter.notifyDataSetChanged();
        }
    }
}
