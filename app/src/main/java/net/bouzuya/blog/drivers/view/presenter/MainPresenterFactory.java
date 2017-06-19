package net.bouzuya.blog.drivers.view.presenter;

public class MainPresenterFactory implements PresenterFactory<MainPresenter> {
    @Override
    public MainPresenter create() {
        return new MainPresenter();
    }
}
