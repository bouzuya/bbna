package net.bouzuya.blog.app.view.presenter;

public class EntryListPresenterFactory implements PresenterFactory<EntryListPresenter> {
    @Override
    public EntryListPresenter create() {
        return new EntryListPresenter();
    }
}
