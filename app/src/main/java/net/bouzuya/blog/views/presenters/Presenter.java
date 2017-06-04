package net.bouzuya.blog.views.presenters;

public interface Presenter<T> {

    void onAttach(T view);

    void onDestroy();

    void onDetach();
}
