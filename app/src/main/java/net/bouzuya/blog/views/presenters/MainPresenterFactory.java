package net.bouzuya.blog.views.presenters;

public class MainPresenterFactory implements PresenterFactory<MainPresenter> {
    @Override
    public MainPresenter create() {
        return new MainPresenter();
    }
}
