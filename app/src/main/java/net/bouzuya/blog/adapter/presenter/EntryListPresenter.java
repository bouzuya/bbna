package net.bouzuya.blog.adapter.presenter;

import net.bouzuya.blog.driver.SelectedDateListener;
import net.bouzuya.blog.driver.view.EntryListView;
import net.bouzuya.blog.entity.Optional;

public class EntryListPresenter implements Presenter<EntryListView> {
    private final SelectedDateListener selectedDateListener;
    private EntryListView view;

    public EntryListPresenter(SelectedDateListener selectedDateListener) {
        this.selectedDateListener = selectedDateListener;
    }

    @Override
    public void onAttach(EntryListView view) {
        this.view = view;
    }

    @Override
    public void onDestroy() {
        // do nothing
    }

    @Override
    public void onDetach() {
        this.view = null;
    }

    public void onSelectEntry(String date) {
        selectedDateListener.set(Optional.of(date));
    }
}
