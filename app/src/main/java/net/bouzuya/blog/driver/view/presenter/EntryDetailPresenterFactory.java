package net.bouzuya.blog.driver.view.presenter;

public class EntryDetailPresenterFactory implements PresenterFactory<EntryDetailPresenter> {
    @Override
    public EntryDetailPresenter create() {
        return new EntryDetailPresenter();
    }
}
