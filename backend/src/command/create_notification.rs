use async_trait::async_trait;
use expo_push_notification_client::{Expo, ExpoClientOptions, ExpoPushMessage, ExpoPushTicket};

use super::App;

#[async_trait]
#[cfg_attr(test, mockall::automock)]
pub trait CanCreateNotification {
    async fn create_notification(&self, input: Input) -> Result<Output, Error>;
}

pub struct Input;
pub struct Output;

#[derive(Debug, thiserror::Error)]
pub enum Error {
    #[error("expo push notification client error")]
    ExpoPushNotificationClient(#[from] expo_push_notification_client::CustomError),
    #[error("expo push notification client builder error")]
    ExpoPushNotificationClientBuilder(#[from] expo_push_notification_client::ValidationError),
}

pub async fn handle(state: &App, _: Input) -> Result<Output, Error> {
    let expo_push_tokens = state.expo_push_tokens.lock().await;
    let expo_client = Expo::new(ExpoClientOptions::default());
    let expo_push_tickets = expo_client
        .send_push_notifications(ExpoPushMessage::builder(expo_push_tokens.clone()).build()?)
        .await?;

    state.expo_push_tickets.lock().await.extend(
        expo_push_tickets
            .into_iter()
            .map(|ticket| match ticket {
                ExpoPushTicket::Ok(ticket) => ticket.id.to_string(),
                ExpoPushTicket::Error(_) => todo!(),
            })
            .collect::<Vec<String>>(),
    );

    Ok(Output)
}
