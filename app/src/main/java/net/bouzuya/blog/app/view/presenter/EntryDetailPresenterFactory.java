package net.bouzuya.blog.app.view.presenter;

public class EntryDetailPresenterFactory implements PresenterFactory<EntryDetailPresenter> {
    @Override
    public EntryDetailPresenter create() {
        return new EntryDetailPresenter();
    }
}
