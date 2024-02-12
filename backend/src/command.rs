use std::{collections::HashSet, sync::Arc};

use async_trait::async_trait;
use tokio::sync::Mutex;

use self::{
    create_expo_push_token::CanCreateExpoPushToken, create_notification::CanCreateNotification,
    delete_expo_push_token::CanDeleteExpoPushToken,
    delete_invalid_expo_push_tokens::CanDeleteInvalidExpoPushTokens,
};

pub mod create_expo_push_token;
pub mod create_notification;
pub mod delete_expo_push_token;
pub mod delete_invalid_expo_push_tokens;

#[derive(Clone, Debug, Default)]
pub struct App {
    // TODO: hide
    pub expo_push_tickets: Arc<Mutex<HashSet<String>>>,
    pub expo_push_tokens: Arc<Mutex<HashSet<String>>>,
}

#[async_trait]
impl CanCreateExpoPushToken for App {
    async fn create_expo_push_token(
        &self,
        input: create_expo_push_token::Input,
    ) -> Result<create_expo_push_token::Output, create_expo_push_token::Error> {
        create_expo_push_token::handle(self, input).await
    }
}

#[async_trait]
impl CanCreateNotification for App {
    async fn create_notification(
        &self,
        input: create_notification::Input,
    ) -> Result<create_notification::Output, create_notification::Error> {
        create_notification::handle(self, input).await
    }
}

#[async_trait]
impl CanDeleteExpoPushToken for App {
    async fn delete_expo_push_token(
        &self,
        input: delete_expo_push_token::Input,
    ) -> Result<delete_expo_push_token::Output, delete_expo_push_token::Error> {
        delete_expo_push_token::handle(self, input).await
    }
}

#[async_trait]
impl CanDeleteInvalidExpoPushTokens for App {
    async fn delete_invalid_expo_push_tokens(
        &self,
        input: delete_invalid_expo_push_tokens::Input,
    ) -> Result<delete_invalid_expo_push_tokens::Output, delete_invalid_expo_push_tokens::Error>
    {
        delete_invalid_expo_push_tokens::handle(self, input).await
    }
}
