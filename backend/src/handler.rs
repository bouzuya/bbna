mod create_expo_push_token;
mod create_notifications;
mod delete_expo_push_token;
mod get_root;

#[cfg(test)]
pub(crate) use self::create_expo_push_token::CreateExpoPushTokenRequestBody;

use std::{collections::HashSet, sync::Arc};

use axum::Router;
use tokio::sync::Mutex;

#[derive(Clone, Debug, Default)]
pub struct App {
    expo_push_tokens: Arc<Mutex<HashSet<String>>>,
}

pub fn route(app: App) -> Router {
    Router::new()
        .merge(create_expo_push_token::route())
        .merge(create_notifications::route())
        .merge(delete_expo_push_token::route())
        .merge(get_root::route())
        .with_state(app)
}
