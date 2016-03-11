package com.wahlzeit.mobile.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.wahlzeit.mobile.R;
import com.wahlzeit.mobile.asyncTasks.ListAllPhotosTask;
import com.wenchao.cardstack.CardStack;

public class ShowFragment extends Fragment {

    View rootView;
    private CardStack mCardStack;
    private CardsDataAdapter mCardAdapter;

    public ShowFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_show, container, false);

        mCardStack = (CardStack) rootView.findViewById(R.id.container);
        mCardStack.setListener(new myCardStackEventListener());

        mCardStack.setContentResource(R.layout.card_content);
        mCardStack.setStackMargin(20);
        mCardAdapter = new CardsDataAdapter(getActivity().getApplicationContext());

        getListAllPhotosTask(mCardAdapter, mCardStack).execute();

        return rootView;
    }

    private ListAllPhotosTask getListAllPhotosTask(BaseAdapter adapter, RelativeLayout container) {
        return new ListAllPhotosTask(adapter, container);
    }

}
