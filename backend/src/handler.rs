mod create_expo_push_token;
mod create_notifications;
mod delete_expo_push_token;
mod get_root;

use crate::command::App;

use axum::Router;

pub fn route(app: App) -> Router {
    Router::new()
        .merge(create_expo_push_token::route())
        .merge(create_notifications::route())
        .merge(delete_expo_push_token::route())
        .merge(get_root::route())
        .with_state(app)
}
