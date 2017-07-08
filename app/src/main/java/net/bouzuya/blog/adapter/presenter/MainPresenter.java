package net.bouzuya.blog.adapter.presenter;

import net.bouzuya.blog.adapter.view.MainView;
import net.bouzuya.blog.driver.view_model.EntryDetailViewModel;
import net.bouzuya.blog.driver.view_model.EntryListViewModel;
import net.bouzuya.blog.entity.EntryDetail;
import net.bouzuya.blog.entity.Optional;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class MainPresenter implements Presenter<MainView> {
    private final EntryDetailViewModel entryDetailViewModel;
    private final EntryListViewModel entryListViewModel;
    private Optional<MainView> view;
    private CompositeDisposable subscriptions;
    private Optional<String> shareTitle;
    private Optional<String> shareUrl;

    public MainPresenter(
            EntryDetailViewModel entryDetailViewModel,
            EntryListViewModel entryListViewModel
    ) {
        this.entryDetailViewModel = entryDetailViewModel;
        this.entryListViewModel = entryListViewModel;
    }

    @Override
    public void onAttach(MainView view) {
        this.view = Optional.of(view);
        this.subscriptions = new CompositeDisposable(
                this.entryDetailViewModel.observable().subscribe(new Consumer<Optional<EntryDetail>>() {
                    @Override
                    public void accept(@NonNull Optional<EntryDetail> value) throws Exception {
                        if (value.isPresent()) {
                            updateShareButtonForDetail(value);
                        }
                    }
                }),
                this.entryListViewModel.observable().subscribe(new Consumer<Optional<String>>() {
                    @Override
                    public void accept(@NonNull Optional<String> value) throws Exception {
                        if (value.isPresent()) {
                            MainPresenter.this.view.get().switchDetail(value.get());
                            updateShareButtonForDetail(Optional.<EntryDetail>empty());
                        }
                    }
                })
        );
    }

    public void onClickShare() {
        this.view.get().share(shareTitle.get(), shareUrl.get());
    }

    public void onStart(Optional<String> selectedDateOptional) {
        if (!this.view.isPresent()) return; // do nothing
        if (selectedDateOptional.isPresent()) {
            this.entryListViewModel.set(selectedDateOptional);
            this.view.get().switchDetail(selectedDateOptional.get());
            this.shareTitle = Optional.empty();
            this.shareUrl = Optional.empty();
            this.view.get().hideShareButton();
        } else {
            this.view.get().switchList("blog.bouzuya.net");
            updateShareButtonForList();
        }
    }

    public void onSwitchDetail() {
        if (!entryListViewModel.get().isPresent()) return;
        this.view.get().switchDetail(entryListViewModel.get().get());
        if (!entryDetailViewModel.get().isPresent()) return;
        updateShareButtonForDetail(entryDetailViewModel.get());
    }

    public void onSwitchList() {
        this.view.get().switchList("blog.bouzuya.net");
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
            this.shareTitle = Optional.of(dateAndTitle);
            this.shareUrl = Optional.of(url);
            this.view.get().showShareButton();
        } else {
            this.shareTitle = Optional.empty();
            this.shareUrl = Optional.empty();
            this.view.get().hideShareButton();
        }
    }

    private void updateShareButtonForList() {
        this.shareTitle = Optional.of("blog.bouzuya.net");
        this.shareUrl = Optional.of("https://blog.bouzuya.net/");
        this.view.get().showShareButton();
    }
}
