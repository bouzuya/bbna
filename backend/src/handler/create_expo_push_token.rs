use axum::{extract::State, http::StatusCode, routing, Json, Router};

use super::App;

pub fn route() -> Router<App> {
    Router::new().route("/expo_push_tokens", routing::post(handler))
}

#[derive(Debug, serde::Deserialize, serde::Serialize)]
pub struct CreateExpoPushTokenRequestBody {
    pub expo_push_token: String,
}

async fn handler(
    State(App { expo_push_tokens }): State<App>,
    Json(CreateExpoPushTokenRequestBody { expo_push_token }): Json<CreateExpoPushTokenRequestBody>,
) -> StatusCode {
    let mut expo_push_tokens = expo_push_tokens.lock().await;
    // TODO: already exists
    expo_push_tokens.insert(expo_push_token);
    StatusCode::CREATED
}
