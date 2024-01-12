use axum::{
    extract::{Path, State},
    http::StatusCode,
    routing, Router,
};

use super::App;

pub fn route() -> Router<App> {
    Router::new().route(
        "/expo_push_tokens/:expo_push_token",
        routing::delete(handler),
    )
}

async fn handler(
    State(App { expo_push_tokens }): State<App>,
    Path(expo_push_token): Path<String>,
) -> StatusCode {
    let mut expo_push_tokens = expo_push_tokens.lock().await;
    // TODO: not found
    expo_push_tokens.remove(&expo_push_token);
    StatusCode::NO_CONTENT
}
