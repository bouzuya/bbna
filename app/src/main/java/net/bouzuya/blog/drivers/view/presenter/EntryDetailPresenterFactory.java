package net.bouzuya.blog.drivers.view.presenter;

public class EntryDetailPresenterFactory implements PresenterFactory<EntryDetailPresenter> {
    @Override
    public EntryDetailPresenter create() {
        return new EntryDetailPresenter();
    }
}
