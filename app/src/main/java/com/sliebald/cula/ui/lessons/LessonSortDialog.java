package com.sliebald.cula.ui.lessons;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import com.sliebald.cula.R;
import com.sliebald.cula.utilities.SortUtils;

/**
 * Sort {@link DialogFragment} for sorting lesson entries.
 */
public class LessonSortDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        // inflate the dialog
        View view = inflater.inflate(R.layout.dialog_sort_lessons, null);

        // get the arguments to set the currently selected values.
        Bundle args = getArguments();

        RadioGroup rg = view.findViewById(R.id.radio_sort);
        SwitchCompat sw = view.findViewById(R.id.switch_order);
        TextView textOrder = view.findViewById(R.id.tv_asc);
        RadioButton rb;
        switch (SortUtils.SortType.valueOf(args.getString(SortUtils.KEY_ACTIVE_SORT_BY))) {
            case ID:
                rb = view.findViewById(R.id.radio_sort_created);
                break;
            default:
                rb = view.findViewById(R.id.radio_sort_name);
        }
        rb.toggle();

        boolean asc = getArguments().getBoolean(SortUtils.KEY_ACTIVE_SORT_ORDER);
        if (asc) {
            textOrder.setText(R.string.sort_asc);
        } else {
            textOrder.setText(R.string.sort_desc);
        }
        sw.setChecked(asc);
        // set an OnClickListener to dynamically adapt the text to the selected order.
        sw.setOnClickListener(v -> {
            if (((SwitchCompat) v).isChecked()) {
                textOrder.setText(R.string.sort_asc);
            } else {
                textOrder.setText(R.string.sort_desc);
            }
        });

        builder.setView(view).setPositiveButton(R.string.sort, (dialog, id) -> {
            // read the selected values and report them via callback
            SortUtils.SortType type;
            switch (rg.getCheckedRadioButtonId()) {
                case R.id.radio_sort_created:
                    type = SortUtils.SortType.ID;
                    break;
                default:
                    type = SortUtils.SortType.NAME;
                    break;
            }

            SortUtils.OnSortChangedListener listener = (SortUtils.OnSortChangedListener) getTargetFragment();
            if (listener != null) {
                listener.onUpdateSortOrderClick(type, sw.isChecked());
            }
        })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                });

        return builder.create();
    }

}
