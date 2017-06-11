package net.bouzuya.blog.views.presenters;

import net.bouzuya.blog.models.Optional;
import net.bouzuya.blog.views.views.MainView;

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

    public void onStart() {
        this.view.get().showList();
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
