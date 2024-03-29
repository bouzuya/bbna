mod create_expo_push_token;
mod create_notification;
mod delete_expo_push_token;
mod delete_invalid_expo_push_tokens;
mod get_root;

use axum::Router;

pub fn route<T>() -> Router<T>
where
    T: Clone
        + crate::command::create_expo_push_token::CanCreateExpoPushToken
        + crate::command::create_notification::CanCreateNotification
        + crate::command::delete_expo_push_token::CanDeleteExpoPushToken
        + crate::command::delete_invalid_expo_push_tokens::CanDeleteInvalidExpoPushTokens
        + Send
        + Sync
        + 'static,
{
    Router::new()
        .merge(create_expo_push_token::route())
        .merge(create_notification::route())
        .merge(delete_expo_push_token::route())
        .merge(delete_invalid_expo_push_tokens::route())
        .merge(get_root::route())
}
