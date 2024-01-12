use axum::{routing, Router};

use super::App;

async fn get_root() -> &'static str {
    "OK"
}

pub fn route() -> Router<App> {
    Router::new().route("/", routing::get(get_root))
}
