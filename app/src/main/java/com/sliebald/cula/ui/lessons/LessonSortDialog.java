package com.sliebald.cula.ui.lessons;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
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
        // inflate the dialog
        View view = View.inflate(requireContext(), R.layout.dialog_sort_lessons, null);

        // get the arguments to set the currently selected values.
        Bundle args = getArguments();

        RadioGroup rgSortBy = view.findViewById(R.id.radio_sort);
        SwitchCompat swSortAscending = view.findViewById(R.id.switch_order);
        TextView textOrder = view.findViewById(R.id.tv_asc);
        RadioButton rbActive = args != null && SortUtils.SortType.valueOf(args.getString(SortUtils.KEY_ACTIVE_SORT_BY)) == SortUtils.SortType.ID
                ? view.findViewById(R.id.radio_sort_created) : view.findViewById(R.id.radio_sort_name);
        rbActive.toggle();

        boolean asc = args != null && args.getBoolean(SortUtils.KEY_ACTIVE_SORT_ORDER);
        textOrder.setText(asc ? R.string.sort_asc : R.string.sort_desc);
        swSortAscending.setChecked(asc);
        // set an OnClickListener to dynamically adapt the text to the selected order.
        swSortAscending.setOnClickListener(v -> textOrder.setText(((SwitchCompat) v).isChecked() ? R.string.sort_asc : R.string.sort_desc));

        builder.setView(view).setPositiveButton(R.string.sort, (dialog, id) -> {
            // read the selected values and report them via callback
            SortUtils.SortType type = rgSortBy.getCheckedRadioButtonId() == R.id.radio_sort_created ? SortUtils.SortType.ID : SortUtils.SortType.NAME;

            SortUtils.OnSortChangedListener listener = (SortUtils.OnSortChangedListener) getTargetFragment();
            if (listener != null) {
                listener.onUpdateSortOrderClick(type, swSortAscending.isChecked());
            }
        }).setNegativeButton(R.string.cancel, (dialog, id) -> {
        });

        return builder.create();
    }

}
