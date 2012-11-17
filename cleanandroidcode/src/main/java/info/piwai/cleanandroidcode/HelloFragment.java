package info.piwai.cleanandroidcode;

import static java.lang.System.currentTimeMillis;
import info.piwai.cleanandroidcode.base.BaseFragment;

import javax.inject.Inject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.squareup.otto.Bus;

/**
 * TODO use support fragments
 */
@SuppressLint("NewApi")
public class HelloFragment extends BaseFragment {

	@Inject
	Bus bus;

	View fragmentButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.hello_fragment, container, false);

		fragmentButton = contentView.findViewById(R.id.fragment_button);
		fragmentButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				bus.post(new UpdateTitleEvent("Hey: " + currentTimeMillis()));
			}
		});

		return contentView;
	}
}
