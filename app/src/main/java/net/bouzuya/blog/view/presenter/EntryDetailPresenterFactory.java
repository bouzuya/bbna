package net.bouzuya.blog.view.presenter;

public class EntryDetailPresenterFactory implements PresenterFactory<EntryDetailPresenter> {
    @Override
    public EntryDetailPresenter create() {
        return new EntryDetailPresenter();
    }
}
