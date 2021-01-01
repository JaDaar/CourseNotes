package com.gnomicsoftware.coursenotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.gnomicsoftware.coursenotes.repository.CourseInfo;
import com.gnomicsoftware.coursenotes.repository.DataManager;
import com.gnomicsoftware.coursenotes.repository.NoteInfo;

import java.util.List;

public class NoteActivity extends AppCompatActivity {
	public static final String NOTE_INFO="com.gnomicsoftware.coursenotes.NOTE_INFO";
	private NoteInfo mNoteSent;
	private boolean mIsNewNote;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note);
		
		try {
			Spinner spinnerCourses=findViewById(R.id.spinner_courses);
			List<CourseInfo> courses = DataManager.getInstance().getCourses();
			ArrayAdapter<CourseInfo> adapterCourses = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses);
			adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerCourses.setAdapter(adapterCourses);
			
			readSentValues();
			
			EditText titleText=findViewById(R.id.editText_Title);
			EditText descText=findViewById(R.id.editText_Desc);
			
			if(mIsNewNote ==false) {
				displayNote(spinnerCourses, titleText, descText);
			}
		}
		catch (Exception e){
			Log.e("Exception",e.getMessage());
		}
	}
	
	private void displayNote(Spinner spinnerCourses, EditText titleText, EditText descText) {
		titleText.setText(mNoteSent.getTitle());
		descText.setText(mNoteSent.getText());
		List<CourseInfo> courses = DataManager.getInstance().getCourses();
		int courseIndex=courses.indexOf(mNoteSent.getCourse());
		spinnerCourses.setSelection(courseIndex);
	}
	
	private void readSentValues() {
		Intent intent = getIntent();
		mNoteSent = intent.getParcelableExtra(NOTE_INFO);
		mIsNewNote = mNoteSent==null;
	}
}