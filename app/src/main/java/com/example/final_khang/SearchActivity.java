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
import com.example.final_khang.dao.SearchDAO;

import java.util.List;
public class SearchActivity extends AppCompatActivity {
    private SearchView searchView;
    private String searchMode = "both"; // default to search both
    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
    private SearchDAO searchDAO;
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
        searchDAO = new SearchDAO(this);
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
            }
             else
                 if ( item.getItemId()==R.id.action_search_post) {
                searchMode = "post";
                Toast.makeText(this, "Search mode: Post", Toast.LENGTH_SHORT).show();
                return true;
            }
            else
                return super.onOptionsItemSelected(item);
        }

    private void performSearch(String query) {
        if (query.isEmpty()) {
            // Clear the search results
            searchAdapter.setResults(null);
            Toast.makeText(this, "Search query is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        List<?> results;
        switch (searchMode) {
            case "user":
                results = searchDAO.searchUsers(query);
                break;
            case "post":
                results = searchDAO.searchPosts(query);
                break;
            default:
                results = searchDAO.searchBoth(query);
                break;
        }
        if (results != null && !results.isEmpty()) {
            searchAdapter.setResults(results);
        } else {
            Toast.makeText(this, "No results found for: " + query, Toast.LENGTH_SHORT).show();
        }
    }
}
