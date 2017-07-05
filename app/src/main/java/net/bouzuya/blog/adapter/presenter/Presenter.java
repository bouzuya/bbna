package net.bouzuya.blog.adapter.presenter;

interface Presenter<T> {

    void onAttach(T view);

    void onDestroy();

    void onDetach();
}
