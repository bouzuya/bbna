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

    #[tokio::test]
    async fn test_bbn_api_entries() -> anyhow::Result<()> {
        #[derive(Debug, PartialEq, serde::Deserialize)]
        struct Entry {
            date: String,
            minutes: u16,
            pubdate: String,
            tags: Vec<String>,
            title: String,
        }
        let response = reqwest::get("https://blog.bouzuya.net/posts.json").await?;
        assert!(response.status().is_success());
        let response_body = response.text().await?;
        let entries = serde_json::from_str::<'_, Vec<Entry>>(response_body.as_str())?;
        assert!(entries.contains(&Entry {
            date: "2012-04-08".to_string(),
            minutes: 35,
            pubdate: "2012-04-08T21:12:21+09:00".to_string(),
            tags: vec!["misc".to_string()],
            title: "じごくのそうべえ".to_string(),
        }));
        Ok(())
    }

    #[tokio::test]
    async fn test_bbn_api_entry() -> anyhow::Result<()> {
        #[derive(Debug, PartialEq, serde::Deserialize)]
        struct EntryDetail {
            data: String,
            date: String,
            minutes: u16,
            html: String,
            pubdate: String,
            tags: Vec<String>,
            title: String,
        }
        let response = reqwest::get("https://blog.bouzuya.net/2012/04/08.json").await?;
        assert!(response.status().is_success());
        let response_body = response.text().await?;
        let entry = serde_json::from_str::<'_, EntryDetail>(response_body.as_str())?;
        assert!(entry
            .data
            .starts_with("金曜日の夜が天国なら日曜日の夜は地獄である。"));
        assert_eq!(entry.date, "2012-04-08");
        assert_eq!(entry.minutes, 35);
        assert!(entry
            .html
            .starts_with("<p>金曜日の夜が天国なら日曜日の夜は地獄である。</p>"));
        assert_eq!(entry.pubdate, "2012-04-08T21:12:21+09:00");
        assert_eq!(entry.tags, vec!["misc".to_string()]);
        assert_eq!(entry.title, "じごくのそうべえ");
        Ok(())
    }
}
