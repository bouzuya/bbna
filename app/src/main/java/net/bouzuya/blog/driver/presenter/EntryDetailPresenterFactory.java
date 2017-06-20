package net.bouzuya.blog.driver.presenter;

public class EntryDetailPresenterFactory implements PresenterFactory<EntryDetailPresenter> {
    @Override
    public EntryDetailPresenter create() {
        return new EntryDetailPresenter();
    }
}
