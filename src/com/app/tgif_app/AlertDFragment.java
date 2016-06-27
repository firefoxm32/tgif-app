package com.app.tgif_app;

import android.app.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.MenuItem;

public class AlertDFragment extends DialogFragment {
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		return new AlertDialog.Builder(getActivity())
				// Set Dialog Icon
				//.setIcon(R.drawable.logo)
				// Set Dialog Title
				.setTitle("Alert DialogFragment")
				// Set Dialog Message
				.setMessage("Alert DialogFragment Tutorial")
 
				// Positive button
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Do something else
					}
				})
 
				// Negative Button
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,	int which) {
						// Do something else
					}
				}).create();
	}
}
