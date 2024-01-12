use std::{collections::HashSet, sync::Arc};

use axum::{
    extract::{Path, State},
    http::StatusCode,
    routing, Json, Router,
};
use expo_push_notification_client::{Expo, ExpoClientOptions, ExpoPushMessage};
use tokio::sync::Mutex;

#[derive(Clone, Debug, Default)]
pub struct App {
    pub expo_push_tokens: Arc<Mutex<HashSet<String>>>,
}

pub fn route(app: App) -> Router {
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

#[derive(Debug, serde::Deserialize, serde::Serialize)]
pub struct CreateExpoPushTokenRequestBody {
    pub expo_push_token: String,
}

pub async fn create_expo_push_token(
    State(App { expo_push_tokens }): State<App>,
    Json(CreateExpoPushTokenRequestBody { expo_push_token }): Json<CreateExpoPushTokenRequestBody>,
) -> StatusCode {
    let mut expo_push_tokens = expo_push_tokens.lock().await;
    // TODO: already exists
    expo_push_tokens.insert(expo_push_token);
    StatusCode::CREATED
}

pub async fn delete_expo_push_token(
    State(App { expo_push_tokens }): State<App>,
    Path(expo_push_token): Path<String>,
) -> StatusCode {
    let mut expo_push_tokens = expo_push_tokens.lock().await;
    // TODO: not found
    expo_push_tokens.remove(&expo_push_token);
    StatusCode::NO_CONTENT
}

pub async fn create_notification(
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

pub async fn get_root() -> &'static str {
    "OK"
}
