package net.bouzuya.blog.adapter.presenter;

public class MainPresenterFactory implements PresenterFactory<MainPresenter> {
    @Override
    public MainPresenter create() {
        return new MainPresenter();
    }
}
