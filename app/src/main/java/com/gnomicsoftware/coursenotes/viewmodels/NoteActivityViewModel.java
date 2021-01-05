package com.gnomicsoftware.coursenotes.viewmodels;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;

public class NoteActivityViewModel extends ViewModel {
	public static final String ORIGINAL_NOTE_COURSE_ID="com.gnomicsoftware.coursenotes.ORIGINAL_NOTE_COURSE_ID";
	public static final String ORIGINAL_NOTE_TITLE="com.gnomicsoftware.coursenotes.ORIGINAL_NOTE_TITLE";
	public static final String ORIGINAL_NOTE_DESCR="com.gnomicsoftware.coursenotes.ORIGINAL_NOTE_DESCR";
	public String mOrigCourseId;
	public String mOrigTitle;
	public String mOrigText;
	public boolean mIsNewlyCreated=true;
	
	public void saveState(Bundle outState) {
		outState.putString(ORIGINAL_NOTE_COURSE_ID,mOrigCourseId);
		outState.putString(ORIGINAL_NOTE_TITLE,mOrigTitle);
		outState.putString(ORIGINAL_NOTE_DESCR,mOrigText);
	}
	
	public void restoreState(Bundle inState){
		mOrigCourseId=inState.getString(ORIGINAL_NOTE_COURSE_ID);
		mOrigTitle=inState.getString(ORIGINAL_NOTE_TITLE);
		mOrigText=inState.getString(ORIGINAL_NOTE_DESCR);
	}
}
