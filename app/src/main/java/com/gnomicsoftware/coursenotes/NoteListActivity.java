package com.gnomicsoftware.coursenotes;

import android.content.Intent;
import android.os.Bundle;

import com.gnomicsoftware.coursenotes.repository.DataManager;
import com.gnomicsoftware.coursenotes.repository.NoteInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class NoteListActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_note_list);
			Toolbar toolbar = findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);
			
			FloatingActionButton fab = findViewById(R.id.fab);
			fab.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					startActivity(new Intent(NoteListActivity.this,NoteActivity.class));
				}
			});
			
			initializeListContent();
		} catch (Exception e) {
		
		}
	}
	
	private void initializeListContent() {

		
		try{
			ListView listView = findViewById(R.id.list_notes);
			List<NoteInfo> notes= DataManager.getInstance().getNotes();
			ArrayAdapter<NoteInfo> adapterNotes=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,notes);
			listView.setAdapter(adapterNotes);
			
			listView.setOnItemClickListener((parent, view, position, id) -> {
				NoteInfo selectedFromList = (NoteInfo) listView.getItemAtPosition(position);
				
				/*Snackbar.make(findViewById(R.id.list_notes),selectedFromList, Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();*/
				
				Intent intent=new Intent(NoteListActivity.this, NoteActivity.class);
				intent.putExtra(NoteActivity.NOTE_INFO,selectedFromList);
				startActivity(intent);
			});
		}
		catch (Exception e){
			Log.e("Exception",e.getMessage());
		}
	}

}