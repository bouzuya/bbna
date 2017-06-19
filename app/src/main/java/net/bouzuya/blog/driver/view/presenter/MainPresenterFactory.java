package net.bouzuya.blog.driver.view.presenter;

public class MainPresenterFactory implements PresenterFactory<MainPresenter> {
    @Override
    public MainPresenter create() {
        return new MainPresenter();
    }
}
