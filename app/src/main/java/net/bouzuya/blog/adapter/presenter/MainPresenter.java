package net.bouzuya.blog.adapter.presenter;

import net.bouzuya.blog.driver.SelectedDateListener;
import net.bouzuya.blog.driver.view.MainView;
import net.bouzuya.blog.entity.EntryDetail;
import net.bouzuya.blog.entity.Optional;

public class MainPresenter implements Presenter<MainView> {
    private final SelectedDateListener selectedDateListener;
    private Optional<MainView> view;
    private Optional<String> selectedEntryDateOptional;
    private SelectedDateListener.OnChangeListener<Optional<String>> listener;

    public MainPresenter(SelectedDateListener selectedDateListener) {
        this.selectedDateListener = selectedDateListener;
        this.selectedEntryDateOptional = Optional.empty();
    }

    @Override
    public void onAttach(MainView view) {
        this.view = Optional.of(view);
        this.listener = new SelectedDateListener.OnChangeListener<Optional<String>>() {
            @Override
            public void onChange(Optional<String> value) {
                if (value.isPresent()) {
                    MainPresenter.this.view.get().showDetail(value.get());
                }
            }
        };
        this.selectedDateListener.subscribe(this.listener);
    }

    public void onLoadEntry(EntryDetail entryDetail) {
        this.view.get().updateShareButton(entryDetail);
    }

    public void onStart(Optional<String> selectedDateOptional) {
        if (!this.view.isPresent()) return; // do nothing
        this.selectedEntryDateOptional = selectedDateOptional;
        if (selectedDateOptional.isPresent()) {
            this.view.get().showDetail(selectedDateOptional.get());
        } else {
            this.view.get().showList();
        }
    }

    public void onSwitchDetail() {
        if (!selectedEntryDateOptional.isPresent()) return;
        this.view.get().showDetail(selectedEntryDateOptional.get());
    }

    public void onSwitchList() {
        this.view.get().showList();
    }

    @Override
    public void onDestroy() {
        // do nothing
    }

    @Override
    public void onDetach() {
        this.view = Optional.empty();
        this.selectedDateListener.unsubscribe(this.listener);
    }
}
