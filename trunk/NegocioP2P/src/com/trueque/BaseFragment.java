package com.trueque;

import android.app.Activity;
import android.app.Fragment;

public class BaseFragment extends Fragment {
	ChangeFragment chfragment;
	
	public interface ChangeFragment{
		public void changeFragment(int pantalla, Fragment f);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		chfragment = (ChangeFragment) activity;
	}
	
	public void changeScreen(int pantalla, Fragment f){
		chfragment.changeFragment(pantalla, f);
	}
}

