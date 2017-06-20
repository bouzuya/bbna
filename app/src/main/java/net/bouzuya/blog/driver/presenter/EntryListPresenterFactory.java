package net.bouzuya.blog.driver.presenter;

public class EntryListPresenterFactory implements PresenterFactory<EntryListPresenter> {
    @Override
    public EntryListPresenter create() {
        return new EntryListPresenter();
    }
}
