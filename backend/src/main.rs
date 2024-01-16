mod command;
mod handler;

use command::App;
use handler::route;
use tokio::net::TcpListener;

#[tokio::main]
async fn main() -> anyhow::Result<()> {
    let state = App::default();
    let router = route().with_state(state);
    let listener = TcpListener::bind("0.0.0.0:3000").await?;
    Ok(axum::serve(listener, router).await?)
}

#[cfg(test)]
mod tests {
    use axum::{async_trait, response::Response};

    #[async_trait]
    pub trait ResponseExt {
        async fn into_body_string(self) -> anyhow::Result<String>;
    }

    #[async_trait]
    impl ResponseExt for Response {
        async fn into_body_string(self) -> anyhow::Result<String> {
            Ok(String::from_utf8(
                axum::body::to_bytes(self.into_body(), usize::MAX)
                    .await?
                    .to_vec(),
            )?)
        }
    }
}
