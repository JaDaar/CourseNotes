package com.gnomicsoftware.coursenotes.activities;

import android.content.Intent;
import android.os.Bundle;

import com.gnomicsoftware.coursenotes.R;
import com.gnomicsoftware.coursenotes.repository.DataManager;
import com.gnomicsoftware.coursenotes.repository.NoteInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
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
					startActivity(new Intent(NoteListActivity.this, NoteActivity.class));
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
				// NoteInfo selectedFromList = (NoteInfo) listView.getItemAtPosition(position);
				
				Intent intent=new Intent(NoteListActivity.this, NoteActivity.class);
				intent.putExtra(NoteActivity.NOTE_POSITION,position);
				startActivity(intent);
			});
		}
		catch (Exception e){
			Log.e("Exception",e.getMessage());
		}
	}

}