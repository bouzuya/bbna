package net.bouzuya.blog.drivers.view.presenter;

public class EntryListPresenterFactory implements PresenterFactory<EntryListPresenter> {
    @Override
    public EntryListPresenter create() {
        return new EntryListPresenter();
    }
}
