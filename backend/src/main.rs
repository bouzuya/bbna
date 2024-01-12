use std::{collections::HashSet, sync::Arc};

use axum::{
    extract::{Path, State},
    http::StatusCode,
    routing, Json, Router,
};
use expo_push_notification_client::{Expo, ExpoClientOptions, ExpoPushMessage};
use tokio::{net::TcpListener, sync::Mutex};

#[derive(Clone, Debug, Default)]
struct App {
    expo_push_tokens: Arc<Mutex<HashSet<String>>>,
}

#[derive(Debug, serde::Deserialize, serde::Serialize)]
struct CreateExpoPushTokenRequestBody {
    expo_push_token: String,
}

async fn create_expo_push_token(
    State(App { expo_push_tokens }): State<App>,
    Json(CreateExpoPushTokenRequestBody { expo_push_token }): Json<CreateExpoPushTokenRequestBody>,
) -> StatusCode {
    let mut expo_push_tokens = expo_push_tokens.lock().await;
    // TODO: already exists
    expo_push_tokens.insert(expo_push_token);
    StatusCode::CREATED
}

async fn delete_expo_push_token(
    State(App { expo_push_tokens }): State<App>,
    Path(expo_push_token): Path<String>,
) -> StatusCode {
    let mut expo_push_tokens = expo_push_tokens.lock().await;
    // TODO: not found
    expo_push_tokens.remove(&expo_push_token);
    StatusCode::NO_CONTENT
}

async fn create_notification(
    State(App { expo_push_tokens }): State<App>,
) -> Result<StatusCode, StatusCode> {
    let expo_client = Expo::new(ExpoClientOptions {
        // TODO
        access_token: None,
    });
    let expo_push_tokens = expo_push_tokens.lock().await;
    expo_client
        .send_push_notifications(
            ExpoPushMessage::builder(expo_push_tokens.clone())
                .build()
                .map_err(|_| StatusCode::INTERNAL_SERVER_ERROR)?,
        )
        .await
        .map_err(|_| StatusCode::INTERNAL_SERVER_ERROR)?;
    Ok(StatusCode::CREATED)
}

async fn get_root() -> &'static str {
    "OK"
}

fn route(app: App) -> Router {
    Router::new()
        .route("/", routing::get(get_root))
        .route("/expo_push_tokens", routing::post(create_expo_push_token))
        .route(
            "/expo_push_tokens/:expo_push_token",
            routing::delete(delete_expo_push_token),
        )
        .route("/notifications", routing::post(create_notification))
        .with_state(app)
}

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
        http::{header::CONTENT_TYPE, Request},
        response::Response,
    };
    use tower::ServiceExt;

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
