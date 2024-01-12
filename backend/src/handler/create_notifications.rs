use axum::{extract::State, http::StatusCode, routing, Router};
use expo_push_notification_client::{Expo, ExpoClientOptions, ExpoPushMessage};

use super::App;

pub fn route() -> Router<App> {
    Router::new().route("/notifications", routing::post(handler))
}

async fn handler(State(App { expo_push_tokens }): State<App>) -> Result<StatusCode, StatusCode> {
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
