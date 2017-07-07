package net.bouzuya.blog.adapter.presenter;

import net.bouzuya.blog.driver.EntryDetailListener;
import net.bouzuya.blog.driver.SelectedDateListener;
import net.bouzuya.blog.driver.view.MainView;
import net.bouzuya.blog.entity.EntryDetail;
import net.bouzuya.blog.entity.Optional;

public class MainPresenter implements Presenter<MainView> {
    private final EntryDetailListener entryDetailListener;
    private final SelectedDateListener selectedDateListener;
    private Optional<MainView> view;
    private Optional<String> selectedEntryDateOptional;
    private SelectedDateListener.OnChangeListener<Optional<String>> onSelectedDateChangeListener;
    private EntryDetailListener.OnChangeListener<Optional<EntryDetail>> onEntryDetailChangeListener;

    public MainPresenter(
            EntryDetailListener entryDetailListener,
            SelectedDateListener selectedDateListener
    ) {
        this.entryDetailListener = entryDetailListener;
        this.selectedDateListener = selectedDateListener;
        this.selectedEntryDateOptional = Optional.empty();
    }

    @Override
    public void onAttach(MainView view) {
        this.view = Optional.of(view);
        this.onEntryDetailChangeListener =
                new EntryDetailListener.OnChangeListener<Optional<EntryDetail>>() {
                    @Override
                    public void onChange(Optional<EntryDetail> value) {
                        if (value.isPresent()) {
                            MainPresenter.this.view.get().updateShareButton(value.get());
                        }
                    }
                };
        this.entryDetailListener.subscribe(this.onEntryDetailChangeListener);
        this.onSelectedDateChangeListener =
                new SelectedDateListener.OnChangeListener<Optional<String>>() {
                    @Override
                    public void onChange(Optional<String> value) {
                        if (value.isPresent()) {
                            MainPresenter.this.view.get().showDetail(value.get());
                        }
                    }
                };
        this.selectedDateListener.subscribe(this.onSelectedDateChangeListener);
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
        this.entryDetailListener.unsubscribe(this.onEntryDetailChangeListener);
        this.selectedDateListener.unsubscribe(this.onSelectedDateChangeListener);
    }
}
