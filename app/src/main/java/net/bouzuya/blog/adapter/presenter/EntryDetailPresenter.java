package net.bouzuya.blog.adapter.presenter;

import net.bouzuya.blog.driver.view.EntryDetailView;
import net.bouzuya.blog.driver.view_model.EntryDetailViewModel;
import net.bouzuya.blog.driver.view_model.EntryListViewModel;
import net.bouzuya.blog.entity.EntryDetail;
import net.bouzuya.blog.entity.Optional;

import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

public class EntryDetailPresenter implements Presenter<EntryDetailView> {
    private final EntryDetailViewModel entryDetailViewModel;
    private final EntryListViewModel entryListViewModel;

    private Optional<EntryDetailView> view;
    private CompositeDisposable disposables;

    public EntryDetailPresenter(
            EntryDetailViewModel entryDetailViewModel,
            EntryListViewModel entryListViewModel
    ) {
        this.entryDetailViewModel = entryDetailViewModel;
        this.entryListViewModel = entryListViewModel;
    }

    @Override
    public void onAttach(EntryDetailView view) {
        this.view = Optional.of(view);
        this.disposables = new CompositeDisposable(
                this.entryListViewModel.observable().subscribe(new Consumer<Optional<String>>() {
                    @Override
                    public void accept(@NonNull Optional<String> selectedDate) throws Exception {
                        Timber.d("onChange: %s", selectedDate);
                        if (!selectedDate.isPresent()) return; // do nothing
                        if (!EntryDetailPresenter.this.view.isPresent()) return; // do nothing
                        EntryDetailView entryDetailView = EntryDetailPresenter.this.view.get();
                        entryDetailView.showLoading();
                        EntryDetailPresenter.this.loadEntryDetail(selectedDate);
                    }
                }));
    }

    @Override
    public void onDestroy() {
        // do nothing
    }

    @Override
    public void onDetach() {
        this.view = Optional.empty();
        this.disposables.dispose();
    }

    public void onStart() {
        loadEntryDetail(entryListViewModel.get());
    }

    private void loadEntryDetail(Optional<String> dateOptional) {
        entryDetailViewModel
                .load(dateOptional)
                .subscribe(new SingleObserver<EntryDetail>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        if (!view.isPresent()) return;
                        view.get().showLoading();
                    }

                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull EntryDetail entryDetail) {
                        if (!view.isPresent()) return;
                        view.get().hideLoading();
                        view.get().showMessage("load " + entryDetail.getId().toISO8601DateString());
                        view.get().showEntryDetail(toHtmlString(entryDetail));
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        if (!view.isPresent()) return;
                        Timber.e("onLoadEntryDetailFinished: ", e);
                        view.get().hideLoading();
                        view.get().showMessage("load error");
                    }
                });
    }

    private String toHtmlString(EntryDetail d) {
        return new StringBuilder()
                .append("<html>")
                .append("<head></head>")
                .append("<body>")
                .append("<article>")
                .append("<header>")
                .append("<h1 class=\"title\">").append(d.getTitle()).append("</h1>")
                // .append("<p class=\"pubdate\">").append(d.getPubdate()).append("</p>")
                // .append("<p class=\"minutes\">").append(d.getMinutes()).append("</p>")
                // .append(d.getTags().isEmpty() ? "" : "<ul class=\"tags\"><li>")
                // .append(this.join("</li><li>", d.getTags()))
                // .append(d.getTags().isEmpty() ? "" : "</li></ul>")
                .append("</header>")
                .append("<div class=\"body\">").append(d.getHtml()).append("</div>")
                .append("</article>")
                .append("</body>")
                .append("</html>")
                .toString();
    }

//    private String join(String delimiter, List<String> list) {
//        if (list.isEmpty()) return "";
//        StringBuilder builder = new StringBuilder();
//        for (String item : list) {
//            builder.append(delimiter).append(item);
//        }
//        return delimiter.isEmpty()
//                ? builder.toString()
//                : builder.substring(delimiter.length()).toString();
//    }
}
