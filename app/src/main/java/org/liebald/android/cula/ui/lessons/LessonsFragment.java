package org.liebald.android.cula.ui.lessons;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

import org.liebald.android.cula.R;
import org.liebald.android.cula.data.database.Entities.LessonEntry;
import org.liebald.android.cula.databinding.FragmentLessonsBinding;
import org.liebald.android.cula.ui.updateLesson.UpdateLessonActivity;
import org.liebald.android.cula.utilities.InjectorUtils;

/**
 * A fragment presenting a list of {@link LessonEntry}s and the possibility to add new ones.
 */
public class LessonsFragment extends Fragment implements
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener,
        LessonsFragmentRecyclerViewAdapter.OnItemClickListener {

    private static final String TAG = LessonsFragment.class.getSimpleName();
    private int mPosition = RecyclerView.NO_POSITION;
    private FragmentLessonsBinding mBinding;

    private LessonsFragmentViewModel mViewModel;

    private LessonsFragmentRecyclerViewAdapter mAdapter;

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
        LessonsViewModelFactory factory = InjectorUtils.provideLessonsViewModelFactory(getContext());
        mViewModel = ViewModelProviders.of(getActivity(), factory).get(LessonsFragmentViewModel.class);

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
        ;
        mBinding.recyclerViewLessonsList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mAdapter = new LessonsFragmentRecyclerViewAdapter(this);
        mBinding.recyclerViewLessonsList.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mBinding.recyclerViewLessonsList);


        //Set the setOnClickListener for the Floating Action Button
        mBinding.fabAddLesson.setOnClickListener(v -> updateLessonActivity());
        mBinding.fabAddLesson.setImageDrawable(new IconicsDrawable(getContext()).icon(FontAwesome.Icon.faw_plus).color(Color.WHITE).sizeDp(24));

        return mBinding.getRoot();
    }

    /**
     * Called when the recycler view is swiped.
     * The swiped item  will be removed with an undo option.
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof LessonsFragmentRecyclerViewAdapter.ViewHolder) {

            final int deletedIndex = viewHolder.getAdapterPosition();
            // remove the item from the viewModel
            mViewModel.removeLessonEntry(deletedIndex);
            // show undo option
            Snackbar snackbar = Snackbar
                    .make(mBinding.libraryCoordinatorLayout, R.string.lesson_deleted, Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.undo, (View view) -> {
                Toast.makeText(getContext(), R.string.restored, Toast.LENGTH_SHORT).show();
                mViewModel.restoreLatestDeletedLessonEntry();
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    void updateLessonActivity() {
        Intent intent = new Intent(getContext(), UpdateLessonActivity.class);
        startActivity(intent);
    }


    @Override
    public void onResume() {
        super.onResume();

        String currentLanguage = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(getResources().getString(R.string.settings_select_language_key), "");
        Log.d(TAG, currentLanguage);
        if (!mViewModel.getCurrentLanguage().equals(currentLanguage)) {
            if (mViewModel.getLessonEntries() != null)
                mViewModel.getLessonEntries().removeObservers(this);

            mViewModel.setCurrentLanguage(currentLanguage);
            mViewModel.languageChanged();
            mViewModel.getLessonEntries().observe(this, libraryEntries -> {
                mAdapter.swapEntries(libraryEntries);
                if (mPosition == RecyclerView.NO_POSITION) {
                    mPosition = 0;
                }
                mBinding.recyclerViewLessonsList.smoothScrollToPosition(mPosition);
            });
        } else if (mViewModel.getLessonEntries() != null) {
            mAdapter.swapEntries(mViewModel.getLessonEntries().getValue());
        }

    }

    @Override
    public void onLessonEntryClick(View view, int id) {

        Intent intent = new Intent(getContext(), UpdateLessonActivity.class);
        intent.putExtra(UpdateLessonActivity.BUNDLE_EXTRA_UPDATE_KEY, id);
        startActivity(intent);
    }


}
