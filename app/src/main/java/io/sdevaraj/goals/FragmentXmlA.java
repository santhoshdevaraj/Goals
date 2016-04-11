package io.sdevaraj.goals;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sdevaraj on 4/6/16.
 */
public class FragmentXmlA extends Fragment {

    public FragmentXmlA() {

    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Boolean savedInstanceState ) {
        return inflater.inflate(R.layout.fragment_xml_a, container, false);
    }

}
