package net.bouzuya.blog.adapter.presenter;

import net.bouzuya.blog.driver.view_model.EntryDetailViewModel;
import net.bouzuya.blog.driver.view_model.EntryListViewModel;
import net.bouzuya.blog.driver.view.MainView;
import net.bouzuya.blog.entity.EntryDetail;
import net.bouzuya.blog.entity.Optional;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class MainPresenter implements Presenter<MainView> {
    private final EntryDetailViewModel entryDetailViewModel;
    private final EntryListViewModel entryListViewModel;
    private Optional<MainView> view;
    private Optional<EntryDetail> entryDetailOptional;
    private Optional<String> selectedEntryDateOptional;
    private CompositeDisposable subscriptions;

    public MainPresenter(
            EntryDetailViewModel entryDetailViewModel,
            EntryListViewModel entryListViewModel
    ) {
        this.entryDetailViewModel = entryDetailViewModel;
        this.entryDetailOptional = Optional.empty();
        this.entryListViewModel = entryListViewModel;
        this.selectedEntryDateOptional = Optional.empty();
    }

    @Override
    public void onAttach(MainView view) {
        this.subscriptions = new CompositeDisposable();
        this.view = Optional.of(view);
        this.subscriptions.add(
                this.entryDetailViewModel.observable().subscribe(new Consumer<Optional<EntryDetail>>() {
                    @Override
                    public void accept(@NonNull Optional<EntryDetail> value) throws Exception {
                        if (value.isPresent()) {
                            MainPresenter.this.entryDetailOptional = value;
                            updateShareButtonForDetail(value);
                        }

                    }
                }));
        this.subscriptions.add(
                this.entryListViewModel.observable().subscribe(new Consumer<Optional<String>>() {
                    @Override
                    public void accept(@NonNull Optional<String> value) throws Exception {
                        if (value.isPresent()) {
                            MainPresenter.this.selectedEntryDateOptional = value;
                            MainPresenter.this.view.get().showDetail(value.get());
                            updateShareButtonForDetail(Optional.<EntryDetail>empty());
                        }
                    }
                })
        );
    }

    public void onStart(Optional<String> selectedDateOptional) {
        if (!this.view.isPresent()) return; // do nothing
        this.selectedEntryDateOptional = selectedDateOptional;
        if (selectedDateOptional.isPresent()) {
            this.view.get().showDetail(selectedDateOptional.get());
            this.view.get().updateShareButton(Optional.<String>empty(), Optional.<String>empty());
        } else {
            this.view.get().showList();
            updateShareButtonForList();
        }
    }

    public void onSwitchDetail() {
        if (!selectedEntryDateOptional.isPresent()) return;
        this.view.get().showDetail(selectedEntryDateOptional.get());
        if (!entryDetailOptional.isPresent()) return;
        updateShareButtonForDetail(entryDetailOptional);
    }

    public void onSwitchList() {
        this.view.get().showList();
        updateShareButtonForList();
    }

    @Override
    public void onDestroy() {
        // do nothing
    }

    @Override
    public void onDetach() {
        this.view = Optional.empty();
        this.subscriptions.dispose();
    }

    private void updateShareButtonForDetail(Optional<EntryDetail> entryDetailOptional) {
        if (entryDetailOptional.isPresent()) {
            EntryDetail entryDetail = entryDetailOptional.get();
            String url = entryDetail.getId().toUrl().toUrlString();
            String date = entryDetail.getId().toISO8601DateString();
            String title = entryDetail.getTitle();
            String dateAndTitle = String.format("%s %s", date, title);
            this.view.get().updateShareButton(Optional.of(dateAndTitle), Optional.of(url));
        } else {
            this.view.get().updateShareButton(Optional.<String>empty(), Optional.<String>empty());
        }
    }

    private void updateShareButtonForList() {
        String title = "blog.bouzuya.net";
        String url = "https://blog.bouzuya.net/";
        this.view.get().updateShareButton(Optional.of(title), Optional.of(url));
    }
}
