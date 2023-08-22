package com.sliebald.cula.ui.updateLesson;

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
 * Sort {@link DialogFragment} for sorting library entries for a lesson.
 */
public class UpdateLessonSortDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // inflate the dialog
        View view = View.inflate(requireContext(), R.layout.dialog_sort_lesson_content, null);

        // get the arguments to set the currently selected values.
        Bundle args = getArguments();

        RadioGroup rg = view.findViewById(R.id.radio_sort);
        SwitchCompat sw = view.findViewById(R.id.switch_order);
        TextView textOrder = view.findViewById(R.id.tv_asc);
        RadioButton rb;
        assert args != null;
        switch (SortUtils.SortType.valueOf(args.getString(SortUtils.KEY_ACTIVE_SORT_BY))) {
            case PART_OF_LESSON:
                rb = view.findViewById(R.id.radio_sort_part_of_lesson);
                break;
            case FOREIGN_WORD:
                rb = view.findViewById(R.id.radio_sort_foreign);
                break;
            case ID:
                rb = view.findViewById(R.id.radio_sort_created);
                break;
            case KNOWLEDGE_LEVEL:
                rb = view.findViewById(R.id.radio_sort_knowledge);
                break;
            default:
                rb = view.findViewById(R.id.radio_sort_native);
        }
        rb.toggle();

        boolean asc = args.getBoolean(SortUtils.KEY_ACTIVE_SORT_ORDER);
        textOrder.setText(asc ? R.string.sort_asc : R.string.sort_desc);
        sw.setChecked(asc);
        // set an OnClickListener to dynamically adapt the text to the selected order.
        sw.setOnClickListener(v -> textOrder.setText(((SwitchCompat) v).isChecked() ? R.string.sort_asc : R.string.sort_desc));

        builder
                .setView(view)
                .setPositiveButton(R.string.sort, (dialog, id) -> {
                    // read the selected values and report them via callback
                    SortUtils.SortType type;
                    int checkedRadioButtonId = rg.getCheckedRadioButtonId();
                    if (checkedRadioButtonId == R.id.radio_sort_part_of_lesson) {
                        type = SortUtils.SortType.PART_OF_LESSON;
                    } else if (checkedRadioButtonId == R.id.radio_sort_foreign) {
                        type = SortUtils.SortType.FOREIGN_WORD;
                    } else if (checkedRadioButtonId == R.id.radio_sort_knowledge) {
                        type = SortUtils.SortType.KNOWLEDGE_LEVEL;
                    } else if (checkedRadioButtonId == R.id.radio_sort_created) {
                        type = SortUtils.SortType.ID;
                    } else {
                        type = SortUtils.SortType.NATIVE_WORD;
                    }
                    SortUtils.OnSortChangedListener listener = (SortUtils.OnSortChangedListener) getTargetFragment();
                    if (listener != null) {
                        listener.onUpdateSortOrderClick(type, sw.isChecked());
                    }
                }).setNegativeButton(R.string.cancel, (dialog, id) -> {
        });
        return builder.create();
    }
}
