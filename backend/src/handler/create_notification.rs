use axum::{extract::State, http::StatusCode, routing, Router};

use crate::command::create_notification::{CanCreateNotification, Input};

pub fn route<T>() -> Router<T>
where
    T: Clone + CanCreateNotification + Send + Sync + 'static,
{
    Router::new().route("/notifications", routing::post(handler::<T>))
}

async fn handler<T>(State(app): State<T>) -> Result<StatusCode, StatusCode>
where
    T: Clone + CanCreateNotification + Send + Sync + 'static,
{
    app.create_notification(Input).await.map_err(|_| {
        // TODO
        StatusCode::INTERNAL_SERVER_ERROR
    })?;
    Ok(StatusCode::CREATED)
}

#[cfg(test)]
mod tests {
    use std::sync::Arc;

    use anyhow::Context;
    use async_trait::async_trait;
    use axum::{
        body::Body,
        http::{header::CONTENT_TYPE, Request},
    };
    use tower::ServiceExt as _;

    use crate::{
        command::create_notification::{self, MockCanCreateNotification, Output},
        tests::ResponseExt as _,
    };

    use super::*;

    #[tokio::test]
    async fn test_create_notification() -> anyhow::Result<()> {
        let mut app = MockApp::default();
        Arc::get_mut(&mut app.mock_create_notification)
            .context("Arc::get_mut")?
            .expect_create_notification()
            .return_once(|_| Box::pin(async { Ok(Output) }));
        let router = route().with_state(app);
        let request = Request::builder()
            .method("POST")
            .uri("/notifications")
            .header(CONTENT_TYPE, "application/json")
            .body(Body::empty())?;
        let response = router.oneshot(request).await?;
        assert_eq!(response.status(), StatusCode::CREATED);
        assert_eq!(response.into_body_string().await?, "");
        Ok(())
    }

    #[derive(Clone, Default)]
    struct MockApp {
        mock_create_notification: Arc<MockCanCreateNotification>,
    }

    #[async_trait]
    impl CanCreateNotification for MockApp {
        async fn create_notification(
            &self,
            input: create_notification::Input,
        ) -> Result<create_notification::Output, create_notification::Error> {
            self.mock_create_notification
                .create_notification(input)
                .await
        }
    }
}
