mod handler;

use handler::{route, App};
use tokio::net::TcpListener;

#[tokio::main]
async fn main() -> anyhow::Result<()> {
    let router = route(App::default());
    let listener = TcpListener::bind("0.0.0.0:3000").await?;
    Ok(axum::serve(listener, router).await?)
}

#[cfg(test)]
mod tests {
    use axum::{
        async_trait,
        body::Body,
        http::{header::CONTENT_TYPE, Request, StatusCode},
        response::Response,
    };
    use tower::ServiceExt;

    use crate::handler::CreateExpoPushTokenRequestBody;

    use super::*;

    #[tokio::test]
    async fn test_get_root() -> anyhow::Result<()> {
        let router = route(App::default());
        let request = Request::builder()
            .method("GET")
            .uri("/")
            .body(Body::empty())?;
        let response = router.oneshot(request).await?;
        assert_eq!(response.status(), StatusCode::OK);
        assert_eq!(response.into_body_string().await?, "OK");
        Ok(())
    }

    #[tokio::test]
    async fn test_create_expo_push_token() -> anyhow::Result<()> {
        let router = route(App::default());
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

    #[tokio::test]
    async fn test_delete_expo_push_token() -> anyhow::Result<()> {
        let router = route(App::default());
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

    #[tokio::test]
    async fn test_create_notifications() -> anyhow::Result<()> {
        // TODO
        Ok(())
    }

    #[async_trait]
    trait ResponseExt {
        async fn into_body_string(self) -> anyhow::Result<String>;
    }

    #[async_trait]
    impl ResponseExt for Response {
        async fn into_body_string(self) -> anyhow::Result<String> {
            Ok(String::from_utf8(
                axum::body::to_bytes(self.into_body(), usize::MAX)
                    .await?
                    .to_vec(),
            )?)
        }
    }
}
