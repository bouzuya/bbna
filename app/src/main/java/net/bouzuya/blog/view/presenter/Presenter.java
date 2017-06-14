package net.bouzuya.blog.view.presenter;

public interface Presenter<T> {

    void onAttach(T view);

    void onDestroy();

    void onDetach();
}
