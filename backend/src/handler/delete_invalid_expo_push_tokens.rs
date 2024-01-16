use axum::{extract::State, http::StatusCode, routing, Router};

use crate::command::delete_invalid_expo_push_tokens::{CanDeleteInvalidExpoPushTokens, Input};

pub fn route<T>() -> Router<T>
where
    T: Clone + CanDeleteInvalidExpoPushTokens + Send + Sync + 'static,
{
    Router::new().route(
        "/expo_push_tokens/:expo_push_token",
        routing::delete(handler::<T>),
    )
}

async fn handler<T>(State(app): State<T>) -> Result<StatusCode, StatusCode>
where
    T: CanDeleteInvalidExpoPushTokens,
{
    let _output = app
        .delete_invalid_expo_push_tokens(Input)
        .await
        .map_err(|_| {
            // TODO
            StatusCode::INTERNAL_SERVER_ERROR
        })?;
    Ok(StatusCode::NO_CONTENT)
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
        command::delete_invalid_expo_push_tokens::{
            self, MockCanDeleteInvalidExpoPushTokens, Output,
        },
        tests::ResponseExt as _,
    };

    use super::*;

    #[tokio::test]
    async fn test_delete_invalid_expo_push_tokens() -> anyhow::Result<()> {
        let mut app = MockApp::default();
        Arc::get_mut(&mut app.mock_delete_invalid_expo_push_tokens)
            .context("Arc::get_mut")?
            .expect_delete_invalid_expo_push_tokens()
            .return_once(|_| Box::pin(async { Ok(Output) }));
        let router = route().with_state(app);
        let request = Request::builder()
            .method("DELETE")
            .uri("/invalid_expo_push_tokens")
            .header(CONTENT_TYPE, "application/json")
            .body(Body::empty())?;
        let response = router.oneshot(request).await?;
        assert_eq!(response.status(), StatusCode::NO_CONTENT);
        assert_eq!(response.into_body_string().await?, "");
        Ok(())
    }

    #[derive(Clone, Default)]
    struct MockApp {
        mock_delete_invalid_expo_push_tokens: Arc<MockCanDeleteInvalidExpoPushTokens>,
    }

    #[async_trait]
    impl CanDeleteInvalidExpoPushTokens for MockApp {
        async fn delete_invalid_expo_push_tokens(
            &self,
            input: delete_invalid_expo_push_tokens::Input,
        ) -> Result<delete_invalid_expo_push_tokens::Output, delete_invalid_expo_push_tokens::Error>
        {
            self.mock_delete_invalid_expo_push_tokens
                .delete_invalid_expo_push_tokens(input)
                .await
        }
    }
}
