use std::{collections::HashSet, sync::Arc};

use async_trait::async_trait;
use tokio::sync::Mutex;

use self::{
    create_expo_push_token::CanCreateExpoPushToken, delete_expo_push_token::CanDeleteExpoPushToken,
};

pub mod create_expo_push_token;
pub mod delete_expo_push_token;

#[derive(Clone, Debug, Default)]
pub struct App {
    // TODO: hide
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
impl CanDeleteExpoPushToken for App {
    async fn delete_expo_push_token(
        &self,
        input: delete_expo_push_token::Input,
    ) -> Result<delete_expo_push_token::Output, delete_expo_push_token::Error> {
        delete_expo_push_token::handle(self, input).await
    }
}
