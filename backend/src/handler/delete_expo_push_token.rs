use axum::{
    extract::{Path, State},
    http::StatusCode,
    routing, Router,
};

use crate::command::delete_expo_push_token::{CanDeleteExpoPushToken, Input};

pub fn route<T>() -> Router<T>
where
    T: Clone + CanDeleteExpoPushToken + Send + Sync + 'static,
{
    Router::new().route(
        "/expo_push_tokens/:expo_push_token",
        routing::delete(handler::<T>),
    )
}

async fn handler<T>(
    State(app): State<T>,
    Path(expo_push_token): Path<String>,
) -> Result<StatusCode, StatusCode>
where
    T: CanDeleteExpoPushToken,
{
    let input = Input { expo_push_token };
    let _output = app.delete_expo_push_token(input).await.map_err(|_| {
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
        command::delete_expo_push_token::{self, MockCanDeleteExpoPushToken, Output},
        tests::ResponseExt as _,
    };

    use super::*;

    #[tokio::test]
    async fn test_delete_expo_push_token() -> anyhow::Result<()> {
        let mut app = MockApp::default();
        Arc::get_mut(&mut app.mock_delete_expo_push_token)
            .context("Arc::get_mut")?
            .expect_delete_expo_push_token()
            .return_once(|_| Box::pin(async { Ok(Output) }));
        let router = route().with_state(app);
        let request = Request::builder()
            .method("DELETE")
            .uri("/expo_push_tokens/ExponentPushToken[xxxxxxxxxxxxxxxxxxxxxx]")
            .header(CONTENT_TYPE, "application/json")
            .body(Body::empty())?;
        let response = router.oneshot(request).await?;
        assert_eq!(response.status(), StatusCode::NO_CONTENT);
        assert_eq!(response.into_body_string().await?, "");
        Ok(())
    }

    #[derive(Clone, Default)]
    struct MockApp {
        mock_delete_expo_push_token: Arc<MockCanDeleteExpoPushToken>,
    }

    #[async_trait]
    impl CanDeleteExpoPushToken for MockApp {
        async fn delete_expo_push_token(
            &self,
            input: delete_expo_push_token::Input,
        ) -> Result<delete_expo_push_token::Output, delete_expo_push_token::Error> {
            self.mock_delete_expo_push_token
                .delete_expo_push_token(input)
                .await
        }
    }
}
