package net.bouzuya.blog.adapter.presenter;

import net.bouzuya.blog.driver.view.MainView;
import net.bouzuya.blog.entity.Optional;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MainPresenterTest {
    @Test
    public void test_onStart_NoArgs() throws Exception {
        MainPresenter presenter = new MainPresenter();
        MainView view = mock(MainView.class);
        presenter.onAttach(view);
        presenter.onStart(Optional.<String>empty());
        verify(view, times(0)).showDetail("2006-01-02");
        verify(view, times(1)).showList();
    }

    @Test
    public void test_onStart_WithArgs() throws Exception {
        MainPresenter presenter = new MainPresenter();
        MainView view = mock(MainView.class);
        presenter.onAttach(view);
        presenter.onStart(Optional.of("2006-01-02"));
        verify(view, times(1)).showDetail("2006-01-02");
        verify(view, times(0)).showList();
    }
}