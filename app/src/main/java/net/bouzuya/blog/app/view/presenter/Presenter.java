package net.bouzuya.blog.app.view.presenter;

public interface Presenter<T> {

    void onAttach(T view);

    void onDestroy();

    void onDetach();
}
