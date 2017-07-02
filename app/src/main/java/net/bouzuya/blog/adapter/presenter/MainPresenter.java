package net.bouzuya.blog.adapter.presenter;

import net.bouzuya.blog.driver.view.MainView;
import net.bouzuya.blog.entity.EntryDetail;
import net.bouzuya.blog.entity.Optional;

public class MainPresenter implements Presenter<MainView> {
    private Optional<MainView> view;
    private Optional<String> selectedEntryDateOptional;

    public MainPresenter() {
        this.selectedEntryDateOptional = Optional.empty();
    }

    @Override
    public void onAttach(MainView view) {
        this.view = Optional.of(view);
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

    public void onSelectEntry(String date) {
        this.selectedEntryDateOptional = Optional.of(date);
        this.view.get().showDetail(date);
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
    }
}
