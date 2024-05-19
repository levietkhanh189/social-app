package com.example.final_khang;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_khang.adapter.SearchAdapter;
import com.example.final_khang.data.DatabaseHelper;
import com.example.final_khang.entity.Post;
import com.example.final_khang.entity.User;

import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private SearchView searchView;
    private String searchMode = "both"; // default to search both
    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search); // Ensure this matches your XML layout file name
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchAdapter = new SearchAdapter();
        recyclerView.setAdapter(searchAdapter);
        databaseHelper = new DatabaseHelper(this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search_user) {
            searchMode = "user";
            Toast.makeText(this, "Search mode: User", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.action_search_post) {
            searchMode = "post";
            Toast.makeText(this, "Search mode: Post", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void performSearch(String query) {
        if (query.isEmpty()) {
            // Clear the search results
            searchAdapter.setResults(null);
            Toast.makeText(this, "Search query is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (searchMode) {
            case "user":
                databaseHelper.searchUsers(query, new DatabaseHelper.UserListCallback() {
                    @Override
                    public void onSuccess(List<User> users) {
                        if (users != null && !users.isEmpty()) {
                            searchAdapter.setResults(users);
                        } else {
                            Toast.makeText(SearchActivity.this, "No results found for: " + query, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(SearchActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case "post":
                databaseHelper.searchPosts(query, new DatabaseHelper.PostListCallback() {
                    @Override
                    public void onSuccess(List<Post> posts) {
                        if (posts != null && !posts.isEmpty()) {
                            searchAdapter.setResults(posts);
                        } else {
                            Toast.makeText(SearchActivity.this, "No results found for: " + query, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(SearchActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            default:
                databaseHelper.searchBoth(query, new DatabaseHelper.SearchCallback() {
                    @Override
                    public void onSuccess(List<Object> results) {
                        if (results != null && !results.isEmpty()) {
                            searchAdapter.setResults(results);
                        } else {
                            Toast.makeText(SearchActivity.this, "No results found for: " + query, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(SearchActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }
}
