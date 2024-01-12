use axum::{
    extract::State,
    http::StatusCode,
    response::{IntoResponse, Response},
    routing, Json, Router,
};

use crate::command::create_expo_push_token::{CanCreateExpoPushToken, Input, Output};

pub fn route<T>() -> Router<T>
where
    T: Clone + CanCreateExpoPushToken + Send + Sync + 'static,
{
    Router::new().route("/expo_push_tokens", routing::post(handler::<T>))
}

#[derive(Debug, serde::Deserialize, serde::Serialize)]
pub struct CreateExpoPushTokenRequestBody {
    pub expo_push_token: String,
}

impl TryFrom<CreateExpoPushTokenRequestBody> for Input {
    type Error = StatusCode;

    fn try_from(
        CreateExpoPushTokenRequestBody { expo_push_token }: CreateExpoPushTokenRequestBody,
    ) -> Result<Self, Self::Error> {
        Ok(Self { expo_push_token })
    }
}

#[derive(Debug, serde::Deserialize, serde::Serialize)]
struct CreateExpoPushTokenResponseBody;

impl From<Output> for CreateExpoPushTokenResponseBody {
    fn from(_: Output) -> Self {
        Self
    }
}

impl IntoResponse for CreateExpoPushTokenResponseBody {
    fn into_response(self) -> Response {
        StatusCode::CREATED.into_response()
    }
}

async fn handler<T>(
    State(app): State<T>,
    Json(body): Json<CreateExpoPushTokenRequestBody>,
) -> Result<CreateExpoPushTokenResponseBody, StatusCode>
where
    T: CanCreateExpoPushToken,
{
    let input = Input::try_from(body)?;
    let output = app.create_expo_push_token(input).await.map_err(|_| {
        // TODO
        StatusCode::INTERNAL_SERVER_ERROR
    })?;
    Ok(CreateExpoPushTokenResponseBody::from(output))
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
        command::create_expo_push_token::{self, MockCanCreateExpoPushToken},
        tests::ResponseExt as _,
    };

    use super::*;

    #[tokio::test]
    async fn test_create_expo_push_token() -> anyhow::Result<()> {
        let mut app = MockApp::default();
        Arc::get_mut(&mut app.mock_create_expo_push_token)
            .context("Arc::get_mut")?
            .expect_create_expo_push_token()
            .return_once(|_| Box::pin(async { Ok(Output) }));
        let router = route().with_state(app);
        let request = Request::builder()
            .method("POST")
            .uri("/expo_push_tokens")
            .header(CONTENT_TYPE, "application/json")
            .body(Body::from(serde_json::to_string(
                &CreateExpoPushTokenRequestBody {
                    expo_push_token: "ExponentPushToken[xxxxxxxxxxxxxxxxxxxxxx]".to_string(),
                },
            )?))?;
        let response = router.oneshot(request).await?;
        assert_eq!(response.status(), StatusCode::CREATED);
        assert_eq!(response.into_body_string().await?, "");
        Ok(())
    }

    #[derive(Clone, Default)]
    struct MockApp {
        mock_create_expo_push_token: Arc<MockCanCreateExpoPushToken>,
    }

    #[async_trait]
    impl CanCreateExpoPushToken for MockApp {
        async fn create_expo_push_token(
            &self,
            input: create_expo_push_token::Input,
        ) -> Result<create_expo_push_token::Output, create_expo_push_token::Error> {
            self.mock_create_expo_push_token
                .create_expo_push_token(input)
                .await
        }
    }
}
