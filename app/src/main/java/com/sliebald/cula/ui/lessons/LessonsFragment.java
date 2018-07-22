package com.sliebald.cula.ui.lessons;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.sliebald.cula.R;
import com.sliebald.cula.data.database.Entities.LessonEntry;
import com.sliebald.cula.databinding.FragmentLessonsBinding;
import com.sliebald.cula.ui.updateLesson.UpdateLessonActivity;

/**
 * A fragment presenting a list of {@link LessonEntry}s and the possibility to add new ones.
 */
public class LessonsFragment extends Fragment implements
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener,
        LessonsRecyclerViewAdapter.OnItemClickListener {

    /**
     * Tag for logging and fragment identification.
     */
    public static final String TAG = LessonsFragment.class.getSimpleName();
    private int mPosition = RecyclerView.NO_POSITION;

    /**
     * Data binding of the layout.
     */
    private FragmentLessonsBinding mBinding;

    /**
     * {@link android.arch.lifecycle.ViewModel} of the Fragment.
     */
    private LessonsViewModel mViewModel;

    /**
     * The adapter used to show the lessons.
     */
    private LessonsRecyclerViewAdapter mAdapter;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LessonsFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() == null || getContext() == null)
            return;
        mViewModel = ViewModelProviders.of(getActivity()).get(LessonsViewModel.class);


    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.getLessonEntries().observe(this, lessonEntries -> {
            if (lessonEntries == null || lessonEntries.size() == 0) {
                Snackbar snackbar = Snackbar
                        .make(mBinding.lessonCoordinatorLayout, R.string.lesson_add_first_lesson,
                                Snackbar.LENGTH_LONG);
                snackbar.setAction(R.string.add, (View view) -> updateLessonActivity());
                snackbar.show();
                return;
            }
            mAdapter.swapEntries(lessonEntries);
            if (mPosition == RecyclerView.NO_POSITION) {
                mPosition = 0;
            }
            mBinding.recyclerViewLessonsList.smoothScrollToPosition(mPosition);
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //initialize Data Binding

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_lessons, container, false);

        // Set the adapter
        Context context = mBinding.recyclerViewLessonsList.getContext();
        mBinding.recyclerViewLessonsList.setLayoutManager(new LinearLayoutManager(context));

        if (getContext() == null)
            return mBinding.getRoot();
        mBinding.recyclerViewLessonsList.addItemDecoration(new DividerItemDecoration(getContext()
                , DividerItemDecoration.VERTICAL));
        mAdapter = new LessonsRecyclerViewAdapter(this);
        mBinding.recyclerViewLessonsList.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0,
                ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mBinding
                .recyclerViewLessonsList);


        //Set the setOnClickListener for the Floating Action Button
        mBinding.fabAddLesson.setOnClickListener(v -> updateLessonActivity());
        mBinding.fabAddLesson.setImageDrawable(new IconicsDrawable(getContext()).icon(FontAwesome
                .Icon.faw_plus).color(Color.WHITE).sizeDp(24));

        return mBinding.getRoot();
    }

    /**
     * Called when the recycler view is swiped.
     * The swiped item  will be removed with an undo option.
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof LessonsRecyclerViewAdapter.ViewHolder) {

            final int deletedIndex = viewHolder.getAdapterPosition();
            // remove the item from the viewModel
            mViewModel.removeLessonEntry(deletedIndex);
            // show undo option
            Snackbar snackbar = Snackbar
                    .make(mBinding.lessonCoordinatorLayout, R.string.lesson_deleted, Snackbar
                            .LENGTH_LONG);
            snackbar.setAction(R.string.undo, (View view) -> {
                Snackbar.make(mBinding.lessonCoordinatorLayout, R.string.restored, Snackbar
                        .LENGTH_SHORT);
                mViewModel.restoreLatestDeletedLessonEntry();
            });
            snackbar.show();
        }
    }

    private void updateLessonActivity() {
        Intent intent = new Intent(getContext(), UpdateLessonActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLessonEntryClick(int id) {

        Intent intent = new Intent(getContext(), UpdateLessonActivity.class);
        intent.putExtra(UpdateLessonActivity.BUNDLE_EXTRA_UPDATE_KEY, id);
        startActivity(intent);
    }

}
