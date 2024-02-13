use async_trait::async_trait;
use expo_push_notification_client::{
    Details, DetailsErrorType, Expo, ExpoClientOptions, ExpoPushReceipt,
};

use super::App;

#[async_trait]
#[cfg_attr(test, mockall::automock)]
pub trait CanDeleteInvalidExpoPushTokens {
    async fn delete_invalid_expo_push_tokens(&self, input: Input) -> Result<Output, Error>;
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
    let expo_push_ticket_ids = state.expo_push_ticket_ids.lock().await;

    let expo_client = Expo::new(ExpoClientOptions::default());

    // TODO: chunk

    let receipts = expo_client
        .get_push_notification_receipts(expo_push_ticket_ids.clone())
        .await?;

    for (_receipt_id, receipt) in receipts {
        match receipt {
            ExpoPushReceipt::Ok => {
                // do nothing
            }
            ExpoPushReceipt::Error(e) => {
                if let Some(Details { error: Some(error) }) = e.details {
                    match error {
                        DetailsErrorType::DeviceNotRegistered => {
                            // TODO: Remove expo_push_token from the database
                        }
                        DetailsErrorType::InvalidCredentials
                        | DetailsErrorType::MessageTooBig
                        | DetailsErrorType::MessageRateExceeded => {
                            // TODO: logging
                        }
                    }
                } else {
                    // TODO: logging
                }
            }
        }
    }

    Ok(Output)
}
