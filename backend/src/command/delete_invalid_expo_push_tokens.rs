use async_trait::async_trait;

use super::App;

#[async_trait]
#[cfg_attr(test, mockall::automock)]
pub trait CanDeleteInvalidExpoPushTokens {
    async fn delete_invalid_expo_push_tokens(&self, input: Input) -> Result<Output, Error>;
}

pub struct Input;
pub struct Output;
pub struct Error;

pub async fn handle(_state: &App, _: Input) -> Result<Output, Error> {
    // TODO
    Ok(Output)
}
