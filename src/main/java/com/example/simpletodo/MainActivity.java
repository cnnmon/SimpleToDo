package com.example.simpletodo;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.io.FileUtils.readLines;
import static org.apache.commons.io.FileUtils.writeLines;

public class MainActivity extends AppCompatActivity implements EditDialog.EditDialogListener {

    List<String> items;
    Button btnAdd;
    EditText etItem;
    EditText etTask;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;
    int activeTask = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize elements
        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        etTask = findViewById(R.id.etTask);
        rvItems = findViewById(R.id.rvItems);
        loadItems();

        // set on long click listener on list (remove)
        ItemsAdapter.OnLongClickListener onLongClickListener = position -> {
            items.remove(position);
            itemsAdapter.notifyItemRemoved(position);
            Toast.makeText(getApplicationContext(), "task removed!", Toast.LENGTH_SHORT).show();
            saveItems();
        };

        ItemsAdapter.OnClickListener onClickListener = position -> {
            activeTask = position;
            openDialog();
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        // set on click listener on button (add)
        btnAdd.setOnClickListener(v -> {
            String todoItem = etItem.getText().toString();
            items.add(todoItem);
            itemsAdapter.notifyItemInserted(items.size() - 1);
            etItem.setText("");
            Toast.makeText(getApplicationContext(), "task added!", Toast.LENGTH_SHORT).show();
            saveItems();
        });
    }

    // EDIT DIALOG
    private void openDialog() {
        EditDialog editDialog = new EditDialog();
        editDialog.show(getSupportFragmentManager(), "edit dialog");
    }

    // PERSISTENCE

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    private void loadItems() {
        try {
            items = new ArrayList<String>(readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            // if no existing data

            items = new ArrayList<>();
        }
    }

    private void saveItems() {
        try {
            writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }

    @Override
    public void applyEdit(String newTask) {
        items.set(activeTask, newTask);
        itemsAdapter.notifyItemChanged(activeTask);
        Toast.makeText(getApplicationContext(), "task edited!", Toast.LENGTH_SHORT).show();
        saveItems();
    }
}