package com.gnomicsoftware.coursenotes.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.gnomicsoftware.coursenotes.R;
import com.gnomicsoftware.coursenotes.databinding.ActivityNoteBinding;
import com.gnomicsoftware.coursenotes.repository.CourseInfo;
import com.gnomicsoftware.coursenotes.repository.DataManager;
import com.gnomicsoftware.coursenotes.repository.NoteInfo;
import com.gnomicsoftware.coursenotes.viewmodels.NoteActivityViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class NoteActivity extends AppCompatActivity {
	public static final String NOTE_POSITION="com.gnomicsoftware.coursenotes.NOTE_POSITION";
	public static final int POSITION_NOT_SET = -1;
	private NoteInfo mNote;
	private boolean mIsNewNote;
	private int mNotePosition;
	private boolean mIsCancelling;
	private ArrayAdapter<CourseInfo> mAdapterCourses;
	private NoteActivityViewModel mNoteActivityViewModel;
	private ActivityNoteBinding vBind;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		vBind=ActivityNoteBinding.inflate(getLayoutInflater());
		View view=vBind.getRoot();
		setContentView(view);
		//setContentView(R.layout.activity_note);
		ViewModelProvider viewModelProvider=new ViewModelProvider(getViewModelStore(),
				ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));
		mNoteActivityViewModel=viewModelProvider.get(NoteActivityViewModel.class);
		
		
		Spinner mSpinnerCourses = findViewById(R.id.spinner_courses);
		EditText mTitleText = findViewById(R.id.editText_Title);
		EditText mDescText = findViewById(R.id.editText_Desc);
		
		//  saved state, restore it
		//if(savedInstanceState!=null && mNoteActivityViewModel.mIsNewlyCreated)
		//	mNoteActivityViewModel.restoreState(savedInstanceState);

		//mNoteActivityViewModel.mIsNewlyCreated=false;
		
		
		try {
			List<CourseInfo> courses = DataManager.getInstance().getCourses();
			mAdapterCourses = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses);
			mAdapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mSpinnerCourses.setAdapter(mAdapterCourses);
			
			readSentValues();
			//saveOriginalNote();
			
			if(mIsNewNote ==false) {
				displayNote(mSpinnerCourses, mTitleText, mDescText);
			}
		}
		catch (Exception e){
			Log.e("Exception",e.getMessage());
		}
	}
	
	@Override
	protected void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		
		if(outState!=null)
			mNoteActivityViewModel.saveState(outState);
	}
	
	private void saveOriginalNote() {
		if(mIsNewNote)
			return;
		
		mNoteActivityViewModel.mOrigCourseId = mNote.getCourse().getCourseId();
		mNoteActivityViewModel.mOrigTitle = mNote.getTitle();
		mNoteActivityViewModel.mOrigText = mNote.getText();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// captures changes to the array adapter when the activity resumes
		mAdapterCourses.notifyDataSetChanged();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(mIsCancelling){  // Cancel is selected from the menu
			if(mIsNewNote)  // Was a new note created, if so remove it from the datamanager.
				DataManager.getInstance().removeNote(mNotePosition);
			/*else
				restorePreviousNote();*/
		}else if(mNoteActivityViewModel.mIsNewlyCreated) {
			saveNote();
		}
	}
	
	private void restorePreviousNote() {
		CourseInfo course = DataManager.getInstance().getCourse(mNoteActivityViewModel.mOrigCourseId);
		mNote.setCourse(course);
		mNote.setTitle(mNoteActivityViewModel.mOrigTitle);
		mNote.setText(mNoteActivityViewModel.mOrigText);
	}
	
	private void saveNote() {
		//  These should be fields but when they are, they can be null causing a crash.
		//  Move to view binding.
		Spinner mSpinnerCourses = findViewById(R.id.spinner_courses);
		EditText mTitleText = findViewById(R.id.editText_Title);
		EditText mDescText = findViewById(R.id.editText_Desc);
		
		mNote.setCourse((CourseInfo) mSpinnerCourses.getSelectedItem());
		mNote.setTitle(mTitleText.getText().toString());
		mNote.setText(mDescText.getText().toString());
	}
	
	private void displayNote(Spinner spinnerCourses, EditText titleText, EditText descText) {
		titleText.setText(mNote.getTitle());
		descText.setText(mNote.getText());
		List<CourseInfo> courses = DataManager.getInstance().getCourses();
		int courseIndex=courses.indexOf(mNote.getCourse());
		spinnerCourses.setSelection(courseIndex);
	}
	
	// instead of sending the reference values for courses, this approach gets the note position from the datamanager.
	private void readSentValues() {
		try{
			Intent intent = getIntent();
			int position = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
			mIsNewNote = position ==POSITION_NOT_SET;
			if(mIsNewNote){
				createNote();
			}
			else{
				mNote =DataManager.getInstance().getNotes().get(position);
			}
		}
		catch (Exception e){
			Log.e("Exception",e.getMessage());
		}
	}
	
	private void createNote() {
		DataManager dm=DataManager.getInstance();
		mNotePosition =dm.createNewNote();
		mNote=dm.getNotes().get(mNotePosition);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try{
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.options, menu);
			return true;
		}
		catch (Exception e){
			Log.e("Exception",e.getMessage());
		}
		return false;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.send_email:
				sendMail();
				return true;
			case R.id.action_cancel:
				mIsCancelling = true;
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void sendMail() {
		try {
			Snackbar.make(findViewById(R.id.spinner_courses), "Sending Email", Snackbar.LENGTH_LONG)
					.setAction("Action", null).show();
			Spinner mSpinnerCourses = findViewById(R.id.spinner_courses);
			EditText mTitleText = findViewById(R.id.editText_Title);
			EditText mDescText = findViewById(R.id.editText_Desc);
			CourseInfo course = (CourseInfo) mSpinnerCourses.getSelectedItem();
			String subject = mTitleText.getText().toString();
			String body = mTitleText.getText().toString() + "\n\n" + mDescText.getText().toString();
			
			Intent intent = new Intent(Intent.ACTION_SEND);
			// MIME Type for EMAIL MESSAGES
			intent.setType("message/rfc2822");
			intent.putExtra(intent.EXTRA_SUBJECT, subject);
			intent.putExtra(intent.EXTRA_TEXT, body);
			intent.putExtra(intent.EXTRA_EMAIL, "jeffrey.hamlin@gmail.com");
			startActivity(intent);
		} catch (Exception e) {
			Log.e("Exception", e.getMessage());
		}
	}
}