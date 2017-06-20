package net.bouzuya.blog.adapter.presenter;

public interface Presenter<T> {

    void onAttach(T view);

    void onDestroy();

    void onDetach();
}
