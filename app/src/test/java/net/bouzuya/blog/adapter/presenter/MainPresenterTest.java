package net.bouzuya.blog.adapter.presenter;

import net.bouzuya.blog.adapter.view.MainView;
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
        verify(view, times(0)).switchDetail("2006-01-02");
        verify(view, times(1)).switchList();
        presenter.onSwitchList();
        presenter.onSwitchDetail();
        verify(view, times(0)).switchDetail("2006-01-02");
    }

    @Test
    public void test_onStart_WithArgs() throws Exception {
        MainPresenter presenter = new MainPresenter();
        MainView view = mock(MainView.class);
        presenter.onAttach(view);
        presenter.onStart(Optional.of("2006-01-02"));
        verify(view, times(1)).switchDetail("2006-01-02");
        verify(view, times(0)).switchList();
        presenter.onSwitchList();
        presenter.onSwitchDetail();
        verify(view, times(2)).switchDetail("2006-01-02");
    }
}