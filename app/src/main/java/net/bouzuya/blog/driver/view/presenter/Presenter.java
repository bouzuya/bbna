package net.bouzuya.blog.driver.view.presenter;

public interface Presenter<T> {

    void onAttach(T view);

    void onDestroy();

    void onDetach();
}
