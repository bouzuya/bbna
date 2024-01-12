use async_trait::async_trait;

use super::App;

#[async_trait]
#[cfg_attr(test, mockall::automock)]
pub trait CanCreateExpoPushToken {
    async fn create_expo_push_token(&self, input: Input) -> Result<Output, Error>;
}

pub struct Input {
    pub expo_push_token: String,
}

pub struct Output;
pub struct Error;

pub async fn handle(state: &App, Input { expo_push_token }: Input) -> Result<Output, Error> {
    let mut expo_push_tokens = state.expo_push_tokens.lock().await;
    // TODO: already exists
    expo_push_tokens.insert(expo_push_token);
    Ok(Output)
}
