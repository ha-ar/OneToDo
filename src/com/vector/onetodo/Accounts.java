package com.vector.onetodo;

import java.io.File;

import uk.me.lewisdeane.ldialogs.BaseDialog.Alignment;
import uk.me.lewisdeane.ldialogs.CustomDialog;
import uk.me.lewisdeane.ldialogs.CustomListDialog;
import uk.me.lewisdeane.ldialogs.CustomDialog.ClickListener;
import uk.me.lewisdeane.ldialogs.CustomListDialog.ListClickListener;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.vector.onetodo.utils.Constants;
import com.vector.onetodo.utils.TypeFaces;
import com.vector.onetodo.utils.Utils;

import eu.inmite.android.lib.dialogs.SimpleDialogFragment;

public class Accounts extends Fragment {

	AQuery aq;private Uri imageUri;
	File photo;
	private static final int TAKE_PICTURE = 1;
	public static final int RESULT_GALLERY = 0;
	String[] items = {"Camera","Gallery"};
	CircularImageView imageEvent;
	CustomDialog.Builder dialogbuilder;CustomDialog onetodopro,changenumber,emailchange;
	CustomListDialog.Builder listbuilder;CustomListDialog select_image;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.account, container, false);
		imageEvent = (CircularImageView) view.findViewById(R.id.image_event);
		aq = new AQuery(getActivity(), view);
		setFont();
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		aq.id(R.id.account_back).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getFragmentManager().popBackStack();
			}
		});
		aq.id(R.id.image_event).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listbuilder = new CustomListDialog.Builder(getActivity(), "Select Image",items);
				
				listbuilder.darkTheme(false);		
				listbuilder.typeface(TypeFaces.get(getActivity(), Constants.ROMAN_TYPEFACE));
				listbuilder.titleAlignment(Alignment.LEFT); 
				listbuilder.itemAlignment(Alignment.LEFT); 
				listbuilder.titleColor(getResources().getColor(android.R.color.holo_blue_dark)); 
				listbuilder.itemColor(Color.BLACK);
				listbuilder.titleTextSize(22);
				listbuilder.itemTextSize(18);
				select_image = listbuilder.build();
				select_image.show();
				select_image.setListClickListener(new ListClickListener() {
					
					@Override
					public void onListItemSelected(int position, String[] items, String item) {
						if(position==0)
						{
							select_image.dismiss();
							Intent intent = new Intent(
									"android.media.action.IMAGE_CAPTURE");

							String path = Environment.getExternalStorageDirectory()
									.toString();
							File makeDirectory = new File(path + File.separator
									+ "OneTodo");
							makeDirectory.mkdir();
							photo = new File(Environment
									.getExternalStorageDirectory()
									+ File.separator
									+ "OneToDo" + File.separator, "OneToDo_"
									+ System.currentTimeMillis() + ".JPG");
							intent.putExtra(MediaStore.EXTRA_OUTPUT,
									Uri.fromFile(photo));
							imageUri = Uri.fromFile(photo);
							startActivityForResult(intent, TAKE_PICTURE);
						}
						if(position==1)
						{
							select_image.dismiss();
							Intent galleryIntent = new Intent(
									Intent.ACTION_PICK,
									android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
							startActivityForResult(galleryIntent, RESULT_GALLERY);
						}
					}
				});				

			}
		});
		aq.id(R.id.timeformat_lay).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				dialogbuilder = new CustomDialog.Builder(getActivity(), "ONEtodo", "buy pro");
        		dialogbuilder.content("You are using the trial version with all the pro-feature.You can carry on basic features free of cost when trial ends.You can buy pro version anytime.");
        		dialogbuilder.negativeText("OK");
        		dialogbuilder.contentAlignment(Alignment.LEFT);
        		dialogbuilder.darkTheme(false);
        		dialogbuilder.titleTextSize(22);
        		dialogbuilder.contentTextSize(18);
        		dialogbuilder.buttonTextSize(14);
        		dialogbuilder.titleAlignment(Alignment.LEFT); 
        		dialogbuilder.buttonAlignment(Alignment.CENTER);
        		dialogbuilder.titleColor(getResources().getColor(android.R.color.holo_blue_light)); 
        		dialogbuilder.contentColor(Color.BLACK); 
        		dialogbuilder.positiveColor(getResources().getColor(android.R.color.holo_blue_light)); 
        		onetodopro = dialogbuilder.build();
        		onetodopro.show();
        		onetodopro.setClickListener(new ClickListener() {
					
					@Override
					public void onConfirmClick() {
						// TODO Auto-generated method stub
						onetodopro.dismiss();
					}
					
					@Override
					public void onCancelClick() {
						// TODO Auto-generated method stub
						onetodopro.dismiss();
					}
				});
			}
	});
		aq.id(R.id.phone_layout).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogbuilder = new CustomDialog.Builder(getActivity(), "Phone Number", "OK");
        		dialogbuilder.content("You are using the trial version with all the pro-feature.You can carry on basic features free of cost when trial ends.You can buy pro version anytime.");
        		dialogbuilder.negativeText("CHANGE NUMBER");
        		dialogbuilder.contentAlignment(Alignment.LEFT);
        		dialogbuilder.darkTheme(false);
        		dialogbuilder.titleTextSize(22);
        		dialogbuilder.contentTextSize(18);
        		dialogbuilder.buttonTextSize(14);
        		dialogbuilder.titleAlignment(Alignment.LEFT); 
        		dialogbuilder.buttonAlignment(Alignment.RIGHT);
        		dialogbuilder.titleColor(getResources().getColor(android.R.color.holo_blue_light)); 
        		dialogbuilder.contentColor(Color.BLACK); 
        		dialogbuilder.positiveColor(getResources().getColor(android.R.color.holo_blue_light)); 
        		changenumber = dialogbuilder.build();
        		changenumber.show();
        		changenumber.setClickListener(new ClickListener() {
					
					@Override
					public void onConfirmClick() {
						// TODO Auto-generated method stub
						changenumber.dismiss();
					}
					
					@Override
					public void onCancelClick() {
						// TODO Auto-generated method stub
						changenumber.dismiss();
					}
				});
			}
		});
		aq.id(R.id.dateformat_lay).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogbuilder = new CustomDialog.Builder(getActivity(), "Email", "OK");
        		dialogbuilder.content("You can reminder alerts on this emial.You can change this email anytime.");
        		dialogbuilder.negativeText("CHANGE EMAIL");
        		dialogbuilder.contentAlignment(Alignment.LEFT);
        		dialogbuilder.darkTheme(false);
        		dialogbuilder.titleTextSize(22);
        		dialogbuilder.contentTextSize(18);
        		dialogbuilder.buttonTextSize(14);
        		dialogbuilder.titleAlignment(Alignment.LEFT); 
        		dialogbuilder.buttonAlignment(Alignment.RIGHT);
        		dialogbuilder.titleColor(getResources().getColor(android.R.color.holo_blue_light)); 
        		dialogbuilder.contentColor(Color.BLACK); 
        		dialogbuilder.positiveColor(getResources().getColor(android.R.color.holo_blue_light)); 
        		emailchange = dialogbuilder.build();
        		emailchange.show();
        		emailchange.setClickListener(new ClickListener() {
					
					@Override
					public void onConfirmClick() {
						// TODO Auto-generated method stub
						emailchange.dismiss();
					}
					
					@Override
					public void onCancelClick() {
						// TODO Auto-generated method stub
						emailchange.dismiss();
					}
				});
			}
		});
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {

		case TAKE_PICTURE:

			if (resultCode == Activity.RESULT_OK){
				Picasso.with(getActivity()).load(imageUri).resize(100, 100)
				.into(imageEvent);
				aq.id(R.id.image_event).background(R.drawable.round_photobutton);
			}
		case RESULT_GALLERY:
			if (null != data) {
				Uri selectedImage = data.getData();
				Picasso.with(getActivity()).load(selectedImage).resize(100, 100)
				.into(imageEvent);
				aq.id(R.id.image_event).background(R.drawable.round_photobutton);
			}
			break;
		default:
			break;
		}
	}
	public void setFont() {
		Utils.RobotoRegular(getActivity(), aq.id(R.id.account).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.accounts).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.email).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.onetodo).getTextView());
		
		Utils.RobotoRegular(getActivity(), aq.id(R.id.mynumber).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.personal).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.name).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.display).getTextView());

		Utils.RobotoMedium(getActivity(), aq.id(R.id.email1).getTextView());
		Utils.RobotoMedium(getActivity(), aq.id(R.id.onetodo1).getTextView());
		Utils.RobotoMedium(getActivity(), aq.id(R.id.mynumber1).getTextView());
		Utils.RobotoMedium(getActivity(), aq.id(R.id.name1).getTextView());
		Utils.RobotoMedium(getActivity(), aq.id(R.id.display1).getTextView());
	}

}
