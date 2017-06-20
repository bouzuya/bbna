package net.bouzuya.blog.adapter.presenter;

public class EntryListPresenterFactory implements PresenterFactory<EntryListPresenter> {
    @Override
    public EntryListPresenter create() {
        return new EntryListPresenter();
    }
}
